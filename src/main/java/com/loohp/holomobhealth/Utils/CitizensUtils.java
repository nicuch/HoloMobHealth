package com.loohp.holomobhealth.Utils;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Entity;

public class CitizensUtils {

    public static boolean isNPC(Entity entity) {
        return CitizensAPI.getNPCRegistry().isNPC(entity);
    }

}
