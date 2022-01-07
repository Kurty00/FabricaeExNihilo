package wraith.fabricaeexnihilo.modules.infested;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.RegistryUtils;

import java.util.Optional;

public class InfestingLeavesBlockEntity extends BaseBlockEntity implements Colored {
    private double progress = 0.0;
    private int tickCounter;
    private InfestedLeavesBlock target = ModBlocks.INFESTED_LEAVES.values().stream().findFirst().orElseThrow();
    
    public static Identifier BLOCK_ENTITY_ID = FabricaeExNihilo.id("infesting");
    public static final BlockEntityType<InfestingLeavesBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            InfestingLeavesBlockEntity::new,
            ModBlocks.INFESTING_LEAVES
    ).build(null);
    
    public InfestingLeavesBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        tickCounter = world == null ? 0 : world.random.nextInt(FabricaeExNihilo.CONFIG.modules.barrels.tickRate);
    }
    
    public static void ticker(World world, BlockPos blockPos, BlockState blockState, InfestingLeavesBlockEntity infestedLeavesEntity) {
        // Don't update every single tick
        if (++infestedLeavesEntity.tickCounter % FabricaeExNihilo.CONFIG.modules.silkworms.updateFrequency != 0) {
            return;
        }
        // Advance
        infestedLeavesEntity.progress += FabricaeExNihilo.CONFIG.modules.silkworms.progressPerUpdate;
        
        if (infestedLeavesEntity.progress < 1f) {
            infestedLeavesEntity.markDirty();
            if (infestedLeavesEntity.progress > FabricaeExNihilo.CONFIG.modules.silkworms.minimumSpreadPercent && world != null) {
                InfestedHelper.tryToSpreadFrom(world, blockPos, FabricaeExNihilo.CONFIG.modules.silkworms.infestingSpreadAttempts);
            }
            return;
        }
        
        // Done Transforming
        if (world == null) {
            return;
        }
        var curState = world.getBlockState(blockPos);
        var newState = infestedLeavesEntity.target.getDefaultState()
                .with(LeavesBlock.DISTANCE, curState.get(LeavesBlock.DISTANCE))
                .with(LeavesBlock.PERSISTENT, curState.get(LeavesBlock.PERSISTENT));
        world.setBlockState(blockPos, newState);
    }
    
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt == null) {
            FabricaeExNihilo.LOGGER.warn("An infesting leaves block at " + pos + " is missing data.");
            return;
        }
        readNbtWithoutWorldInfo(nbt);
    }
    
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        toNBTWithoutWorldInfo(nbt);
    }
    
    public void toNBTWithoutWorldInfo(NbtCompound nbt) {
        nbt.putDouble("progress", progress);
        nbt.putString("target", RegistryUtils.getId(target).toString());
    }
    
    public void readNbtWithoutWorldInfo(NbtCompound nbt) {
        progress = nbt.getDouble("progress");
        target = Registry.BLOCK.getOrEmpty(new Identifier(nbt.getString("target")))
                .flatMap(block -> block instanceof InfestedLeavesBlock infested ? Optional.of(infested) : Optional.empty())
                .orElse(ModBlocks.INFESTED_LEAVES.values().stream().findFirst().orElseThrow());
    }
    
    @Override
    public int getColor(int index) {
        var originalColor = MinecraftClient.getInstance().getBlockColors().getColor(Registry.BLOCK.get(target.getLeafBlock()).getDefaultState(), world, pos, 0);
        return Color.average(Color.WHITE, new Color(originalColor), progress).toInt();
    }
    
    public InfestedLeavesBlock getTarget() {
        return target;
    }
    
    public void setTarget(InfestedLeavesBlock target) {
        this.target = target;
    }
}