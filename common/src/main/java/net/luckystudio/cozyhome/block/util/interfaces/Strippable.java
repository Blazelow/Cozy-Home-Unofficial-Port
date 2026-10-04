package net.luckystudio.cozyhome.block.util.interfaces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface Strippable {

    // Method to define the stripped version of the block
    static Block getStrippedVersion(BlockState state) {

        // Get the registry name of the original block using the new method for Fabric 1.21
        Identifier originalBlockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());

        // Construct the stripped version's registry name by adding the prefix "stripped_"
        Identifier strippedBlockId = Identifier.fromNamespaceAndPath(originalBlockId.getNamespace(), "stripped_" + originalBlockId.getPath());

        // Look up the stripped block from the registry
        Block strippedBlock = BuiltInRegistries.BLOCK.getValue(strippedBlockId);

        // If the stripped block exists, return it, otherwise default to a safe block
        return strippedBlock;  // Default to a safe block (like oak wood) in case not found
    }

    // Method that is called when an axe is used on the block
    static InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Block strippedBlock = getStrippedVersion(state);
        if (strippedBlock != Blocks.AIR) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.getItem() instanceof AxeItem) {
                if (!world.isClientSide()) {
                    // Set the block state to the stripped block while keeping the original state
                    world.setBlock(pos, strippedBlock.withPropertiesOf(state), 11);  // 11 is for notifying neighbors and updating the world
                    world.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1, 1);
                    // Damage the axe
                    itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());  // Axe durability damage
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }
}

