package net.luckystudio.cozyhome.block.custom;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import net.luckystudio.cozyhome.block.util.ModProperties;
import net.luckystudio.cozyhome.block.util.enums.HorizontalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.enums.VerticalLinearConnectionBlock;
import net.luckystudio.cozyhome.block.util.interfaces.ConnectingBlock;
public class FireplaceBlock extends Block implements ConnectingBlock {
    public static final EnumProperty<HorizontalLinearConnectionBlock> HORIZONTAL_LINEAR_CONNECTION = ModProperties.HORIZONTAL_CONNECTION;
    public static final EnumProperty<VerticalLinearConnectionBlock> VERTICAL_LINEAR_CONNECTION = ModProperties.VERTICAL_CONNECTION;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public FireplaceBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HORIZONTAL_LINEAR_CONNECTION, HorizontalLinearConnectionBlock.SINGLE)
                .setValue(VERTICAL_LINEAR_CONNECTION, VerticalLinearConnectionBlock.SINGLE)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public boolean isMatchingBlock(BlockState targetState) {
        return targetState.getBlock() == this;
    }
}
