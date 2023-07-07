package wraith.fabricaeexnihilo.datagen.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;

import java.util.function.Consumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;
import static wraith.fabricaeexnihilo.datagen.builder.recipe.ToolRecipeJsonBuilder.crooking;
import static wraith.fabricaeexnihilo.datagen.builder.recipe.ToolRecipeJsonBuilder.hammering;

public class ToolRecipeProvider extends FabricRecipeProvider {
    public ToolRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        crooking(Blocks.ACACIA_LEAVES, Items.ACACIA_SAPLING, 0.25).offerTo(exporter, "crooking/acacia_sapling");
        crooking(Blocks.BIRCH_LEAVES, Items.BIRCH_SAPLING, 0.25).offerTo(exporter, "crooking/birch_sapling");
        crooking(Blocks.DARK_OAK_LEAVES, Items.DARK_OAK_SAPLING, 0.25).offerTo(exporter, "crooking/dark_oak_sapling");
        crooking(Blocks.JUNGLE_LEAVES, Items.JUNGLE_SAPLING, 0.25).offerTo(exporter, "crooking/jungle_sapling");
        crooking(Blocks.OAK_LEAVES, Items.OAK_SAPLING, 0.25).offerTo(exporter, "crooking/oak_sapling");
        crooking(Blocks.SPRUCE_LEAVES, Items.SPRUCE_SAPLING, 0.25).offerTo(exporter, "crooking/spruce_sapling");
        crooking(BlockTags.LEAVES, ModItems.RAW_SILKWORM, 0.1, 0.2, 0.3).offerTo(exporter, "crooking/silkwork");
        crooking(BlockTags.LEAVES, Items.STICK, 0.01).offerTo(exporter, "crooking/stick");
        crooking(ModTags.INFESTED_LEAVES, Items.STRING, 0.75, 0.25).offerTo(exporter, "crooking/string");

        offerCoralHammeringRecipes(Blocks.BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL, Blocks.BRAIN_CORAL_FAN, "brain", exporter);
        offerCoralHammeringRecipes(Blocks.BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL, Blocks.BUBBLE_CORAL_FAN, "bubble", exporter);
        offerCoralHammeringRecipes(Blocks.FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL, Blocks.FIRE_CORAL_FAN, "fire", exporter);
        offerCoralHammeringRecipes(Blocks.HORN_CORAL_BLOCK, Blocks.HORN_CORAL, Blocks.HORN_CORAL_FAN, "horn", exporter);
        offerCoralHammeringRecipes(Blocks.TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL, Blocks.TUBE_CORAL_FAN, "tube", exporter);

        hammering(Blocks.PRISMARINE, ModBlocks.CRUSHED.get(id("crushed_prismarine")), 1).offerTo(exporter, "hammering/rock/prismarine");
        hammering(Blocks.ANDESITE, ModBlocks.CRUSHED.get(id("crushed_andesite")), 1).offerTo(exporter, "hammering/rock/andesite");
        hammering(Blocks.CALCITE, ModBlocks.CRUSHED.get(id("crushed_calcite")), 1).offerTo(exporter, "hammering/rock/calcite");
        hammering(Blocks.DIORITE, ModBlocks.CRUSHED.get(id("crushed_diorite")), 1).offerTo(exporter, "hammering/rock/diorite");
        hammering(Blocks.GRANITE, ModBlocks.CRUSHED.get(id("crushed_granite")), 1).offerTo(exporter, "hammering/rock/granite");
        hammering(ModBlocks.CRUSHED.get(id("crushed_granite")), Blocks.RED_SAND, 1).offerTo(exporter, "hammering/rock/crushed_granite");
        hammering(Blocks.NETHERRACK, ModBlocks.CRUSHED.get(id("crushed_netherrack")), 1).offerTo(exporter, "hammering/rock/netherrack");
        hammering(Blocks.NETHER_BRICKS, ModBlocks.CRUSHED.get(id("crushed_netherrack")), 1).offerTo(exporter, "hammering/rock/nether_bricks");
        hammering(Blocks.END_STONE, ModBlocks.CRUSHED.get(id("crushed_endstone")), 1).offerTo(exporter, "hammering/rock/end_stone");
        hammering(Blocks.END_STONE_BRICKS, ModBlocks.CRUSHED.get(id("crushed_endstone")), 1).offerTo(exporter, "hammering/rock/end_stone_bricks");

        hammering(Blocks.BLACKSTONE, ModBlocks.CRUSHED.get(id("crushed_blackstone")), 1).offerTo(exporter, "hammering/stone/blackstone");
        hammering(Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE, 1).offerTo(exporter, "hammering/stone/deepslate");
        hammering(Blocks.COBBLED_DEEPSLATE, ModBlocks.CRUSHED.get(id("crushed_deepslate")), 1).offerTo(exporter, "hammering/stone/cobbled_deepslate");
        hammering(Blocks.STONE, Blocks.COBBLESTONE, 1).offerTo(exporter, "hammering/stone/stone");
        hammering(Blocks.COBBLESTONE, Blocks.GRAVEL, 1).offerTo(exporter, "hammering/stone/cobblestone");
        hammering(Blocks.GRAVEL, Blocks.SAND, 1).offerTo(exporter, "hammering/stone/gravel");
        hammering(Blocks.SAND, ModBlocks.CRUSHED.get(id("silt")), 1).offerTo(exporter, "hammering/stone/sand");
        hammering(ModBlocks.CRUSHED.get(id("silt")), ModBlocks.CRUSHED.get(id("dust")), 1).offerTo(exporter, "hammering/stone/silt");

        hammering(ModTags.Common.CONCRETE_POWDERS, ModBlocks.CRUSHED.get(id("silt")), 1).offerTo(exporter, "hammering/concrete_to_silt");
        hammering(ConventionalBlockTags.GLASS_BLOCKS, Blocks.SAND, 1).offerTo(exporter, "hammering/glass_to_sand");
        hammering(BlockTags.WOOL, Items.STRING, 1, 1, 1, 1).offerTo(exporter, "hammering/wool_to_string");
    }

    private void offerCoralHammeringRecipes(Block block, Block coral, Block fan, String id, Consumer<RecipeJsonProvider> exporter) {
        hammering(block, coral, 1, 1, 0.5, 0.1).offerTo(exporter, "hammering/coral/" + id + "_block");
        hammering(coral, fan, 1, 0.5).offerTo(exporter, "hammering/coral/" + id);
    }

    @Override
    public String getName() {
        return "Tool Recipes";
    }
}
