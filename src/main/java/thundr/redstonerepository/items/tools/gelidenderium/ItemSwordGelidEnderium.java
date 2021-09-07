package thundr.redstonerepository.items.tools.gelidenderium;

import cofh.core.util.helpers.DamageHelper;
import cofh.redstonearsenal.item.tool.ItemSwordFlux;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemSwordGelidEnderium extends ItemSwordFlux {

    public int radius = 8;

    public ItemSwordGelidEnderium(Item.ToolMaterial toolMaterial) {
        super(toolMaterial);
        this.maxEnergy = GelidEnderiumEnergy.maxEnergy;
        this.energyPerUse = GelidEnderiumEnergy.energyPerUse;
        this.energyPerUseCharged = GelidEnderiumEnergy.energyPerUseCharged;
        this.maxTransfer = GelidEnderiumEnergy.maxTransfer;
        this.damage = 15;
        this.damageCharged = 8;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase player) {
        if (stack.getItemDamage() > 0) {
            stack.setItemDamage(0);
        }
        EntityPlayer thePlayer = (EntityPlayer) player;
        if (thePlayer.capabilities.isCreativeMode || this.getEnergyStored(stack) >= this.energyPerUse) {
            AxisAlignedBB bb;
            List<EntityMob> entities;
            int fluxDamage = this.isEmpowered(stack) ? this.damageCharged : 1;
            float potionDamage = 1.0f;
            if (player.isPotionActive(MobEffects.STRENGTH)) {
                potionDamage += (float) player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier() * 1.3f;
            }
            entity.attackEntityFrom(DamageHelper.causePlayerFluxDamage(thePlayer), (float) fluxDamage * potionDamage);
            int toExtract = this.isEmpowered(stack) ? this.energyPerUseCharged : this.energyPerUse;
            this.extractEnergy(stack, toExtract, thePlayer.capabilities.isCreativeMode);
            if (this.isEmpowered(stack) && (entities = new ArrayList(thePlayer.world.getEntitiesWithinAABB(EntityMob.class, bb = new AxisAlignedBB(entity.posX - (double) this.radius, entity.posY - (double) this.radius, entity.posZ - (double) this.radius, entity.posX + (double) this.radius, entity.posY + (double) this.radius, entity.posZ + (double) this.radius)))).size() > 1 && this.getEnergyStored(stack) >= this.energyPerUseCharged * entities.size()) {
                for (EntityMob i : entities) {
                    i.attackEntityFrom(DamageHelper.causePlayerFluxDamage(thePlayer), (float) fluxDamage * potionDamage);
                }
                this.extractEnergy(stack, this.energyPerUseCharged * entities.size(), thePlayer.capabilities.isCreativeMode);
            }
        }
        return true;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("\u00a7aSmashes mobs in a large radius when empowered.");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }
}

