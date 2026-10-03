package net.luckystudio.cozyhome.item.custom;

import net.luckystudio.cozyhome.util.ModScreenTexts;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
public class PaintBrushItem extends BrushItem {

    private static final ChatFormatting CAPTION = ChatFormatting.GRAY;

    public PaintBrushItem(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public void usageTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks >= 0 && user instanceof Player playerEntity) {
            HitResult hitResult = this.getHitResult(playerEntity);
            if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks + 1;
                boolean bl = i % 10 == 5;
                if (bl) {
                    BlockPos blockPos = blockHitResult.getBlockPos();
                    BlockState blockState = world.getBlockState(blockPos);
                    HumanoidArm arm = user.getUsedItemHand() == InteractionHand.MAIN_HAND ? playerEntity.getMainArm() : playerEntity.getMainArm().getOpposite();
                    if (blockState.hasBlockBreakParticles() && blockState.getRenderShape() != RenderShape.INVISIBLE) {
                        this.addDustParticles(world, blockHitResult, blockState, user.getRotationVec(0.0F), arm);
                    }

                    SoundEvent soundEvent;
                    if (blockState.getBlock() instanceof BrushableBlock brushableBlock) {
                        soundEvent = brushableBlock.getBrushingSound();
                    } else {
                        soundEvent = SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC;
                    }

                    world.playSound(playerEntity, blockPos, soundEvent, SoundSource.BLOCKS);
                    if (!world.isClientSide() && world.getBlockEntity(blockPos) instanceof BrushableBlockEntity brushableBlockEntity) {
                        boolean bl2 = brushableBlockEntity.brush(world.getTime(), playerEntity, blockHitResult.getSide());
                        if (bl2) {
                            EquipmentSlot equipmentSlot = stack.equals(playerEntity.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                            stack.damage(1, user, equipmentSlot);
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
        return ProjectileUtil.getCollision(user, entity -> !entity.isSpectator() && entity.canHit(), user.getBlockInteractionRange());
    }

    private void addDustParticles(Level world, BlockHitResult hitResult, BlockState state, Vec3 userRotation, HumanoidArm arm) {
        double d = 3.0;
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        int j = world.getRandom().nextBetweenExclusive(7, 12);
        BlockParticleOption blockStateParticleEffect = new BlockParticleOption(ParticleTypes.BLOCK, state);
        Direction direction = hitResult.getSide();
        PaintBrushItem.DustParticlesOffset dustParticlesOffset = PaintBrushItem.DustParticlesOffset.fromSide(userRotation, direction);
        Vec3 vec3d = hitResult.getPos();

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
                case DOWN, UP -> new PaintBrushItem.DustParticlesOffset(userRotation.getZ(), 0.0, -userRotation.getX());
                case NORTH -> new PaintBrushItem.DustParticlesOffset(1.0, 0.0, -0.1);
                case SOUTH -> new PaintBrushItem.DustParticlesOffset(-1.0, 0.0, 0.1);
                case WEST -> new PaintBrushItem.DustParticlesOffset(-0.1, 0.0, -1.0);
                case EAST -> new PaintBrushItem.DustParticlesOffset(0.1, 0.0, 1.0);
            };
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.on_interacted_with_dyeable_block").formatted(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.sets_block_color")));
    }
}
