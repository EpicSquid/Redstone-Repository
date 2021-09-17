package thundr.redstonerepository.entity.projectile;

import cofh.core.init.CoreProps;
import cofh.redstonearsenal.entity.projectile.EntityArrowFlux;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import thundr.redstonerepository.RedstoneRepository;

public class EntityArrowGelid extends EntityArrowFlux {

    public EntityArrowGelid(World world) {
        super(world);
    }

    public static void initialize(int id) {
        EntityRegistry.registerModEntity(new ResourceLocation(RedstoneRepository.MODID, "gelid_arrow"), EntityArrowGelid.class, "redstonerepository.gelid_arrow", id, RedstoneRepository.INSTANCE, CoreProps.ENTITY_TRACKING_DISTANCE, 1, true);
    }
}
