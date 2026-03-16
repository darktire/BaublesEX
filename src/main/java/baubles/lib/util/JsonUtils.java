package baubles.lib.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        return json.get(name).getAsString();
    }

    public static int getInt(JsonObject json, String name) {
        return json.get(name).getAsInt();
    }

    public static float getFloat(JsonObject json, String name) {
        return json.get(name).getAsFloat();
    }

    public static double getDouble(JsonObject json, String name) {
        return json.get(name).getAsDouble();
    }

    public static boolean getBoolean(JsonObject json, String name) {
        return json.get(name).getAsBoolean();
    }

    public static String getString(JsonObject json, String name, String fallback) {
        return json.has(name) ? json.get(name).getAsString() : fallback;
    }

    public static int getInt(JsonObject json, String name, int fallback) {
        return json.has(name) ? json.get(name).getAsInt() : fallback;
    }

    public static float getFloat(JsonObject json, String name, float fallback) {
        return json.has(name) ? json.get(name).getAsFloat() : fallback;
    }

    public static double getDouble(JsonObject json, String name, double fallback) {
        return json.has(name) ? json.get(name).getAsDouble() : fallback;
    }

    public static boolean getBoolean(JsonObject json, String name, boolean fallback) {
        return json.has(name) ? json.get(name).getAsBoolean() : fallback;
    }

    public static JsonObject getObject(JsonObject json, String name) {
        return json.getAsJsonObject(name);
    }

    public static boolean hasFields(JsonObject json, String... members) {
        if (json == null) {
            return false;
        }
        else {
            return Arrays.stream(members).allMatch(json::has);
        }
    }

    public static void objectStream(JsonReader in, FieldParser parser) throws IOException {
        if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                parser.parse(name);
            }
            in.endObject();
        }
    }

    public static void arrayStream(JsonReader in, Consumer<String> func) throws IOException {
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
        void parse(String key) throws IOException;
    }
}