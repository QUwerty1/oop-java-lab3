package Reader;

import IntArray.IntArray;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryReader extends Reader{
    final private DataInputStream input;

    public BinaryReader(InputStream inputStream) {
        super();
        input = new DataInputStream(inputStream);
    }

    @Override
    public IntArray read() throws IOException {
        int[] numbers = new int[input.available() / 4];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = input.readInt();
        }
        return new IntArray(numbers);
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
}
