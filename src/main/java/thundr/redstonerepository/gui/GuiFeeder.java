package thundr.redstonerepository.gui;

import cofh.core.gui.GuiContainerCore;
import cofh.core.gui.element.ElementButton;
import cofh.core.gui.element.tab.TabInfo;
import cofh.core.util.helpers.StringHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.gui.element.ElementEnergyItem;
import thundr.redstonerepository.gui.element.ElementHungerPoints;
import thundr.redstonerepository.item.util.ItemFeeder;
import thundr.redstonerepository.network.PacketRR;
import thundr.redstonerepository.util.HungerHelper;

import java.io.IOException;

public class GuiFeeder extends GuiContainerCore {

    ElementButton addFood;
    ElementEnergyItem energy;
    ElementHungerPoints hungerPoints;
    public static final  ResourceLocation TEXTURE = new ResourceLocation(RedstoneRepository.MODID, "textures/gui/feeder.png");
    //String PATH_BUTTON = new ResourceLocation(RedstoneRepository.MODID, "textures/gui/feeder.png").toString();
    ItemStack feederStack;
    ItemFeeder baseFeeder;
    ContainerFeeder containerFeeder;

    public GuiFeeder(InventoryPlayer inventoryPlayer, ContainerFeeder containerFeeder) {
        super(containerFeeder, TEXTURE);
        this.name = containerFeeder.getInventoryName();
        this.xSize = 176;
        this.ySize = 148;
        this.feederStack = containerFeeder.getContainerStack();
        this.baseFeeder = (ItemFeeder) containerFeeder.getContainerStack().getItem();
        this.containerFeeder = containerFeeder;
        this.generateInfo("tab.redstonerepository.feeder");
    }

    public void initGui() {
        super.initGui();
        if (!this.myInfo.isEmpty()) {
            this.addTab(new TabInfo(this, this.myInfo));
        }
        //this.addFood = new ElementButton(this, 101, 26, "AddFood", 177, 64, 177, 80, 177, 96, 16, 16, this.PATH_BUTTON);
        this.addFood = new ElementButton(this, 101, 26, "AddFood", 177, 64, 177, 80, 177, 96, 16, 16, this.texture.toString());
        this.energy = new ElementEnergyItem(this, 151, 6, ((ContainerFeeder) this.inventorySlots).getContainerStack());
        this.hungerPoints = new ElementHungerPoints(this, 160, 6, ((ContainerFeeder) this.inventorySlots).getContainerStack());
        this.addElement(this.addFood);
        this.addElement(this.energy);
        this.addElement(this.hungerPoints);
        Keyboard.enableRepeatEvents(true);
    }

    protected void mouseClicked(int mX, int mY, int mButton) throws IOException {
        super.mouseClicked(mX, mY, mButton);
    }

    protected void updateButtons() {
        ItemStack tmpStack = this.containerFeeder.inventorySlots.get(this.containerFeeder.inventorySlots.size() - 1).getStack().copy();
        if (tmpStack.isEmpty() || HungerHelper.findHungerValueSingle(tmpStack) > this.baseFeeder.getMaxHungerPoints(this.feederStack) - this.baseFeeder.getHungerPoints(this.feederStack)) {
            this.addFood.setDisabled();
        } else {
            this.addFood.setEnabled(true);
        }
    }

    protected void drawGuiContainerForegroundLayer(int x, int y) {
        ItemStack tmpStack = this.containerFeeder.inventorySlots.get(this.containerFeeder.inventorySlots.size() - 1).getStack().copy();
        if (this.drawTitle & this.name != null) {
            this.fontRenderer.drawString(StringHelper.localize(this.name), 6, 6, 4210752);
        }
        if (this.drawInventory) {
            this.fontRenderer.drawString(StringHelper.localize("container.inventory"), 8, this.ySize - 96 + 3, 4210752);
        }
        if (!tmpStack.isEmpty()) {
            this.fontRenderer.drawString(StringHelper.localize("gui.redstonerepository.food.1") + " " + HungerHelper.findHungerValueSingle(tmpStack), 65, 48, 47104);
            this.fontRenderer.drawString(StringHelper.localize("gui.redstonerepository.food.2") + " " + HungerHelper.findHungerValues(tmpStack), 65, 56, 47104);
        }
        this.drawElements(0.0f, true);
        this.drawTabs(0.0f, true);
        this.updateButtons();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        // TODO: this!
    }

    public void handleElementButtonClick(String button, int mouseButton) {
        ItemStack tmpStack = this.containerFeeder.inventorySlots.get(this.containerFeeder.inventorySlots.size() - 1).getStack().copy();
        if (mouseButton == 0) {
            tmpStack.setCount(1);
            int hunger = HungerHelper.findHungerValues(tmpStack);
            int hungerToUse = this.baseFeeder.receiveHungerPoints(this.feederStack, hunger, true);
            if (hunger == hungerToUse) {
                this.baseFeeder.receiveHungerPoints(this.feederStack, hunger, false);
                PacketRR.sendAddFood(hunger, tmpStack.getCount());
            }
        } else if (mouseButton == 1) {
            int hungerTotal = HungerHelper.findHungerValues(tmpStack);
            int hungerPerItem = HungerHelper.findHungerValueSingle(tmpStack);
            int hungerToUse = this.baseFeeder.receiveHungerPoints(this.feederStack, hungerTotal, true);
            if (hungerTotal == hungerToUse) {
                this.baseFeeder.receiveHungerPoints(this.feederStack, hungerTotal, false);
                PacketRR.sendAddFood(hungerTotal, tmpStack.getCount());
            } else if (hungerToUse >= hungerPerItem) {
                int stacksToDelete = hungerToUse / hungerPerItem;
                this.baseFeeder.receiveHungerPoints(this.feederStack, hungerPerItem * stacksToDelete, false);
                PacketRR.sendAddFood(hungerPerItem * stacksToDelete, stacksToDelete);
            }
        }
        GuiFeeder.playClickSound(0.7f);
    }
}

