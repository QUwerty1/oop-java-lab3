package Reader;

import IntArray.IntArray;

import java.io.IOException;
import java.io.InputStream;

public abstract class Reader implements AutoCloseable {
    public Reader() {
    }

    public abstract IntArray read() throws IOException, NumberFormatException;
    public abstract void close() throws IOException;
}
