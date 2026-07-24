package baubles.api.module;

import net.minecraft.entity.EntityLivingBase;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleCore {
    private final Map<IModule, AtomicInteger> levels = new WeakHashMap<>();

    public void increment(IModule module) {
        levels.computeIfAbsent(module, k -> new AtomicInteger(0)).incrementAndGet();
    }
    
    public void decrement(IModule module) {
        AtomicInteger count = levels.get(module);
        if (count == null) {
            levels.put(module, new AtomicInteger(0));
            return;
        }
        count.decrementAndGet();
    }

    public void batchIncrement(Collection<IModule> modules) {
        for (IModule module : modules) {
            increment(module);
        }
    }
    public void batchDecrement(Collection<IModule> modules) {
        for (IModule module : modules) {
            decrement(module);
        }
    }

    public void restore(Collection<IModule> modules) {
        this.levels.clear();
        for (IModule module : modules) {
            this.levels.computeIfAbsent(module, key -> new AtomicInteger(0)).incrementAndGet();
        }
    }

    public void apply(EntityLivingBase entity) {
        this.levels.forEach((key, value) -> key.updateStatus(entity, value.get()));
    }
}