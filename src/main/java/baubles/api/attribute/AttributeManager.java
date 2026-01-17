package baubles.api.attribute;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;

import java.util.*;
import java.util.stream.Collectors;

public class AttributeManager {
    private static final HashMap<BaubleTypeEx, IAttribute> PLAYER_BAUBLES = new HashMap<>();
    private static final Set<EntityLivingBase> LISTENER = Collections.newSetFromMap(new WeakHashMap<>());

    public static void loadAttributes() {
        Set<BaubleTypeEx> get = new HashSet<>(TypesData.getOrder());
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
    public static Collection<IAttribute> getAttributes() {
        return PLAYER_BAUBLES.values();
    }
    public static List<BaubleTypeEx> computeSlots(EntityLivingBase entity) {
        AbstractAttributeMap map = entity.getAttributeMap();
        return TypesData.getOrder().stream()
                .flatMap(type -> Collections.nCopies(getValue(map, type), type).stream())
                .collect(Collectors.toList());
    }

    private static IAttribute generate(BaubleTypeEx type) {
        return new NonNegativeAttribute(null, type.getTranslateKey(), type.getAmount()).setShouldWatch(true);
    }

    public static void attachAttributes(EntityLivingBase entity, IBaublesItemHandler handler) {
        AbstractAttributeMap map = entity.getAttributeMap();
        for (IAttribute attribute : PLAYER_BAUBLES.values()) {
            map.attributes.put(attribute, new AdvancedInstance(map, attribute).addListener(handler));
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
