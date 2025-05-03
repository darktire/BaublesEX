package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static baubles.api.BaublesRegister.getBaubles;
import static baubles.common.Baubles.config;


public class JsonHelper {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File jsonDir;
    private final File typeDir;
    private final File itemDir;

    public JsonHelper() {
        this.jsonDir = new File(config.modDir, "baubles");
        this.typeDir = new File(jsonDir, "bauble_type");
        this.itemDir = new File(jsonDir, "bauble_type");
    }

    public void typesToJson() throws IOException {

        for (BaubleTypeEx type: getBaubles().values()) {
            typesToJson(typeDir, type);
        }
    }

    public void typesToJson(File dir, BaubleTypeEx type) throws IOException {
        File jsonFile = new File(dir, type.getTypeName() + ".json");
        FileUtils.write(jsonFile, GSON.toJson(type), StandardCharsets.UTF_8);
    }

    public void jsonToType() throws FileNotFoundException {
        Collection<File> fileList = FileUtils.listFiles(typeDir, new String[]{"json"}, true);
        if (fileList.isEmpty()) throw new FileNotFoundException();
        for (File jsonFile: fileList) {
            BaubleTypeEx type = GSON.fromJson(new FileReader(jsonFile),BaubleTypeEx.class);
            getBaubles().put(type.getTypeName(), type);
        }
    }

}