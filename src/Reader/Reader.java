package Reader;

import java.io.IOException;

public abstract class Reader implements AutoCloseable {
    public Reader() {
    }

    public abstract int[] read() throws IOException, NumberFormatException;
    public abstract void close() throws IOException;
}
