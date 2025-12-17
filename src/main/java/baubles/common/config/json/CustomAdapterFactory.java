package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.IBaubleKey;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CustomAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        ParameterizedType pType = (ParameterizedType) type.getType();
        Type actualArg = pType.getActualTypeArguments()[0];
        if (actualArg == BaubleTypeEx.class) {
            @SuppressWarnings("unchecked")
            TypeAdapter<T> typeDataAdapter = (TypeAdapter<T>) new TypeDataAdapter();
            return typeDataAdapter;
        }
        else if (actualArg == IBaubleKey.class) {
            @SuppressWarnings("unchecked")
            TypeAdapter<T> itemDataAdapter = (TypeAdapter<T>) new ItemDataAdapter();
            return itemDataAdapter;
        }
        return null;
    }
}
