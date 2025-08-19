package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        ParameterizedType pType = (ParameterizedType) type.getType();
        Type actualArg = pType.getActualTypeArguments()[0];
        if (actualArg == BaubleTypeEx.class) {
            return (TypeAdapter<T>) new TypeDataAdapter();
        }
        else if (actualArg == BaublesWrapper.class) {
            return (TypeAdapter<T>) new ItemDataAdapter();
        }
        return null;
    }
}
