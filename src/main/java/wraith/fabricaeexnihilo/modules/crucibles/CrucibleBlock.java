package wraith.fabricaeexnihilo.modules.crucibles;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.util.VoxelShapeHelper;

public class CrucibleBlock extends BlockWithEntity {
    public CrucibleBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world == null || world.isClient() || pos == null) {
            return ActionResult.PASS;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            return crucible.activate(player, hand);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (fromPos == null || !fromPos.equals(pos == null ? null : pos.down()) || world == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            crucible.updateHeat();
        }
    }
    
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (world == null || world.isClient() || pos == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            crucible.updateHeat();
        }
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    /**
     * BlockEntity functions
     */
    
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleBlockEntity(pos, state);
    }
    
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            EnchantmentHelper.get(itemStack).forEach(crucible.getEnchantments()::setEnchantmentLevel);
        }
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, CrucibleBlockEntity.TYPE, CrucibleBlockEntity::ticker);
    }
    
    private static final VoxelShape SHAPE;
    
    static {
        SHAPE = VoxelShapeHelper.union(
                createCuboidShape(0.0, 0.0, 0.0, 3.0, 3.0, 3.0),
                createCuboidShape(0.0, 0.0, 13.0, 3.0, 3.0, 16.0),
                createCuboidShape(13.0, 0.0, 0.0, 16.0, 3.0, 3.0),
                createCuboidShape(13.0, 0.0, 13.0, 16.0, 3.0, 16.0),
                createCuboidShape(0.0, 3.0, 0.0, 16.0, 16.0, 16.0)
        );
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
}