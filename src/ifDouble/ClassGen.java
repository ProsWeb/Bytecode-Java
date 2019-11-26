package ifDouble;

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
        generateDMethod(cw);
        generateAbsMethod(cw);
        cw.visitEnd();
        return cw.toByteArray();
    }

    private void generateDefaultConstructor(final ClassWriter cw) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void generateDMethod(final ClassWriter cw) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "d", // method name
                "(DDD)D", // method descriptor
                null,    // exceptions
                null);   // method attributes
        mv.visitCode();
        // BEGIN (write your solution here)
        mv.visitVarInsn(Opcodes.DLOAD, 2); //b
        mv.visitVarInsn(Opcodes.DLOAD, 2); //b
        mv.visitInsn(Opcodes.DMUL); // b * b -> stack
        mv.visitLdcInsn(4.0);
        mv.visitVarInsn(Opcodes.DLOAD, 0); //a
        mv.visitVarInsn(Opcodes.DLOAD, 4); //c
        mv.visitInsn(Opcodes.DMUL); // a * c -> stack
        mv.visitInsn(Opcodes.DMUL); // 4 * (ac) -> stack
        mv.visitInsn(Opcodes.DSUB); // (bb) - (4ac)
        mv.visitInsn(Opcodes.DRETURN);
        mv.visitMaxs(4, 6);
        // END
        mv.visitEnd();
    }

    private void generateAbsMethod(final ClassWriter cw ) {
        final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "abs",    // method name
                "(D)D", // method descriptor
                null,     // exceptions
                null);    // method attributes
        mv.visitCode();
        final Label elseLabel = new Label();
        // BEGIN (write your solution here)

        // load variable and const 0 to stack
        mv.visitVarInsn(Opcodes.DLOAD, 0);
        mv.visitInsn(Opcodes.DCONST_0);
        // compare variable with 0
        mv.visitInsn(Opcodes.DCMPL);
        // compare result of double comparison with 0
        mv.visitJumpInsn(Opcodes.IFGE, elseLabel);

        // if the result of comparation has been lower than 0 return -a
        mv.visitVarInsn(Opcodes.DLOAD, 0);
        mv.visitInsn(Opcodes.DNEG);
        mv.visitInsn(Opcodes.DRETURN);

        // if the result of comparation has been greater than 0 return a
        mv.visitLabel(elseLabel);
        mv.visitVarInsn(Opcodes.DLOAD, 0);
        mv.visitInsn(Opcodes.DRETURN);

        mv.visitMaxs(4, 2);
        // END
        mv.visitEnd();
    }
}
