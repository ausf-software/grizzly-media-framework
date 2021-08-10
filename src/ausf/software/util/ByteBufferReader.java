// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ByteBufferReader {

    private FileInputStream stream;
    private byte[] buffer;

    public ByteBufferReader(String path) {
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBuffer(int bufferSize) {

        try {
            if(stream.available() > bufferSize) {
                writeInBuffer(bufferSize);
            } else {
                writeInBuffer(stream.available());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    private void writeInBuffer(int size) throws IOException {
        buffer = new byte[size];
        stream.read(buffer, 0, size);
    }

    public int getRemnant() {
        try {
            return stream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void close() {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
