package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.common.Baubles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;


public class JsonHelper {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File jsonDir;
    private final File typeDir;
    private final File itemDir;

    public JsonHelper() {
        this.jsonDir = new File(Baubles.config.modDir, "baubles");
        this.typeDir = new File(jsonDir, "bauble_type");
        this.itemDir = new File(jsonDir, "bauble_type");
    }

    public void typesToJson() throws IOException {
        Iterator<BaubleTypeEx> types = Baubles.baubles.iterator();
        while (types.hasNext()) {
            BaubleTypeEx type = types.next();
            File jsonFile = new File(typeDir, type.getTypeName() + ".json");

            FileUtils.write(jsonFile, GSON.toJson(type), StandardCharsets.UTF_8);
        }
    }

    public void jsonToType() throws FileNotFoundException {
        Collection<File> files = FileUtils.listFiles(typeDir, new String[]{"json"}, true);
        if (files.isEmpty()) throw new FileNotFoundException();
        try {
            for (File jsonFile : files) {
                    BaubleTypeEx type = GSON.fromJson(new FileReader(jsonFile), BaubleTypeEx.class);
                    Baubles.baubles.registerBauble(type.getTypeName(), type);
            }
        } catch (FileNotFoundException e) {
            //Impossible?
            throw new RuntimeException(e);
        }
    }

}