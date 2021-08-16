/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants;

/**
 * Storage of constant values of the buffer
 * size for reading the file format
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */

public enum BufferSizes {

    /**
     * The size of the buffer readable for the WAV file.
     */
    WAVE_BUFFER_SIZE ( 10240),
    ;

    private int index;

    BufferSizes(int i) {
        index = i;
    }

    /**
     * Returns the buffer size.
     *
     * @return the buffer size
     *
     */
    public int getSize() {
        return index;
    }
}
