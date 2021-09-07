package thundr.redstonerepository.gui.element;

import cofh.core.gui.GuiContainerCore;
import cofh.core.gui.element.ElementBase;
import cofh.core.util.helpers.MathHelper;
import cofh.core.util.helpers.RenderHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ElementEnergyItem extends ElementBase {
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("redstonerepository:textures/gui/energy.png");
    public static final int DEFAULT_SCALE = 56;
    protected IEnergyContainerItem storage;
    protected ItemStack stack;
    protected boolean isInfinite;
    protected boolean alwaysShowMinimum = false;

    public ElementEnergyItem(GuiContainerCore gui, int posX, int posY, ItemStack stack) {
        super(gui, posX, posY);
        this.storage = (IEnergyContainerItem)stack.getItem();
        this.stack = stack;
        this.texture = DEFAULT_TEXTURE;
        this.sizeX = 9;
        this.sizeY = 56;
        this.texW = 32;
        this.texH = 64;
    }

    public ElementEnergyItem setAlwaysShow(boolean show) {
        this.alwaysShowMinimum = show;
        return this;
    }

    public ElementEnergyItem setInfinite(boolean infinite) {
        this.isInfinite = infinite;
        return this;
    }

    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        int amount = this.getScaled();
        RenderHelper.bindTexture((ResourceLocation)this.texture);
        this.drawTexturedModalRect(this.posX, this.posY, 0, 0, this.sizeX, this.sizeY);
        this.drawTexturedModalRect(this.posX, this.posY + 56 - amount, 17, 56 - amount, this.sizeX, amount);
    }

    public void drawForeground(int mouseX, int mouseY) {
    }

    public void addTooltip(List<String> list) {
        if (this.isInfinite) {
            list.add("Infinite RF");
        } else {
            list.add(StringHelper.formatNumber((long)this.storage.getEnergyStored(this.stack)) + " / " + StringHelper.formatNumber((long)this.storage.getMaxEnergyStored(this.stack)) + " RF");
        }
    }

    protected int getScaled() {
        if (this.storage.getMaxEnergyStored(this.stack) <= 0) {
            return this.sizeY;
        }
        long fraction = (long)this.storage.getEnergyStored(this.stack) * (long)this.sizeY / (long)this.storage.getMaxEnergyStored(this.stack);
        return this.alwaysShowMinimum && this.storage.getEnergyStored(this.stack) > 0 ? Math.max(1, MathHelper.round((double)fraction)) : MathHelper.round((double)fraction);
    }
}

