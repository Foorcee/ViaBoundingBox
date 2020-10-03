import de.foorcee.viaboundingbox.api.asm.ByteCodeUtils;
import de.foorcee.viaboundingbox.api.asm.ClassTransformer;
import de.foorcee.viaboundingbox.version.v1_16.BoundingBox_v1_16;
import org.bukkit.Bukkit;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestByteCode_v1_16 {

    @Test
    public void test() throws IOException {
        for (ClassTransformer classTransformer : BoundingBox_v1_16.classTransformers) {
            byte[] data = ByteCodeUtils.injectClass(classTransformer);
            String className = classTransformer.getClassName().replace("\\.", "_");
            File outputDir = new File("test/");
            File file = new File(outputDir, className + ".class");
            if(file.exists()) file.delete();

            outputDir.mkdirs();

            DataOutputStream dataOutputStream =
                    new DataOutputStream(
                            new FileOutputStream(
                                    file));

            dataOutputStream.write(data);
        }
    }
}
