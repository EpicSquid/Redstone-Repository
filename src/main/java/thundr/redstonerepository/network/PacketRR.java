package thundr.redstonerepository.network;

import cofh.core.network.PacketBase;
import cofh.core.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.gui.ContainerFeeder;
import thundr.redstonerepository.items.ItemFeeder;

public class PacketRR extends PacketBase {

    public static void sendAddFood(int hunger, int stackSizeDec) {
        PacketHandler.sendToServer(PacketRR.getPacket(PacketTypes.ADD_FOOD).addInt(hunger).addInt(stackSizeDec));
    }

    public static PacketBase getPacket(PacketTypes theType) {
        return new PacketRR().addByte(theType.ordinal());
    }

    public void handlePacket(EntityPlayer player, boolean isServer) {
        try {
            byte type = this.getByte();
            switch (PacketTypes.values()[type]) {
                case ADD_FOOD: {
                    ItemStack stack;
                    if (player.openContainer instanceof ContainerFeeder && (stack = ((ContainerFeeder) player.openContainer).getContainerStack()).getItem() instanceof ItemFeeder) {
                        ItemFeeder feeder = (ItemFeeder) stack.getItem();
                        int hungerAdded = this.getInt();
                        feeder.receiveHungerPoints(stack, hungerAdded, false);
                        player.openContainer.inventorySlots.get(player.openContainer.inventorySlots.size() - 1).decrStackSize(this.getInt());
                    }
                    return;
                }
            }
            RedstoneRepository.LOGGER.error("Unknown Packet Type " + type);
        } catch (Exception e) {
            RedstoneRepository.LOGGER.error("Packet malformed!");
            e.printStackTrace();
        }
    }

    public enum PacketTypes {
        ADD_FOOD;


        PacketTypes() {
        }
    }

}

