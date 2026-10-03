package net.luckystudio.cozyhome.block.util.interfaces;

import net.luckystudio.cozyhome.block.custom.water_holding_blocks.AbstractWaterHoldingBlockEntity;
import net.luckystudio.cozyhome.block.util.enums.ContainsBlock;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
public interface WaterHoldingBlock {

    float getLiquidLevelHeight(BlockState state);

    List<Direction> getDirectionsToPull(BlockState state);

    Direction pullingDirection(BlockState state, Level world, BlockPos pos);

    boolean isFull(BlockState state);

    void addLiquid(BlockState state, Level world, BlockPos pos, BlockState pullState, Direction pullDirection);

    void removeLiquid(BlockState state, Level world, BlockPos pos);

    static boolean trySoup(Item item, Level world, BlockPos pos, Player player, InteractionHand hand, ContainsBlock contents) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item); // or BuiltInRegistries.ITEM.getKey(item) in newer versions

        boolean isSupplementariesSoup = id.toString().equals("supplementaries:soap");
        if (isSupplementariesSoup) {
            if (world.getBlockEntity(pos) instanceof AbstractWaterHoldingBlockEntity blockEntity && contents == ContainsBlock.WATER) {
                blockEntity.soupTime = blockEntity.soupTime + 200;
                player.getItemInHand(hand).consume(1, player);
                return true;
            } else {
                player.displayClientMessage(Component.translatable("message.cozyhome.needs_liquid"), true);
            }
        }
        return false;
    }

    static ItemInteractionResult toggleSwitch(BlockState state, Level world, BlockPos pos, Player player) {
        if (!(state.getBlock() instanceof WaterHoldingBlock waterHoldingBlock)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (!player.isShiftKeyDown()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (state.getValue(BlockStateProperties.TRIGGERED) || (waterHoldingBlock.pullingDirection(state, world, pos) != null && !waterHoldingBlock.isFull(state))) {
            world.setBlock(pos, state.cycle(BlockStateProperties.TRIGGERED), Block.UPDATE_ALL);
        }

        player.displayClientMessage(Component.translatable("message.cozyhome.needs_liquid"), true);
        return ItemInteractionResult.SUCCESS;
    }
}