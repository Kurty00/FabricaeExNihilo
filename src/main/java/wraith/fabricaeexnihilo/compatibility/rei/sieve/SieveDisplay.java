package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.fluid.Fluids;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.SieveRecipeKey;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.SieveRecipeOutputs;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SieveDisplay implements Display {
    public final boolean waterlogged;
    public final EntryIngredient input;
    public final EntryIngredient mesh;
    public final Map<EntryIngredient, List<Double>> outputs;

    public SieveDisplay(SieveRecipeKey key, SieveRecipeOutputs outputs) {
        this.waterlogged = key.waterlogged();
        this.input = EntryIngredients.ofIngredient(key.input());
        this.mesh = EntryIngredients.of(key.mesh());
        this.outputs = outputs.outputs()
                .entries()
                .stream()
                .map(entry -> Map.entry(EntryIngredients.of(entry.getKey()), entry.getValue()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.SIFTING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return waterlogged ? List.of(input, mesh, EntryIngredients.of(Fluids.WATER)) : List.of(input, mesh);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.copyOf(outputs.keySet());
    }

}