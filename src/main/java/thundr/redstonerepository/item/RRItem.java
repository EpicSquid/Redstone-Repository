package thundr.redstonerepository.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import thundr.redstonerepository.RedstoneRepository;

public class RRItem extends Item {

    private final Rarity rarity;

    public RRItem() {
        this(Rarity.RARE);
    }

    public RRItem(Rarity rarity) {
        super(new Properties().tab(RedstoneRepository.tabRedstoneRepository));
        this.rarity = rarity;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.rarity;
    }
}
