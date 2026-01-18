package baubles.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaublesTransformer implements IClassTransformer {
    private static final String TARGET_OWNER = "baubles/common/Config";
    private static final String TARGET_FIELD = "renderBaubles";
    private static final String FIELD_DESC = "Z";

    private static final String[] TARGETS = new String[]{
            "artifacts.client.model.layer.LayerBauble#doRenderLayer",
            "keletu.enigmaticlegacy.client.LayerBauble#doRenderLayer",
            "rlmixins.client.layer.LayerQuarkBaubleHat#doRenderLayer"
    };

    private static final Map<String, Set<String>> MAP = new HashMap<>();
    static {
        for (String raw : TARGETS) {
            int separator = raw.indexOf("#");
            String className = raw.substring(0, separator);
            String methodKey = raw.substring(separator + 1);

            MAP.computeIfAbsent(className, k -> new HashSet<>()).add(methodKey);
        }
    }

    private boolean isClassTarget(String transformedName) {
        return MAP.containsKey(transformedName);
    }

    private boolean isMethodTarget(String transformedName, String methodName) {
        Set<String> targetMethods = MAP.get(transformedName);
        return targetMethods.contains(methodName);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!isClassTarget(transformedName)) return basicClass;

        ClassReader cr = new ClassReader(basicClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

        cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String methodDesc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, methodName, methodDesc, signature, exceptions);

                if (!isMethodTarget(transformedName, methodName)) return mv;

                return new MethodVisitor(Opcodes.ASM5, mv) {
                    @Override
                    public void visitFieldInsn(int opcode, String owner, String fieldName, String fieldDesc) {
                        if (opcode == Opcodes.GETSTATIC
                                && TARGET_OWNER.equals(owner)
                                && TARGET_FIELD.equals(fieldName)
                                && FIELD_DESC.equals(fieldDesc)) {
                            super.visitInsn(Opcodes.ICONST_1);
                            return;
                        }
                        super.visitFieldInsn(opcode, owner, fieldName, fieldDesc);
                    }
                };
            }
        }, ClassReader.EXPAND_FRAMES);

        return cw.toByteArray();
    }
}
