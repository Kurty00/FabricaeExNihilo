package wraith.fabricaeexnihilo.json.recipe;

import com.google.gson.*;
import wraith.fabricaeexnihilo.api.crafting.BlockIngredient;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.recipes.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.json.BaseJson;

import java.lang.reflect.Type;

public final class CrucibleHeatRecipeJson extends BaseJson<CrucibleHeatRecipe> {

    private CrucibleHeatRecipeJson() {}

    public static final CrucibleHeatRecipeJson INSTANCE = new CrucibleHeatRecipeJson();

    @Override
    public CrucibleHeatRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        return new CrucibleHeatRecipe(
            obj.has("block") ? BlockIngredient.fromJson(obj.get("block")) : BlockIngredient.EMPTY,
            obj.has("fluid") ? FluidIngredient.fromJson(obj.get("fluid")) : FluidIngredient.EMPTY,
            obj.get("heat").getAsInt()
        );
    }

    @Override
    public JsonElement serialize(CrucibleHeatRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();
        if(!src.ingredient().isEmpty()) {
            obj.add("block", src.ingredient().toJson());
        }
        if(!src.fluid().isEmpty()) {
            obj.add("fluid", src.fluid().toJson());
        }
        obj.add("heat", context.serialize(src.value()));
        return obj;
    }

}