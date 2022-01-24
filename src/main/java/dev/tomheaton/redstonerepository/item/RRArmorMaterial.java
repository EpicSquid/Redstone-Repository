package dev.tomheaton.redstonerepository.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum RRArmorMaterial implements IArmorMaterial {
    GELID_ENDERIUM("gelid_enderium", 0, new int[] {0, 0, 0, 0}, 0, Items.IRON_INGOT, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f),
    ;

    private final String name;
    private final int durability;
    private final int[] defense;
    private final int enchantability;
    private final Item repairItem;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private static final int[] max_damage_array = new int[] {13, 15, 16, 11};

    RRArmorMaterial(String name, int durability, int[] defense, int enchantability, Item repairItem, SoundEvent equipSound, float toughness, float knockbackResistance) {
        this.name = name;
        this.durability = durability;
        this.defense = defense;
        this.enchantability = enchantability;
        this.repairItem = repairItem;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slot) {
        return max_damage_array[slot.getIndex()] * durability;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slot) {
        return defense[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(repairItem);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
