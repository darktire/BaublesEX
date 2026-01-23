package baubles.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JsonUtils {
    public static List<JsonElement> parseArray(JsonObject json, String name) {
        List<JsonElement> list = new ArrayList<>();
        if (json.has(name)) {
            JsonElement element = json.get(name);
            if (element instanceof JsonArray) {
                element.getAsJsonArray().forEach(list::add);
            } else {
                list.add(element);
            }
        }
        return list;
    }

    public static String getString(JsonObject json, String name) {
        JsonElement element = json.get(name);
        return element.getAsString();
    }

    public static int getInt(JsonObject json, String name) {
        JsonElement element = json.get(name);
        return element.getAsInt();
    }

    public static void parseObject(JsonReader in, FieldParser parser) throws IOException {
        if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();
            while (in.hasNext()) {
                String key = in.nextName();
                parser.parse(in, key);
            }
            in.endObject();
        }
    }

    public static void parseArray(JsonReader in, Consumer<String> func) throws IOException {
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            while (in.hasNext()) {
                String value = in.nextString();
                func.accept(value);
            }
            in.endArray();
        }
        else func.accept(in.nextString());
    }

    @FunctionalInterface
    public interface FieldParser {
        void parse(JsonReader in, String key) throws IOException;
    }
}
