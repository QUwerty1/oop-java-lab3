package Writer;

import IntArray.IntArray;

import java.io.IOException;

abstract public class Writer implements AutoCloseable {
    public Writer() {}
    public abstract void write(IntArray intArray) throws IOException;
    public abstract void write(String string) throws IOException;
    public abstract void close() throws IOException;
}
