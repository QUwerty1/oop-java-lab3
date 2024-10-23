package Reader;

import IntArray.IntArray;

import java.io.IOException;
import java.io.InputStream;

public abstract class Reader implements AutoCloseable {
    public Reader(InputStream inputStream) {
    }

    public abstract IntArray read() throws IOException;
}
