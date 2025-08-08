package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.TypesData;
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
        this.itemDir = new File(jsonDir, "bauble_item");
    }

    public void typesToJson() throws IOException {
        Iterator<BaubleTypeEx> types = TypesData.iterator();
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
                    TypesData.registerBauble(type);
            }
        } catch (FileNotFoundException e) {
            //Impossible?
            throw new RuntimeException(e);
        }
    }

    public void itemToJson(Object obj) {
        File jsonFile = new File(itemDir, "item.json");

        try {
            FileUtils.write(jsonFile, GSON.toJson(obj), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void jsonToItem() {
/*        File jsonFile = new File(itemDir, "item.json");
        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        try {
            ItemsData.setRegHelper(GSON.fromJson(new FileReader(jsonFile), type));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }*/
    }
}