package tomheaton.redstonerepository.gui.element;

import cofh.core.gui.GuiContainerCore;
import cofh.core.gui.element.ElementBase;
import cofh.core.util.helpers.MathHelper;
import cofh.core.util.helpers.RenderHelper;
import cofh.core.util.helpers.StringHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tomheaton.redstonerepository.api.IHungerStorageItem;

import java.util.List;

public class ElementHungerPoints extends ElementBase {

    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("redstonerepository:textures/gui/hunger.png");
    public static final int DEFAULT_SCALE = 56;
    protected IHungerStorageItem storage;
    protected ItemStack stack;

    public ElementHungerPoints(GuiContainerCore gui, int posX, int posY, ItemStack stack) {
        super(gui, posX, posY);
        this.storage = (IHungerStorageItem) stack.getItem();
        this.stack = stack;
        this.texture = DEFAULT_TEXTURE;
        this.sizeX = 9;
        this.sizeY = 56;
        this.texW = 32;
        this.texH = 64;
    }

    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        int amount = this.getScaled();
        RenderHelper.bindTexture(this.texture);
        this.drawTexturedModalRect(this.posX, this.posY, 0, 0, this.sizeX, this.sizeY);
        this.drawTexturedModalRect(this.posX, this.posY + 56 - amount, 17, 56 - amount, this.sizeX, amount);
    }

    public void drawForeground(int mouseX, int mouseY) {
    }

    public void addTooltip(List<String> list) {
        list.add(StringHelper.formatNumber(this.storage.getHungerPoints(this.stack)) + " / " + StringHelper.formatNumber(this.storage.getMaxHungerPoints(this.stack)) + " Hunger Points");
    }

    protected int getScaled() {
        if (this.storage.getMaxHungerPoints(this.stack) <= 0) {
            return this.sizeY;
        }
        long fraction = (long) this.storage.getHungerPoints(this.stack) * (long) this.sizeY / (long) this.storage.getMaxHungerPoints(this.stack);
        return this.storage.getHungerPoints(this.stack) > 0 ? Math.max(1, MathHelper.round((double) fraction)) : MathHelper.round((double) fraction);
    }
}

