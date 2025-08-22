package baubles.coremod;

import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.FMLLog;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BaublesTransformer implements IClassTransformer, Opcodes {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.matches(".*Item(Angel|Chicken)Ring")) {
            log("tansforming " + transformedName);
            return redirectBaublesCap(basicClass);
        }
        return basicClass;
    }

    public static byte[] redirectBaublesCap(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);

        classNode.methods.forEach(method -> {
            if (method.name.equals("initCapabilities")) {
                for (AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                    if (insn.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode) insn).name.equals("getBaubleProvider")) {
                        method.instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 1));
                        MethodInsnNode newCall = new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "baubles/coremod/BaublesTransformer",
                                "hook",
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

    public static ICapabilityProvider hook(ICapabilityProvider provider, ItemStack stack) {
        IBauble bauble = provider.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
        provider.getClass();
        if (!(bauble instanceof BaublesWrapper)) {
            if (!ItemsData.isBauble(stack.getItem())) {
                ItemsData.registerBauble(stack.getItem(), bauble.getBaubleType(stack).getNewType());
            }
            return BaublesCapabilityProvider.getProvider(stack);
        }
        return provider;
    }

    private static void log(String s) {
        FMLLog.info("[Baubles ASM] %s", s);
    }
}
