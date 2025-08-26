package baubles.common.config;

public abstract class PartialConfig {
    public PartialConfig(){ loadData(); }

    public abstract void loadData();

    public static <T extends PartialConfig> void create(Class<T> clazz) {
        try {
            clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
