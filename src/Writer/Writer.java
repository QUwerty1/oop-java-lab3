package Writer;

import IntArray.IntArray;

import java.io.IOException;
import java.io.OutputStream;

abstract public class Writer implements AutoCloseable {
    public Writer(OutputStream outputStream) {}
    public abstract void write(IntArray intArray) throws IOException;
}
