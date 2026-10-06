package net.luckystudio.cozyhome.block.custom.birdbath;

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import net.luckystudio.cozyhome.util.ModScreenTexts;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/** A garden bird bath. A bucket fills it, an empty bucket takes the water back, and rain fills it too. */
public class BirdBathBlock extends Block {
    public static final MapCodec<BirdBathBlock> CODEC = simpleCodec(BirdBathBlock::new);
    public static final EnumProperty<ContainsBlock> CONTAINS = ModProperties.CONTAINS;

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 2, 14),
            Block.box(4, 2, 4, 12, 10, 12),
            Block.box(0, 10, 0, 16, 16, 16));

    public BirdBathBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS, ContainsBlock.NONE));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        boolean filled = state.getValue(CONTAINS) == ContainsBlock.WATER;
        if (stack.is(Items.WATER_BUCKET) && !filled) {
            if (!world.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                player.awardStat(Stats.FILL_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                world.setBlock(pos, state.setValue(CONTAINS, ContainsBlock.WATER), Block.UPDATE_ALL);
                world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        }
        if (stack.is(Items.BUCKET) && filled) {
            if (!world.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                world.setBlock(pos, state.setValue(CONTAINS, ContainsBlock.NONE), Block.UPDATE_ALL);
                world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    // Rain slowly fills an empty bird bath
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(CONTAINS) == ContainsBlock.NONE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(CONTAINS) == ContainsBlock.NONE && world.isRainingAt(pos.above()) && random.nextInt(3) == 0) {
            world.setBlock(pos, state.setValue(CONTAINS, ContainsBlock.WATER), Block.UPDATE_ALL);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.cozyhome.interact_with_bucket").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.fills_or_empties")));
        tooltip.add(Component.translatable("tooltip.cozyhome.rain").withStyle(ChatFormatting.GRAY));
        tooltip.add(ModScreenTexts.entry().append(Component.translatable("tooltip.cozyhome.fills_when_raining")));
    }
}
