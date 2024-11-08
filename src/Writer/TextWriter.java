package Writer;

import IntArray.IntArray;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TextWriter extends Writer {

    public TextWriter(OutputStream outputStream) {
        super();
        output = new OutputStreamWriter(outputStream);
    }

    @Override
    public void write(IntArray intArray) throws IOException {
        output.write(intArray.toString());
    }

    public void write(String string) throws IOException {
        output.write(string);
    }

    final private OutputStreamWriter output;

    @Override
    public void close() throws IOException {
        output.close();
    }
}
