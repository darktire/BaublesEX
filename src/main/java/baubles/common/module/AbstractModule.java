package baubles.common.module;

import baubles.api.module.IModule;

import java.util.UUID;

public abstract class AbstractModule implements IModule {
    protected final UUID id;
    protected int limit;

    protected AbstractModule() {
        this.id = UUID.randomUUID();
    }

    public void updateLimit(int limit) {
        this.limit = limit;
    }
}
