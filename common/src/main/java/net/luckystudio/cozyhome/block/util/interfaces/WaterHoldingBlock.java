package net.luckystudio.cozyhome.block.util.interfaces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import net.luckystudio.cozyhome.block.custom.water_holding_blocks.AbstractWaterHoldingBlockEntity;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import java.util.List;
public interface WaterHoldingBlock {

    float getLiquidLevelHeight(BlockState state);

    List<Direction> getDirectionsToPull(BlockState state);

    Direction pullingDirection(BlockState state, Level world, BlockPos pos);

    boolean isFull(BlockState state);

    void addLiquid(BlockState state, Level world, BlockPos pos, BlockState pullState, Direction pullDirection);

    void removeLiquid(BlockState state, Level world, BlockPos pos);

    static boolean trySoup(Item item, Level world, BlockPos pos, Player player, InteractionHand hand, ContainsBlock contents) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item); // or BuiltInRegistries.ITEM.getKey(item) in newer versions

        boolean isSupplementariesSoup = id.toString().equals("supplementaries:soap");
        if (isSupplementariesSoup) {
            if (world.getBlockEntity(pos) instanceof AbstractWaterHoldingBlockEntity blockEntity && contents == ContainsBlock.WATER) {
                blockEntity.soupTime = blockEntity.soupTime + 200;
                player.getItemInHand(hand).consume(1, player);
                return true;
            } else {
                player.sendOverlayMessage(Component.translatable("message.cozyhome.needs_liquid"));
            }
        }
        return false;
    }

    static InteractionResult toggleSwitch(BlockState state, Level world, BlockPos pos, Player player) {
        if (!(state.getBlock() instanceof WaterHoldingBlock waterHoldingBlock)) return InteractionResult.TRY_WITH_EMPTY_HAND;

        if (!player.isShiftKeyDown()) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (state.getValue(BlockStateProperties.TRIGGERED) || (waterHoldingBlock.pullingDirection(state, world, pos) != null && !waterHoldingBlock.isFull(state))) {
            world.setBlock(pos, state.cycle(BlockStateProperties.TRIGGERED), Block.UPDATE_ALL);
        }

        player.sendOverlayMessage(Component.translatable("message.cozyhome.needs_liquid"));
        return InteractionResult.SUCCESS;
    }
}