package Reader;

import IntArray.IntArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextReader extends Reader {
    final private BufferedReader input;

    public TextReader(InputStream inputStream) {
        super(inputStream);
        input = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public IntArray read() throws IOException {
        return new IntArray(input.readLine());
    }

    @Override
    public void close() throws Exception {
        input.close();
    }
}
