package net.luckystudio.cozyhome.block.custom.lamps;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class IronLampBlock extends AbstractLampBlock {
    public static final MapCodec<IronLampBlock> CODEC = createCodec(IronLampBlock::new);
    public static final VoxelShape SINGLE_SHAPE = Shapes.or(
            Block.box(5, 0, 5, 11, 14, 11),
            Block.box(3, 2, 3, 13, 12, 13));
    public static final VoxelShape MIDDLE_SHAPE = Block.box(6, 0, 6, 10, 16, 10);
    public static final VoxelShape BOTTOM_SHAPE = Shapes.or(MIDDLE_SHAPE, Block.box(4, 0, 4, 12, 4, 12));
    public IronLampBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CONNECTION)) {
            case MIDDLE -> MIDDLE_SHAPE;
            case TAIL -> BOTTOM_SHAPE;
            default -> SINGLE_SHAPE;
        };
    }

    // Gives the Iron Lamp that default looking color we all know
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) instanceof LampBlockEntity lampBlockEntity) {
            final int color = DyedItemColor.getOrDefault(itemStack, -1005508);
            DataComponentMap components = DataComponentMap.builder().add(DataComponents.DYED_COLOR, new DyedItemColor(color, false)).build();
            lampBlockEntity.setComponents(components);
            lampBlockEntity.setChanged();
            world.sendBlockUpdated(pos, state, state, 0);
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }
}
