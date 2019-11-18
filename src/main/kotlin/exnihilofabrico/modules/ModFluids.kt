package exnihilofabrico.modules

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.id
import exnihilofabrico.modules.fluids.MilkFluid
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry

object ModFluids {
    val bucketItemSettings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(1).recipeRemainder(Items.BUCKET).maxCount(1)
    val blockSettings: Block.Settings = FabricBlockSettings.of(Material.WATER).noCollision().strength(100.0f, 100.0f).dropsNothing().build()

    fun registerFluids(registry: Registry<Fluid>) {
        Registry.register(registry, id("witchwater"), WitchWaterFluid.Still)
        Registry.register(registry, id("witchwater_flowing"), WitchWaterFluid.Flowing)
        Registry.register(registry, id("milk"), MilkFluid.Still)
        Registry.register(registry, id("milk_flowing"), MilkFluid.Flowing)

    }
}