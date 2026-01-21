package baubles.common.module;

import baubles.api.module.IModule;

import java.util.UUID;

public abstract class AbstractModule implements IModule {
    protected final UUID id;
    protected int max;
    protected int min;

    protected AbstractModule() {
        this.id = UUID.randomUUID();
    }

    protected boolean validate(int value) {
        return this.min <= value && value <= this.max;
    }
}
