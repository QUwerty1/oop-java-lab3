package Writer;

import IntArray.IntArray;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryWriter extends Writer {

    public BinaryWriter(OutputStream outputStream) {
        super();
        output = new DataOutputStream(outputStream);
    }

    @Override
    public void write(IntArray intArray) throws IOException {
        for (int number: intArray.getNumbers()) {
            output.writeInt(number);
        }
    }

    @Override
    public void write(String string) { }

    final private DataOutputStream output;

    @Override
    public void close() throws Exception {
        output.close();
    }
}
