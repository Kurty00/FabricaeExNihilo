package wraith.fabricaeexnihilo.util;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.datagen.DatagenItems;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.farming.PlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TallPlantableItem;
import wraith.fabricaeexnihilo.modules.farming.TransformingItem;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesBlock;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntrypointHelper {
    public static final Map<ItemConvertible, List<ConditionJsonProvider>> CONDITIONS = Maps.newIdentityHashMap();

    public static void callEntrypoints() {
        var entrypoints = FabricLoader.getInstance().getEntrypoints("fabricaeexnihilo:api", FENApiModule.class).stream();

        // Enable all compat modules when running datagen to ensure all
        // data gets generated regardless of currently active dependencies
        if (System.getProperty("fabric-api.datagen") != null) {
            DatagenItems.register();
        } else {
            entrypoints = entrypoints.filter(FENApiModule::shouldLoad);
        }

        entrypoints.forEach(entrypoint -> entrypoint.onInit(new FENRegistriesImpl(entrypoint.getResourceCondition())));
    }

    private static Identifier id(String ore, @Nullable String prefix, @Nullable String suffix) {
        return FabricaeExNihilo.id((prefix == null ? "" : prefix) + ore + (suffix == null ? "" : suffix));
    }

    private static final class FENRegistriesImpl implements FENRegistries {
        @Nullable
        private final ConditionJsonProvider condition;

        private FENRegistriesImpl(@Nullable ConditionJsonProvider condition) {
            this.condition = condition;
        }

        @Override
        public FabricItemSettings defaultItemSettings() {
            return new FabricItemSettings();
        }

        @Override
        public FabricBlockSettings gravelyBlockSettings() {
            return FabricBlockSettings.copyOf(Blocks.GRAVEL);
        }

        @Override
        public FabricBlockSettings infestedLeavesBlockSettings() {
            return FabricBlockSettings.copyOf(ModBlocks.INFESTED_LEAVES_SETTINGS);
        }

        @Override
        public FabricBlockSettings sandyBlockSettings() {
            return FabricBlockSettings.copyOf(Blocks.SAND);
        }

        @Override
        public FabricBlockSettings stoneBlockSettings() {
            return FabricBlockSettings.copyOf(Blocks.STONE).requiresTool();
        }

        @Override
        public FabricBlockSettings woodenBlockSettings() {
            return FabricBlockSettings.copyOf(Blocks.OAK_PLANKS);
        }

        @Override
        public Block registerBarrel(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            var id = id(name, null, "_barrel");
            var block = ModBlocks.BARRELS.computeIfAbsent(id, __ -> {
                var block1 = new BarrelBlock(settings, isFireproof);
                if (!isFireproof) FlammableBlockRegistry.getDefaultInstance().add(block1, 5, 20);
                return block1;
            });
            if (condition != null)
                CONDITIONS.computeIfAbsent(block, __ -> new ArrayList<>()).add(condition);
            return block;
        }

        @Override
        public Block registerCrucible(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            var id = id(name, null, "_crucible");
            var block = ModBlocks.CRUCIBLES.computeIfAbsent(id, __ -> {
                var block1 = new CrucibleBlock(settings, isFireproof);
                if (!isFireproof) FlammableBlockRegistry.getDefaultInstance().add(block1, 5, 20);
                return block1;
            });
            if (condition != null)
                CONDITIONS.computeIfAbsent(block, __ -> new ArrayList<>()).add(condition);
            return block;
        }

        @Override
        public Block registerCrushedBlock(String name, AbstractBlock.Settings settings) {
            var id = id(name, null, null);
            var block = ModBlocks.CRUSHED.computeIfAbsent(id, __ -> new FallingBlock(settings));
            if (condition != null)
                CONDITIONS.computeIfAbsent(block, __ -> new ArrayList<>()).add(condition);
            return block;
        }

        @Override
        public Block registerInfestedLeaves(String name, Identifier source, AbstractBlock.Settings settings) {
            //TODO: Make these stack somehow: multiple sources for one result.
            var id = id(name, "infested_", "_leaves");
            var block = ModBlocks.INFESTED_LEAVES.computeIfAbsent(id, __ -> {
                var block1 = new InfestedLeavesBlock(source, settings);
                FlammableBlockRegistry.getDefaultInstance().add(block1, 30, 60);
                return block1;
            });
            if (condition != null)
                CONDITIONS.computeIfAbsent(block, __ -> new ArrayList<>()).add(condition);
            return block;
        }

        @Override
        public Item registerMesh(String name, Color color, int enchantability, Item.Settings settings) {
            var id = id(name, null, "_mesh");
            var item = ModItems.MESHES.computeIfAbsent(id, __ -> new MeshItem(color, enchantability, settings));
            if (condition != null)
                CONDITIONS.computeIfAbsent(item, __ -> new ArrayList<>()).add(condition);
            return item;
        }

        @Override
        public Item registerOrePiece(String name, Item.Settings settings) {
            var id = id(name, "raw_", "_piece");
            var item = ModItems.ORE_PIECES.computeIfAbsent(id, __ -> new Item(settings));
            if (condition != null)
                CONDITIONS.computeIfAbsent(item, __ -> new ArrayList<>()).add(condition);
            return item;
        }

        @Override
        public Item registerSeed(String name, Lazy<Block[]> plants) {
            var id = id(name, null, "_seeds");
            var item = ModItems.SEEDS.computeIfAbsent(id, __ -> new PlantableItem(plants, ModItems.BASE_SETTINGS));
            if (condition != null)
                CONDITIONS.computeIfAbsent(item, __ -> new ArrayList<>()).add(condition);
            return item;
        }

        @Override
        public Block registerSieve(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            var id = id(name, null, "_sieve");
            var block = ModBlocks.SIEVES.computeIfAbsent(id, __ -> {
                var block1 = new SieveBlock(settings);
                if (!isFireproof) FlammableBlockRegistry.getDefaultInstance().add(block1, 5, 20);
                return block1;
            });
            if (condition != null)
                CONDITIONS.computeIfAbsent(block, __ -> new ArrayList<>()).add(condition);
            return block;
        }

        @Override
        public Block registerStrainer(String name, boolean isFireproof, AbstractBlock.Settings settings) {
            var id = id(name, null, "_strainer");
            var block = ModBlocks.STRAINERS.computeIfAbsent(id, __ -> {
                var block1 = new StrainerBlock(settings);
                if (!isFireproof) FlammableBlockRegistry.getDefaultInstance().add(block1, 5, 20);
                return block1;
            });
            if (condition != null)
                CONDITIONS.computeIfAbsent(block, __ -> new ArrayList<>()).add(condition);
            return block;
        }

        @Override
        public Item registerTallPlantSeed(String name, Lazy<TallPlantBlock[]> plants) {
            var id = id(name, null, "_seeds");
            var item = ModItems.SEEDS.computeIfAbsent(id, __ -> new TallPlantableItem(plants, ModItems.BASE_SETTINGS));
            if (condition != null)
                CONDITIONS.computeIfAbsent(item, __ -> new ArrayList<>()).add(condition);
            return item;
        }

        @Override
        public Item registerTransformingSeed(String name, Lazy<Block> from, Lazy<Block> to) {
            var id = id(name, null, "_seeds");
            var item = ModItems.SEEDS.computeIfAbsent(id, __ -> new TransformingItem(from, to, ModItems.BASE_SETTINGS));
            if (condition != null)
                CONDITIONS.computeIfAbsent(item, __ -> new ArrayList<>()).add(condition);
            return item;
        }

        @Override
        public WoodenBlockBundle registerWood(String name, boolean fireproof, AbstractBlock.Settings settings) {
            var sieve = registerSieve(name, fireproof, settings);
            var strainer = registerStrainer(name, fireproof, settings);
            var barrel = registerBarrel(name, fireproof, settings);
            var crucible = registerCrucible(name, fireproof, settings);
            return new WoodBlockBundleImpl(sieve, strainer, barrel, crucible);
        }

        private record WoodBlockBundleImpl(Block sieve, Block strainer, Block barrel, Block crucible) implements WoodenBlockBundle {
        }
    }
}
