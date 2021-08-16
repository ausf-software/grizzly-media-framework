/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Implementation of a file reader of a fixed size of the readable buffer.
 *
 * @see ausf.software.file.audio.WAVFile
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public class ByteBufferReader {

    /**
     * Data reading stream
     */
    private FileInputStream stream;
    /**
     * Read data buffer array
     */
    private byte[] buffer;

    /**
     * Creating an instance of the reader with the specified file path.
     * @param path file path
     */
    public ByteBufferReader(String path) {
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the specified data fragment from the stream and
     * returns an array of bytes of the read buffer.
     * @param bufferSize the size of the buffer to be read
     * @return an array of bytes of the read buffer
     */
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

    /**
     * Sets the buffer size and reads data from the stream.
     * @param size the size of the buffer to be read
     * @throws IOException
     */
    private void writeInBuffer(int size) throws IOException {
        buffer = new byte[size];
        stream.read(buffer, 0, size);
    }

    /**
     * Returns the number of bytes that are left to read.
     * @return the number of bytes that are left to read
     */
    public int getRemnant() {
        try {
            return stream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Closes the data reading stream.
     */
    public void close() {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
