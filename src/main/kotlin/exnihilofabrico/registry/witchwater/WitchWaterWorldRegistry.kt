package exnihilofabrico.registry.witchwater

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.WeightedList
import exnihilofabrico.api.recipes.WitchWaterWorldRecipe
import exnihilofabrico.api.registry.IWitchWaterWorldRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.registry.AbstractRegistry
import net.minecraft.fluid.Fluid
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*

data class WitchWaterWorldRegistry(val registry: MutableList<WitchWaterWorldRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<WitchWaterWorldRecipe>>(), IWitchWaterWorldRegistry {

    override fun clear() = registry.clear()

    override fun register(fluid: FluidIngredient, result: WeightedList) {
        val match = registry.firstOrNull { it.fluid == fluid }
        if(match != null)
            match.results.amend(result)
        else {
            registry.add(WitchWaterWorldRecipe(fluid, result))
        }
    }

    override fun isRegistered(fluid: Fluid) = registry.any { it.fluid.test(fluid) }

    override fun getResult(fluid: Fluid, rand: Random) = getAllResults(fluid)?.choose(rand)
    override fun getAllResults(fluid: Fluid): WeightedList? {
        registry.forEach {
            if(it.fluid.test(fluid))
                return it.results
        }
        return null
    }

    override fun registerJson(file: File) {
        val json: MutableList<WitchWaterWorldRecipe> = gson.fromJson(FileReader(file),
            SERIALIZATION_TYPE
        )
        json.forEach { register(it.fluid, it.results) }
    }

    override fun serializable() = registry

    companion object {
        val SERIALIZATION_TYPE: Type = object: TypeToken<MutableList<WitchWaterWorldRecipe>>(){}.type
        // TODO fix serialization
        fun fromJson(file: File) = fromJson(
            file,
            { WitchWaterWorldRegistry() },
            MetaModule::registerWitchWaterWorld
        )
    }
}