package me.refracdevelopment.simpletags.manager.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.chat.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class PlayerMapper {

    private final String directory;
    private final JsonParser parser;

    public PlayerMapper(String directory) {
        this.parser = new JsonParser();
        this.directory = directory;
        File folder = new File(directory);
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!new File(directory).isDirectory()) {
            throw new IllegalArgumentException("The directory path is not an directory");
        }
    }

    public void loadPlayerFile(UUID uuid) {
        final File playerFile = new File(this.directory + File.separator + uuid.toString() + ".json");
        if (!playerFile.exists()) {
            this.generateDefaultFile(uuid, SimpleTags.getInstance().getProfileManager().getProfile(uuid).getData().getName());
        }
        SimpleTags.getInstance().getProfileManager().getProfile(uuid).getData().setTag(getTag(uuid));
        SimpleTags.getInstance().getProfileManager().getProfile(uuid).getData().setTagPrefix(getTagPrefix(uuid));
    }

    public void savePlayer(UUID uuid, String name, String tag, String tagPrefix) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", uuid.toString());
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("tag", tag);
        jsonObject.addProperty("tagPrefix", tagPrefix);
        this.saveFile(uuid, jsonObject);
    }

    private void generateDefaultFile(UUID uuid, String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", uuid.toString());
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("tag", "");
        jsonObject.addProperty("tagPrefix", "");
        this.saveFile(uuid, jsonObject);
    }

    private void saveFile(final UUID uuid, final JsonObject jsonObject) {
        final File file = new File(this.directory + File.separator + uuid.toString() + ".json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            Color.log("&cError while trying to create config file for user: " + uuid);
            e.printStackTrace();
        }
        try {
            final FileWriter myWriter = new FileWriter(file);
            myWriter.write(jsonObject.toString());
            myWriter.close();
        } catch (IOException e) {
            Color.log("&cError while trying to create a printer for file for user: " + uuid);
            e.printStackTrace();
        }
    }

    public String getTag(UUID uuid) {
        try {
            FileReader reader = new FileReader(this.directory + File.separator + uuid + ".json");
            JsonObject obj = (JsonObject) this.parser.parse(reader);
            reader.close();
            return obj.get("tag").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTagPrefix(UUID uuid) {
        try {
            FileReader reader = new FileReader(this.directory + File.separator + uuid + ".json");
            JsonObject obj = (JsonObject) this.parser.parse(reader);
            reader.close();
            return obj.get("tagPrefix").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}