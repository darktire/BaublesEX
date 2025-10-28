package baubles.common.container;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpansionManager {
    private static final ExpansionManager INSTANCE = new ExpansionManager();
    private static final Map<EntityPlayerMP, ContainerExpanded> DATA = new ConcurrentHashMap<>();

    private ExpansionManager() {}

    public static ExpansionManager getInstance() {
        return INSTANCE;
    }

    public void openExpansion(EntityPlayerMP player, ContainerExpanded container) {
        closeExpansion(player);
        DATA.put(player, container);
    }

    public void closeExpansion(EntityPlayerMP player) {
        ContainerExpanded removed = DATA.remove(player);
        if (removed == null) return;
        removed.onContainerClosed(player);
    }

    public ContainerExpanded getPlayerExpansion(EntityPlayerMP player) {
        return DATA.get(player);
    }

    public boolean hasOpenExpansion(EntityPlayerMP player) {
        return DATA.containsKey(player);
    }
}
