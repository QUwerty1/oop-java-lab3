package Writer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryWriter extends Writer {

    public BinaryWriter(OutputStream outputStream) {
        super();
        output = new DataOutputStream(outputStream);
    }

    @Override
    public void write(int[] numbers) throws IOException {
        for (int number: numbers) {
            output.writeInt(number);
        }
    }

    @Override
    public void write(String string) { }

    final private DataOutputStream output;

    @Override
    public void close() throws IOException {
        output.close();
    }
}
