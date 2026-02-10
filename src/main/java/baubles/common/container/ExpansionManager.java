package baubles.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ExpansionManager {
    private static final ExpansionManager INSTANCE = new ExpansionManager();
    private final Map<UUID, ContainerExpansion> map = new ConcurrentHashMap<>();
    private Container marked;

    private ExpansionManager() {}

    public static ExpansionManager getInstance() {
        return INSTANCE;
    }

    public void openExpansion(EntityPlayer player, ContainerExpansion container) {
        this.map.put(player.getUniqueID(), container);
        this.mark(player.openContainer);
    }

    public void closeExpansion(EntityPlayer player) {
        ContainerExpansion removed = map.remove(player.getUniqueID());
        if (removed == null) return;
        removed.onContainerClosed(player);
    }

    public ContainerExpansion getExpansion(EntityPlayer player) {
        return map.get(player.getUniqueID());
    }

    public boolean isExpanded(EntityPlayer player) {
        return map.containsKey(player.getUniqueID());
    }

    public void mark(Container container) {
        this.marked = container;
    }

    public boolean isMarked(Container container) {
        boolean flag = this.marked == container;
        if (flag) this.marked = null;
        return flag;
    }
}
