package thundr.redstonerepository.gui;

import cofh.core.gui.container.ContainerInventoryItem;
import cofh.core.gui.container.InventoryContainerItemWrapper;
import cofh.core.gui.slot.ISlotValidator;
import cofh.core.gui.slot.SlotLocked;
import cofh.core.gui.slot.SlotValidated;
import cofh.core.util.helpers.StringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ContainerFeeder extends ContainerInventoryItem implements ISlotValidator {
	
    static final String NAME = "item.redstonerepository.bauble.feeder.name";

    public ContainerFeeder(ItemStack stack, InventoryPlayer inventoryPlayer) {
        super(stack, inventoryPlayer);
        this.bindPlayerInventory(inventoryPlayer);
        this.addSlotToContainer((Slot)new SlotValidated((ISlotValidator)this, (IInventory)this.containerWrapper, 0, 80, 26));
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemFood;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        int i;
        int xOffset = this.getPlayerInventoryHorizontalOffset();
        int yOffset = this.getPlayerInventoryVerticalOffset();
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot((IInventory)inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            if (i == inventoryPlayer.currentItem) {
                this.addSlotToContainer((Slot)new SlotLocked((IInventory)inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
                continue;
            }
            this.addSlotToContainer(new Slot((IInventory)inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
        }
    }

    public String getInventoryName() {
        return this.containerWrapper.func_145818_k_() ? this.containerWrapper.func_70005_c_() : StringHelper.localize((String)"item.redstonerepository.bauble.feeder.name");
    }

    protected int getPlayerInventoryVerticalOffset() {
        return 66;
    }

    protected int getPlayerInventoryHorizontalOffset() {
        return 8;
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return true;
	}
}

