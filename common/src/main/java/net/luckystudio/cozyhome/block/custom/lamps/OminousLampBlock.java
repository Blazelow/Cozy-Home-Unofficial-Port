package net.luckystudio.cozyhome.block.custom.lamps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
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

import com.mojang.serialization.MapCodec;
import net.luckystudio.cozyhome.block.util.enums.VerticalLinearConnectionBlock;
import org.jetbrains.annotations.Nullable;
public class OminousLampBlock extends AbstractLampBlock {
    public static final MapCodec<OminousLampBlock> CODEC = simpleCodec(OminousLampBlock::new);
    public static final VoxelShape TOP_PIECE = Block.box(3, 2, 3, 13, 8, 13);
    public static final VoxelShape BOTTOM_PIECE = Block.box(5, 0, 5, 11, 2, 11);

    public static final VoxelShape SINGLE_SHAPE = Shapes.or(TOP_PIECE, BOTTOM_PIECE);
    public static final VoxelShape TOP_SHAPE = Shapes.or(TOP_PIECE, Block.box(6, 0, 6, 10, 4, 10));
    public static final VoxelShape MIDDLE_SHAPE = Block.box(6, 0, 6, 10, 16, 10);
    public static final VoxelShape BOTTOM_SHAPE = Shapes.or(BOTTOM_PIECE, Block.box(6, 2, 6, 10, 16, 10));

    public OminousLampBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CONNECTION)) {
            case HEAD -> TOP_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            case TAIL -> BOTTOM_SHAPE;
            default -> SINGLE_SHAPE;
        };
    }

    // Gives the Iron Lamp that default looking color we all know
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) instanceof LampBlockEntity lampBlockEntity) {
            final int color = DyedItemColor.getOrDefault(itemStack, -5231066);
            DataComponentMap components = DataComponentMap.builder().set(DataComponents.DYED_COLOR, new DyedItemColor(color)).build();
            lampBlockEntity.setComponents(components);
            lampBlockEntity.setChanged();
            world.sendBlockUpdated(pos, state, state, 0);
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    // Add flame particles to the lamp when on
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (state.getValue(CONNECTION) == VerticalLinearConnectionBlock.HEAD || state.getValue(CONNECTION) == VerticalLinearConnectionBlock.SINGLE) {
            if (state.getValue(LIT)) {
                double x = pos.getX() + 0.5D;
                double y = pos.getY() + 0.55D;
                double z = pos.getZ() + 0.5D;
                world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
