package com.loohp.holomobhealth.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.loohp.holomobhealth.HoloMobHealth;

public class EntityTypeUtils {

    private static File file;
    private static JSONObject json;
    private static final JSONParser parser = new JSONParser();
    private static Map<String, String> entry = new HashMap<>();

    private static final List<EntityType> MobTypesList = new ArrayList<>();

    public static List<EntityType> getMobList() {
        return MobTypesList;
    }

    public static void setUpList() {
        MobTypesList.clear();
        for (EntityType each : EntityType.values()) {
            if (each.equals(EntityType.PLAYER) || each.equals(EntityType.ARMOR_STAND) || each.equals(EntityType.UNKNOWN)) {
                continue;
            }
            Set<Class<?>> clazzList = ClassUtils.getAllExtendedOrImplementedTypesRecursively(each.getEntityClass());
            if (clazzList.contains(org.bukkit.entity.LivingEntity.class)) {
                MobTypesList.add(each);
            }
        }
    }

    public static String getMinecraftName(Entity entity) {
        if (file == null) {
            return LegacyEntityTypeUtils.getLegacyMinecraftName(entity);
        }

        EntityType type = entity.getType();
        String path;
        if (HoloMobHealth.version.equals("1.13") || HoloMobHealth.version.contentEquals("1.13.1")) {
            path = "entity.minecraft." + type.name().toLowerCase();
            if (type.equals(EntityType.PIG_ZOMBIE)) {
                path = "entity.minecraft.zombie_pigman";
            }
        } else {
            path = "entity." + type.getKey().getNamespace() + '.' + type.getKey().getKey();
        }

        path = TropicalFishUtils.addTropicalFishType(entity, path);

        if (type.equals(EntityType.VILLAGER)) {
            Villager villager = (Villager) entity;
            path = path + "." + villager.getProfession().toString().toLowerCase();
        }

        return json.containsKey(path) ? json.get(path).toString() : (entry.getOrDefault(path, path));
    }

    public static void reloadLang() {
        if (file == null) {
            LegacyEntityTypeUtils.reloadLegacyLang();
            return;
        }
        try {
            json = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            @SuppressWarnings("unchecked")
            HashMap<String, String> HashMap = new Gson().fromJson(json.toString(), HashMap.class);
            entry = HashMap;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void setupLang() {
        String langVersion;

        switch (HoloMobHealth.version) {
            case "1.15":
                langVersion = "V1_15";
                break;
            case "1.14":
                langVersion = "V1_14";
                break;
            case "1.13.1":
            case "1.13":
                langVersion = "V1_13";
                break;
            default:
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "JSON custom language files are not supported on this version");
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "HoloMobHealth will use legacy EntityType names method instead!");
                file = null;
                LegacyEntityTypeUtils.setupLegacyLang();
                return;
        }

        if (!HoloMobHealth.plugin.getDataFolder().exists()) {
            HoloMobHealth.plugin.getDataFolder().mkdir();
        }

        File langFolder = new File(HoloMobHealth.plugin.getDataFolder().getAbsolutePath() + "/lang");
        if (!langFolder.exists()) {
            langFolder.mkdir();
        }

        String resourceName = "/lang/" + langVersion + ".json";

        File langFile = new File(langFolder.getAbsolutePath() + "/" + langVersion + ".json");
        if (!langFile.exists()) {
            try (InputStream in = HoloMobHealth.plugin.getClass().getResourceAsStream(resourceName)) {
                Files.copy(in, langFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        file = langFile;
        reloadLang();
    }

}
