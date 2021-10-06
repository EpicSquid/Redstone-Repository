package thundr.redstonerepository.item.util;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cofh.core.item.ItemCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import thundr.redstonerepository.RedstoneRepository;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemRingBase extends ItemCore implements IBauble {
    public ItemRingBase(){
        super(RedstoneRepository.MODID);
        setMaxStackSize(1);
    }

    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack){
        return BaubleType.RING;
    }

}