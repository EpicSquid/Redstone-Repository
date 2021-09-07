package thundr.redstonerepository.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int FEEDER_ID = 0;

    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case 0: {
                return new GuiFeeder(player.inventory, new ContainerFeeder(player.getHeldItemMainhand(), player.inventory));
            }
        }
        return null;
    }

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case 0: {
                return new ContainerFeeder(player.getHeldItemMainhand(), player.inventory);
            }
        }
        return null;
    }
}

