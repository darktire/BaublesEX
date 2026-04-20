package baubles.lib.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ASMScanner {

    private static final Map<String, String> CACHE = new HashMap<>();

    private final Class<?> startClass;
    private final Set<String> visited = new HashSet<>();
    private String foundInClassName;

    private ASMScanner(Class<?> startClass) {
        this.startClass = startClass;
    }

    public static ASMScanner of(Class<?> clazz) {
        return new ASMScanner(clazz);
    }

    public ASMScanner getMethod(String name, String desc) {
        String key = startClass.getName() + '|' + name + '|' + desc;
        String cached = CACHE.get(key);
        if (cached != null) {
            this.foundInClassName = cached.isEmpty() ? null : cached;
        } else {
            this.foundInClassName = scanRecursive(startClass, name, desc);
            CACHE.put(key, foundInClassName == null ? "" : foundInClassName);
        }
        return this;
    }

    private String scanRecursive(Class<?> clazz, String name, String desc) {
        if (clazz == null || clazz == Object.class || !visited.add(clazz.getName())) return null;

        if (hasMethodInBytecode(clazz, name, desc)) return clazz.getName();

        String found = scanRecursive(clazz.getSuperclass(), name, desc);
        if (found != null) return found;

        for (Class<?> itf : clazz.getInterfaces()) {
            found = scanRecursive(itf, name, desc);
            if (found != null) return found;
        }

        return null;
    }

    private boolean hasMethodInBytecode(Class<?> clazz, String name, String desc) {
        String resource = "/" + clazz.getName().replace('.', '/') + ".class";
        try (InputStream is = clazz.getResourceAsStream(resource)) {
            if (is == null) return false;

            ClassReader cr = new ClassReader(is);
            cr.accept(new ClassVisitor(Opcodes.ASM5) {
                @Override
                public MethodVisitor visitMethod(int access, String mName, String mDesc, String sig, String[] exc) {
                    if (mName.equals(name) && mDesc.equals(desc)) {
                        boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
                        boolean isPrivate = (access & Opcodes.ACC_PRIVATE) != 0;
                        if (!isStatic && !isPrivate) {
                            throw new MethodFoundException();
                        }
                    }
                    return null;
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        } catch (MethodFoundException e) {
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isOverriddenFrom(Class<?> baseClass) {
        if (foundInClassName == null) return false;
        return !foundInClassName.equals(baseClass.getName());
    }

    private static class MethodFoundException extends RuntimeException {}
}
