package thundr.redstonerepository.item.util;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cofh.core.key.KeyBindingItemMultiMode;
import cofh.core.util.helpers.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.init.RedstoneRepositoryEquipment;
import thundr.redstonerepository.item.ItemCoreRF;

import javax.annotation.Nullable;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemRingMining extends ItemCoreRF implements IBauble {

    public ItemRingMining() {
        super(RedstoneRepository.MODID);
        maxEnergy = 4000000;
        maxTransfer = 5000;
        energyPerUse = 100;
        setMaxDamage(0);
        setNoRepair();
        setMaxStackSize(1);
        setUnlocalizedName("redstonerepository.util.ring.mining");
        setCreativeTab(RedstoneRepository.tabCommon);
        addPropertyOverride(new ResourceLocation("active"), (stack, world, entity) -> this.getMode(stack) == MODE.ENABLED.getValue() ? 1.0f : 0.0f);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.redstonerepository.ring.mining.title"));
        if (this.getMode(stack) == ItemRingMining.MODE.ENABLED.getValue()) {
            tooltip.add(StringHelper.localizeFormat("info.redstonerepository.tooltip.active", StringHelper.BRIGHT_GREEN, StringHelper.END, StringHelper.getKeyName(KeyBindingItemMultiMode.INSTANCE.getKey())));
        } else {
            tooltip.add(StringHelper.localizeFormat("info.redstonerepository.tooltip.disabled", StringHelper.LIGHT_RED, StringHelper.END, StringHelper.getKeyName(KeyBindingItemMultiMode.INSTANCE.getKey())));
        }
        if(!RedstoneRepositoryEquipment.EquipmentInit.enable[3]){
            tooltip.add(StringHelper.RED + "Baubles not loaded: Recipe disabled.");
        }
        tooltip.add(StringHelper.localize("info.cofh.charge") + ": " + StringHelper.getScaledNumber(getEnergyStored(stack)) + " / " + StringHelper.getScaledNumber(getMaxEnergyStored(stack)) + " RF");
        tooltip.add(StringHelper.localize("info.cofh.send") + "/" + StringHelper.localize("info.cofh.receive") + ": " + StringHelper.formatNumber(maxTransfer) + "/" + StringHelper.formatNumber(maxTransfer) + " RF/t");
    }

    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    public void onModeChange(EntityPlayer player, ItemStack stack) {
        if (this.getMode(stack) == MODE.ENABLED.getValue()) {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.8f);
        } else {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.5f);
        }
    }

    public enum MODE {
        DISABLED(0),
        ENABLED(1);

        private final int value;

        MODE(int newValue) {
            this.value = newValue;
        }

        public int getValue() {
            return this.value;
        }
    }
}
