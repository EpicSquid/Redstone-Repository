package thundr.redstonerepository.network;

import cofh.core.network.PacketBase;
import cofh.core.network.PacketHandler;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Logger;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.gui.ContainerFeeder;
import thundr.redstonerepository.items.ItemFeeder;

public class PacketRR extends PacketBase {
	
    public void handlePacket(EntityPlayer player, boolean isServer) {
        try {
            byte type = this.getByte();
            switch (PacketTypes.values()[type]) {
                case ADD_FOOD: {
                    ItemStack stack;
                    if (player.openContainer instanceof ContainerFeeder && (stack = ((ContainerFeeder)player.openContainer).getContainerStack()).getItem() instanceof ItemFeeder) {
                        ItemFeeder feeder = (ItemFeeder)stack.getItem();
                        int hungerAdded = this.getInt();
                        feeder.receiveHungerPoints(stack, hungerAdded, false);
                        ((Slot)player.openContainer.inventorySlots.get(player.openContainer.inventorySlots.size() - 1)).decrStackSize(this.getInt());
                    }
                    return;
                }
            }
            RedstoneRepository.LOG.error("Unknown Packet Type " + type);
        }
        catch (Exception e) {
            RedstoneRepository.LOG.error("Packet malformed!");
            e.printStackTrace();
        }
    }

    public static void sendAddFood(int hunger, int stackSizeDec) {
        PacketHandler.sendToServer((PacketBase)PacketRR.getPacket(PacketTypes.ADD_FOOD).addInt(hunger).addInt(stackSizeDec));
    }

    public static PacketBase getPacket(PacketTypes theType) {
        return new PacketRR().addByte(theType.ordinal());
    }

    public static enum PacketTypes {
        ADD_FOOD;
        

        private PacketTypes() {
        }
    }

}

