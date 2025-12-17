package baubles.common.config.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.function.Consumer;

public abstract class CustomAdapter<T> extends TypeAdapter<T> {

    @FunctionalInterface
    public interface FieldParser {
        void parse(JsonReader in, String key) throws IOException;
    }

    protected void parseObject(JsonReader in, FieldParser parser) throws IOException {
        in.beginObject();
        while (in.hasNext()) {
            String key = in.nextName();
            parser.parse(in, key);
        }
        in.endObject();
    }

    protected void parseArray(JsonReader in, Consumer<String> func) throws IOException {
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
}
