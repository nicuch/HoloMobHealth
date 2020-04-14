package com.loohp.holomobhealth.Utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TropicalFish;

public class TropicalFishUtils {

    public static String addTropicalFishType(Entity entity, String toAppend) {
        String path = toAppend;
        EntityType type = entity.getType();
        if (type.equals(EntityType.TROPICAL_FISH)) {
            TropicalFish fish = (TropicalFish) entity;
            path = new StringBuilder().append(path).append(".type.").append(fish.getPattern().toString().toLowerCase()).toString();
        }
        return path;
    }
}
