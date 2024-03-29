package forLoop;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import java.io.FileOutputStream;

public class ClassGen {

    public static void main(final String... args) throws Exception {
        final String path = args[0];
        final byte[] byteCode = new ClassGen().generateMathClass();
        try (FileOutputStream stream = new FileOutputStream(path)) {
            stream.write(byteCode);
        }
    }

    private byte[] generateMathClass() {
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(51,
                Opcodes.ACC_PUBLIC,
                "Math",
                null,
                "java/lang/Object",
                null);
        generateDefaultConstructor(cw);
        generateSumMethod(cw);
        cw.visitEnd();
        return cw.toByteArray();
    }

    private void generateDefaultConstructor(final ClassWriter cw) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void generateSumMethod(final ClassWriter cw) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "sum", // method name
                "(I)I", // method descriptor
                null,    // exceptions
                null);   // method attributes

        Label forLabel = new Label();
        Label label = new Label();
        mv.visitCode();
        // BEGIN (write your solution here)
        // int result = 0;
        mv.visitInsn(Opcodes.ICONST_0); // load result = 0
        mv.visitVarInsn(Opcodes.ISTORE, 1); // save result
        // int i = 0;
        mv.visitInsn(Opcodes.ICONST_0); // load i = 0
        mv.visitVarInsn(Opcodes.ISTORE, 2); // save i
        // i <= a;
        mv.visitLabel(forLabel);
        mv.visitVarInsn(Opcodes.ILOAD, 2); // load i from the stack
        mv.visitVarInsn(Opcodes.ILOAD, 0); // load a from the stack
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, label);

        mv.visitVarInsn(Opcodes.ILOAD, 1); // load result from the stack
        mv.visitVarInsn(Opcodes.ILOAD, 2); // load i from the stack
        mv.visitInsn(Opcodes.ICONST_1); // load 1
        mv.visitInsn(Opcodes.IADD); // (i + 1)
        mv.visitInsn(Opcodes.IADD); // result + (i + 1)
        mv.visitVarInsn(Opcodes.ISTORE, 1); // save result
        mv.visitIincInsn(2, 1); // i++
        mv.visitJumpInsn(Opcodes.GOTO, forLabel);

        // label
        mv.visitLabel(label);
        mv.visitVarInsn(Opcodes.ILOAD, 1); // load result from the stack
        mv.visitInsn(Opcodes.IRETURN);

        mv.visitMaxs(3, 3);
        // END
    }
}