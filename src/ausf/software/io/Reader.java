/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.io;

import ausf.software.util.ByteBufferReader;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static ausf.software.util.Array.reverseByteArray;

/**
 *
 * Implementation of an abstract object containing shared
 * data and methods with tools for reading data from a file.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public abstract class Reader {

    /**
     * An instance of a reader with a set buffer size for reading
     */
    protected ByteBufferReader reader;
    /**
     * An array of bytes of the buffer read from the file
     */
    protected byte[] buffer;
    /**
     * The number of the read buffer
     */
    protected int bufferPageNumber;

    /**
     * File path.
     */
    protected String path;

    /**
     * Converting an array to a buffer to further convert the buffer to a number.
     *
     * @param offset the offset of the fragment in the buffer byte array
     * @param endField the end point of the fragment in the buffer byte array
     * @return the ByteBuffer received during the conversion of the input array
     */
    protected ByteBuffer convertAreaArrayToBuffer(int offset, int endField) {
        return ByteBuffer.wrap(getByteArrayField(offset, endField));
    }

    /**
     * Copy area buffer and revers array.
     *
     * @param offset the offset of the fragment in the buffer byte array
     * @param endField the end point of the fragment in the buffer byte array
     * @return returns an inverted array from the desired one
     */
    protected byte[] getByteArrayField(int offset, int endField) {
        return reverseByteArray(getBufferArea(offset, endField));
    }

    /**
     * Reads a fragment at the specified offset from the buffer byte array, and returns the read array.
     *
     * @param offset the offset of the fragment in the buffer byte array
     * @param endArea the end point of the fragment in the buffer byte array
     * @return an array with a fragment of the buffer byte array
     */
    protected byte[] getBufferArea(int offset, int endArea) {
        return Arrays.copyOfRange(buffer, offset, endArea);
    }


}
