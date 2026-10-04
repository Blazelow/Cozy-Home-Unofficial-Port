package net.luckystudio.cozyhome.item.custom;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

import net.luckystudio.cozyhome.util.ModScreenTexts;
import java.util.List;
public class PaintBrushItem extends BrushItem {

    private static final ChatFormatting CAPTION = ChatFormatting.GRAY;

    public PaintBrushItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks >= 0 && user instanceof Player playerEntity) {
            HitResult hitResult = this.getHitResult(playerEntity);
            if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
                int i = this.getUseDuration(stack, user) - remainingUseTicks + 1;
                boolean bl = i % 10 == 5;
                if (bl) {
                    BlockPos blockPos = blockHitResult.getBlockPos();
                    BlockState blockState = world.getBlockState(blockPos);
                    HumanoidArm arm = user.getUsedItemHand() == InteractionHand.MAIN_HAND ? playerEntity.getMainArm() : playerEntity.getMainArm().getOpposite();
                    if (blockState.shouldSpawnTerrainParticles() && blockState.getRenderShape() != RenderShape.INVISIBLE) {
                        this.addDustParticles(world, blockHitResult, blockState, user.getViewVector(0.0F), arm);
                    }

                    SoundEvent soundEvent;
                    if (blockState.getBlock() instanceof BrushableBlock brushableBlock) {
                        soundEvent = brushableBlock.getBrushSound();
                    } else {
                        soundEvent = SoundEvents.BRUSH_GENERIC;
                    }

                    world.playSound(playerEntity, blockPos, soundEvent, SoundSource.BLOCKS);
                    if (!world.isClientSide() && world.getBlockEntity(blockPos) instanceof BrushableBlockEntity brushableBlockEntity) {
                        boolean bl2 = brushableBlockEntity.brush(world.getGameTime(), (ServerLevel) world, playerEntity, blockHitResult.getDirection(), stack);
                        if (bl2) {
                            EquipmentSlot equipmentSlot = stack.equals(playerEntity.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                            stack.hurtAndBreak(1, user, equipmentSlot);
                        }
                    }
                }

                return;
            }
            user.stopUsingItem();
        } else {
            user.stopUsingItem();
        }
    }

    private HitResult getHitResult(Player user) {
        return ProjectileUtil.getHitResultOnViewVector(user, entity -> !entity.isSpectator() && entity.isPickable(), user.blockInteractionRange());
    }

    private void addDustParticles(Level world, BlockHitResult hitResult, BlockState state, Vec3 userRotation, HumanoidArm arm) {
        double d = 3.0;
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        int j = world.getRandom().nextIntBetweenInclusive(7, 11);
        BlockParticleOption blockStateParticleEffect = new BlockParticleOption(ParticleTypes.BLOCK, state);
        Direction direction = hitResult.getDirection();
        PaintBrushItem.DustParticlesOffset dustParticlesOffset = PaintBrushItem.DustParticlesOffset.fromSide(userRotation, direction);
        Vec3 vec3d = hitResult.getLocation();

        for (int k = 0; k < j; k++) {
            world.addParticle(
                    blockStateParticleEffect,
                    vec3d.x - (double)(direction == Direction.WEST ? 1.0E-6F : 0.0F),
                    vec3d.y,
                    vec3d.z - (double)(direction == Direction.NORTH ? 1.0E-6F : 0.0F),
                    dustParticlesOffset.xd() * (double)i * 3.0 * world.getRandom().nextDouble(),
                    0.0,
                    dustParticlesOffset.zd() * (double)i * 3.0 * world.getRandom().nextDouble()
            );
        }
    }
    record DustParticlesOffset(double xd, double yd, double zd) {

        public static PaintBrushItem.DustParticlesOffset fromSide(Vec3 userRotation, Direction side) {
            double d = 0.0;

            return switch (side) {
                case DOWN, UP -> new PaintBrushItem.DustParticlesOffset(userRotation.z, 0.0, -userRotation.x);
                case NORTH -> new PaintBrushItem.DustParticlesOffset(1.0, 0.0, -0.1);
                case SOUTH -> new PaintBrushItem.DustParticlesOffset(-1.0, 0.0, 0.1);
                case WEST -> new PaintBrushItem.DustParticlesOffset(-0.1, 0.0, -1.0);
                case EAST -> new PaintBrushItem.DustParticlesOffset(0.1, 0.0, 1.0);
            };
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, display, tooltip, type);
        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(Component.translatable("tooltip.cozyhome.on_interacted_with_dyeable_block").withStyle(ChatFormatting.GRAY));
        tooltip.accept(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.sets_block_color")));
    }
}
