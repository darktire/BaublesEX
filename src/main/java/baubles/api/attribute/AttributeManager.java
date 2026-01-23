package baubles.api.attribute;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypeData;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

import java.util.*;
import java.util.stream.Collectors;

public class AttributeManager {
    private static final Map<BaubleTypeEx, IAttribute> PLAYER_BAUBLES = new HashMap<>();
    private static final Set<EntityLivingBase> LISTENER = Collections.newSetFromMap(new WeakHashMap<>());

    public static void loadAttributes() {
        Set<BaubleTypeEx> get = new HashSet<>(TypeData.getOrder());
        Set<BaubleTypeEx> set = PLAYER_BAUBLES.keySet();

        for (BaubleTypeEx type : set) {
            if (get.contains(type)) {
                ((NonNegativeAttribute) PLAYER_BAUBLES.get(type)).setDefaultValue(type.getAmount());
            }
            else {
                ((NonNegativeAttribute) PLAYER_BAUBLES.get(type)).setDefaultValue(0);
            }
        }
        LISTENER.forEach(AttributeManager::correct);

        Set<BaubleTypeEx> extra = Sets.difference(get, set);
        for (BaubleTypeEx type : extra) {
            PLAYER_BAUBLES.put(type, generate(type));
        }
        if (extra.isEmpty()) return;
        for (EntityLivingBase entity : LISTENER) {
            attachAttributes(entity, BaublesApi.getBaublesHandler(entity));
        }
    }

    public static IAttribute getAttribute(BaubleTypeEx type) {
        return PLAYER_BAUBLES.get(type);
    }
    public static Map<BaubleTypeEx, AdvancedInstance> getModified(EntityLivingBase entity) {
        AbstractAttributeMap map = entity.getAttributeMap();
        return PLAYER_BAUBLES.keySet().stream()
                .map(attribute -> new AbstractMap.SimpleEntry<>(
                        attribute,
                        getInstance(map, attribute)
                ))
                .filter(wrapper -> {
                    AdvancedInstance instance = wrapper.getValue();
                    return instance instanceof AdvancedInstance && instance.isModified;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
    public static List<BaubleTypeEx> computeSlots(EntityLivingBase entity) {
        AbstractAttributeMap map = entity.getAttributeMap();
        return TypeData.getOrder().stream()
                .flatMap(type -> Collections.nCopies(getValue(map, type), type).stream())
                .collect(Collectors.toList());
    }

    private static IAttribute generate(BaubleTypeEx type) {
        return new NonNegativeAttribute(null, type.getTranslateKey(), type.getAmount());
    }

    public static void attachAttributes(EntityLivingBase entity, IBaublesItemHandler handler) {
        AbstractAttributeMap map = entity.getAttributeMap();
        for (IAttribute attribute : PLAYER_BAUBLES.values()) {
            IAttributeInstance instance = new AdvancedInstance(map, attribute).addListener(handler);
//            modifiers.forEach(instance::applyModifier);
            map.attributesByName.put(attribute.getName(), instance);
            map.attributes.put(attribute, instance);
        }
    }

    public static void correct(EntityLivingBase entity) {
        AbstractAttributeMap map = entity.getAttributeMap();
        for (IAttribute attribute : PLAYER_BAUBLES.values()) {
            ((AdvancedInstance) map.getAttributeInstance(attribute)).correct();
        }
    }

    public static int getValue(AbstractAttributeMap map, BaubleTypeEx type) {
        if (PLAYER_BAUBLES.containsKey(type)) {
            return (int) map.attributes.get(PLAYER_BAUBLES.get(type)).getAttributeValue();
        }
        return 0;
    }

    public static AdvancedInstance getInstance(AbstractAttributeMap map, BaubleTypeEx type) {
        return (AdvancedInstance) map.getAttributeInstance(PLAYER_BAUBLES.get(type));
    }
}
