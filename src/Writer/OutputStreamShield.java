package Writer;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamShield extends OutputStream implements AutoCloseable {

    final private OutputStream output;

    public OutputStreamShield(OutputStream outputStream) {
        output = outputStream;
    }

    @Override
    public void write(int i) throws IOException {
        output.write(i);
    }

    public void close() throws IOException {
        output.flush();
    }
}
