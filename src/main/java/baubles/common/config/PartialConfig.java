package baubles.common.config;

public abstract class PartialConfig {
    public PartialConfig(){ loadData(); }

    public abstract void loadData();

    @SafeVarargs
    public static void create(Class<? extends PartialConfig>... classes) {
        try {
            for (Class<?> clazz : classes) {
                clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
