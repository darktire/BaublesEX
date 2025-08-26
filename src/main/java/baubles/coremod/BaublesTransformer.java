package baubles.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BaublesTransformer implements IClassTransformer, Opcodes {
    private static final Logger log = LogManager.getLogger("BAUBLES_ASM");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.matches("com.rwtema.extrautils2.items.Item(Angel|Chicken)Ring")) {
            debug(transformedName);
            return redirectBaublesCap(basicClass);
        }
        else if (transformedName.matches("goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustom(Cape|Elytra)")) {
            debug(transformedName);
            return elytraInBaubles(basicClass);
        }
        return basicClass;
    }

    private static byte[] redirectBaublesCap(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);

        classNode.methods.forEach(method -> {
            if (method.name.equals("initCapabilities")) {
                for (AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                    if (insn.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode) insn).name.equals("getBaubleProvider")) {
                        method.instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 1));
                        MethodInsnNode newCall = new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "baubles/common/util/HookHelper",
                                "redirectBaublesCap",
                                "(Lnet/minecraftforge/common/capabilities/ICapabilityProvider;Lnet/minecraft/item/ItemStack;)Lnet/minecraftforge/common/capabilities/ICapabilityProvider;",
                                false);

                        method.instructions.insert(insn.getNext(), newCall);
                        break;
                    }
                }
            }
        });

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private static byte[] elytraInBaubles(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);

        classNode.methods.forEach(method -> {
            if (method.name.equals("doRenderLayer")) {
                for (AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                    if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) insn).name.equals("getItemStackFromSlot")) {
                        MethodInsnNode newCall = new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "baubles/common/util/HookHelper",
                                "elytraInBaubles",
                                "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;",
                                false);

                        method.instructions.insertBefore(insn, newCall);
                        method.instructions.remove(insn);
                        break;
                    }
                }
            }
        });

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private static void debug(String s) {
        log.debug(String.format("transforming %s",s));
    }
}
