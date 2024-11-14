package Writer;

import java.io.IOException;

abstract public class Writer implements AutoCloseable {
    public Writer() {}
    public abstract void write(int[] numbers) throws IOException;
    public abstract void write(String string) throws IOException;
    public abstract void close() throws IOException;
}
