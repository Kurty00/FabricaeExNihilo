package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.crafting.BlockIngredient;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.modules.ModRecipes;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class LeakingRecipe extends BaseRecipe<LeakingRecipe.LeakingRecipeContext> {
    private final BlockIngredient block;
    private final FluidIngredient fluid;
    private final long amount;
    private final Block result;
    
    public LeakingRecipe(Identifier id, BlockIngredient block, FluidIngredient fluid, long amount, Block result) {
        super(id);
        this.block = block;
        this.fluid = fluid;
        this.amount = amount;
        this.result = result;
    }
    
    
    public static Optional<LeakingRecipe> find(Block block, FluidVariant fluid, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.LEAKING, new LeakingRecipeContext(block, fluid), world);
    }
    
    
    @Override
    public boolean matches(LeakingRecipeContext context, World world) {
        return block.test(context.block) && fluid.test(context.fluid);
    }
    
    @Override
    public ItemStack getDisplayStack() {
        return result.asItem().getDefaultStack();
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.LEAKING_SERIALIZER;
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.LEAKING;
    }
    
    public BlockIngredient getBlock() {
        return block;
    }
    
    public FluidIngredient getFluid() {
        return fluid;
    }
    
    public long getAmount() {
        return amount;
    }
    
    public Block getResult() {
        return result;
    }
    
    public static class Serializer implements RecipeSerializer<LeakingRecipe> {
        @Override
        public LeakingRecipe read(Identifier id, JsonObject json) {
            BlockIngredient block = BlockIngredient.fromJson(json.get("block"));
            FluidIngredient fluid = FluidIngredient.fromJson(json.get("fluid"));
            long amount = json.get("amount").getAsLong();
            Block result = Registry.BLOCK.get(new Identifier(json.get("result").getAsString()));
            
            return new LeakingRecipe(id, block, fluid, amount, result);
        }
    
        @Override
        public LeakingRecipe read(Identifier id, PacketByteBuf buf) {
            BlockIngredient block = BlockIngredient.fromPacket(buf);
            FluidIngredient fluid = FluidIngredient.fromPacket(buf);
            long amount = buf.readLong();
            Block result = Registry.BLOCK.get(buf.readIdentifier());
    
            return new LeakingRecipe(id, block, fluid, amount, result);
        }
    
        @Override
        public void write(PacketByteBuf buf, LeakingRecipe recipe) {
            recipe.block.toPacket(buf);
            recipe.fluid.toPacket(buf);
            buf.writeLong(recipe.amount);
            buf.writeIdentifier(Registry.BLOCK.getId(recipe.result));
        }
    
    }
    
    protected static record LeakingRecipeContext(Block block, FluidVariant fluid) implements RecipeContext { }
}
