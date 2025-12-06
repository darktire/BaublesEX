package baubles.common.network;

import baubles.api.registries.TypesData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public final class PacketPool {

    private static final int MAX_POOL_SIZE = 256;

    private static final ConcurrentLinkedQueue<PacketSync> POOL = new ConcurrentLinkedQueue<>();

    private static final LongAdder CREATED  = new LongAdder();
    private static final LongAdder REUSED   = new LongAdder();
    private static final LongAdder RECYCLED = new LongAdder();
    private static final AtomicInteger SIZE = new AtomicInteger();

    public static PacketSync borrow(EntityLivingBase entity, int slot, ItemStack stack, int visible) {
        int entityId = -1;
        if (entity != null) entityId = entity.getEntityId();
        PacketSync pkt = POOL.poll();
        if (pkt == null) {
            CREATED.increment();
            pkt = new PacketSync();
        }
        else {
            SIZE.decrementAndGet();
            REUSED.increment();
        }
        return pkt.set(entityId, slot, stack, visible);
    }

    public static void release(PacketSync pkt) {
        if (pkt == null) return;
        pkt.reset();
        int currentSize = SIZE.getAndIncrement();
        if (currentSize >= MAX_POOL_SIZE) {
            SIZE.decrementAndGet();
            return;
        }
        POOL.offer(pkt);
        RECYCLED.increment();
    }


    public static String getStats() {
        return String.format("Baubles PacketPool: created=%d, reused=%d, recycled=%d, avail=%d", CREATED.sum(), REUSED.sum(), RECYCLED.sum(), SIZE.get());
    }
    public static void clear() {
        POOL.clear(); SIZE.set(0);
    }
    public static void resetStats() {
        CREATED.reset(); REUSED.reset(); RECYCLED.reset();
    }


    public static void warmup() {
        int preset = (int) (1.5 * TypesData.getLazyList().size() + 45);
        if (preset > MAX_POOL_SIZE) preset = MAX_POOL_SIZE;
        for (int i = 0; i < preset; i++) POOL.offer(new PacketSync());
        SIZE.set(preset);
        CREATED.add(preset);
    }

    private PacketPool() {}
}