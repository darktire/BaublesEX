package baubles.common.config.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public enum Category {
    ITEM_DATA(ItemDataAdapter.INSTANCE),
    ITEM(ItemHelper.INSTANCE),
    TYPE_DATA(TypeDataAdapter.INSTANCE),
    TYPE(TypeHelper.INSTANCE),
    MODULE_DATA(ModuleDataAdapter.INSTANCE),
    MODULE(ModuleHelper.INSTANCE);

    final Type type;
    final Object adapter;

    Category(Object adapter) {
        this.adapter = adapter;
        this.type = getType(adapter);
    }

    private static Type getType(Object adapter) {
        Type type = findSuperclassType(adapter.getClass());
        if (type == null) type = findInterfaceType(adapter.getClass());
        return type;
    }

    private static Type findSuperclassType(Class<?> clazz) {
        if (clazz == Object.class) return null;

        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[0];
        }

        return findSuperclassType((Class<?>) type);
    }

    private static Type findInterfaceType(Class<?> clazz) {
        if (clazz == Object.class) return null;

        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                return ((ParameterizedType) type).getActualTypeArguments()[0];
            } else if (type instanceof Class<?>) {
                return findInterfaceType((Class<?>) type);
            }
        }

        return findInterfaceType(clazz.getSuperclass());
    }
}
