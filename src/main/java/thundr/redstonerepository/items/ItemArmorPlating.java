package thundr.redstonerepository.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thundr.redstonerepository.RedstoneRepository;

import java.util.List;

public class ItemArmorPlating extends Item {

    private static final String[] names = new String[]{"enderium", "power.empty", "power.full"};

    public ItemArmorPlating() {
        this.setUnlocalizedName("redstonerepository.plating");
        this.setCreativeTab(RedstoneRepository.tabCommon);
        this.setHasSubtypes(true);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
    }

    @SideOnly(value = Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(this, 1, 0));
    }

    public EnumRarity getRarity(ItemStack stack) {
        if (stack.getItemDamage() == 0) {
            return EnumRarity.RARE;
        }
        if (stack.getItemDamage() == 1) {
            return EnumRarity.UNCOMMON;
        }
        if (stack.getItemDamage() == 2) {
            return EnumRarity.EPIC;
        }
        return EnumRarity.COMMON;
    }
}

