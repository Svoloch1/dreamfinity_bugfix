package svoloch.dreamfinity_bugfix;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
@IFMLLoadingPlugin.SortingIndex(value = 1001)
public class HookLoader implements IFMLLoadingPlugin, IClassTransformer {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{getClass().getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("cr0s.warpdrive.BreathingManager")) {
            basicClass = transformWarpDriveBreathingManager(basicClass);
        } else if (transformedName.equals("cr0s.warpdrive.render.RenderOverlayAir")) {
            basicClass = transformWarpDriveRenderOverlayAir(basicClass);
        }
        return basicClass;
    }

    private byte[] transformWarpDriveRenderOverlayAir(byte[] bytes) {
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        final Iterator methods = classNode.methods.iterator();

        do {
            if (!methods.hasNext()) {
                break;
            }
            final MethodNode methodNode = (MethodNode) methods.next();
            if (methodNode.name.equals("renderAir")) {
                AbstractInsnNode firstNode = methodNode.instructions.getFirst();
                while (!(firstNode instanceof VarInsnNode)) {
                    firstNode = firstNode.getNext();
                }

                MethodInsnNode invokeNode = new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "svoloch/dreamfinity_bugfix/WarpDriveManagerMO",
                        "isAndroidSP",
                        "()Z",
                        false);
                LabelNode label = new LabelNode();

                firstNode = firstNode.getPrevious();
                methodNode.instructions.insert(firstNode, label);
                methodNode.instructions.insert(firstNode, new InsnNode(Opcodes.RETURN));
                methodNode.instructions.insert(firstNode, new JumpInsnNode(Opcodes.IFEQ, label));
                methodNode.instructions.insert(firstNode, invokeNode);
            }

        } while (true);
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        return bytes;
    }

    private byte[] transformWarpDriveBreathingManager(byte[] bytes) {
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        final Iterator methods = classNode.methods.iterator();

        do {
            if (!methods.hasNext()) {
                break;
            }
            final MethodNode methodNode = (MethodNode) methods.next();
            if (methodNode.name.equals("onLivingUpdateEvent")) {
                AbstractInsnNode firstNode = methodNode.instructions.getFirst();
                while (!(firstNode instanceof VarInsnNode)) {
                    firstNode = firstNode.getNext();
                }

                MethodInsnNode invokeNode = new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "svoloch/dreamfinity_bugfix/WarpDriveManagerMO",
                        "isAndroid",
                        "(Lnet/minecraft/entity/Entity;)Z",
                        false);
                LabelNode label = new LabelNode();

                firstNode = firstNode.getPrevious();
                methodNode.instructions.insert(firstNode, label);
                methodNode.instructions.insert(firstNode, new InsnNode(Opcodes.RETURN));
                methodNode.instructions.insert(firstNode, new JumpInsnNode(Opcodes.IFEQ, label));
                methodNode.instructions.insert(firstNode, invokeNode);
                methodNode.instructions.insert(firstNode, new VarInsnNode(Opcodes.ALOAD, 0));
            } else if (methodNode.name.equals("onLivingJoinEvent")) {
                AbstractInsnNode firstNode = methodNode.instructions.getFirst();
                while (!(firstNode instanceof VarInsnNode)) {
                    firstNode = firstNode.getNext();
                }
                MethodInsnNode invokeNode = new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "svoloch/dreamfinity_bugfix/WarpDriveManagerMO",
                        "isAndroid",
                        "(Lnet/minecraft/entity/Entity;)Z",
                        false);
                LabelNode label = new LabelNode();

                firstNode = firstNode.getPrevious();
                methodNode.instructions.insert(firstNode, label);
                methodNode.instructions.insert(firstNode, new InsnNode(Opcodes.IRETURN));
                methodNode.instructions.insert(firstNode, new InsnNode(Opcodes.ICONST_1));
                methodNode.instructions.insert(firstNode, new JumpInsnNode(Opcodes.IFEQ, label));
                methodNode.instructions.insert(firstNode, invokeNode);
                methodNode.instructions.insert(firstNode, new VarInsnNode(Opcodes.ALOAD, 0));
            }
        } while (true);
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        return bytes;
    }
}
