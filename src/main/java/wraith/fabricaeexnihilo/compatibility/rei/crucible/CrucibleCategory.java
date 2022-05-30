package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class CrucibleCategory implements DisplayCategory<CrucibleDisplay> {

    public static final Identifier GLYPH = id("textures/gui/rei/glyphs.png");
    public static final int GLYPH_HEIGHT = 16;
    public static final int GLYPH_U = 0;
    public static final int GLYPH_V = 0;
    public static final int GLYPH_WIDTH = 16;
    public static final int MARGIN = 6;
    public static final int HEIGHT = MARGIN * 2 + 16;
    public static final int INPUT_X = MARGIN;
    public static final int GLYPH_X = INPUT_X + 18;
    public static final int OUTPUT_X = GLYPH_X + 18;
    public static final int INPUT_Y = MARGIN;
    public static final int GLYPH_Y = MARGIN;
    public static final int OUTPUT_Y = MARGIN;
    public static final int WIDTH = MARGIN * 2 + 18 * 9;
    private final CategoryIdentifier<? extends CrucibleDisplay> crucible;
    private final ItemStack icon;
    private final String name;

    public CrucibleCategory(CategoryIdentifier<? extends CrucibleDisplay> crucible, ItemStack icon, String name) {
        this.crucible = crucible;
        this.icon = icon;
        this.name = name;
    }

    @Override
    public CategoryIdentifier<? extends CrucibleDisplay> getCategoryIdentifier() {
        return crucible;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(CrucibleDisplay display) {
        return WIDTH;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(icon);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText(this.name);
    }

    @Override
    public List<Widget> setupDisplay(CrucibleDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        var inputs = display.getInputEntries().get(0);
        var outputs = display.getOutputEntries().get(0);
        var output = display.getAmount();

        // Input
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + INPUT_X, bounds.getMinY() + INPUT_Y)).entries(inputs));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + GLYPH_X, bounds.getMinY() + GLYPH_Y, GLYPH_WIDTH, GLYPH_HEIGHT, GLYPH, GLYPH_U, GLYPH_V));

        // Output
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + OUTPUT_X, bounds.getMinY() + OUTPUT_Y)).entries(outputs));

        // Amount Text Value
        var text = Widgets.createLabel(new Point(0, 0), new LiteralText(String.valueOf(output)));
        text.setPoint(new Point(bounds.getMinX() + WIDTH - MARGIN - text.getBounds().getMaxX(), bounds.getMinY() + MARGIN + 18 - text.getBounds().getMaxY() - text.getBounds().getMaxY() / 2));

        widgets.add(text);

        return widgets;
    }

}

