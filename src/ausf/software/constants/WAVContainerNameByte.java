/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants;

/**
 * Storage of byte values for WAV containers name.
 */

public enum WAVContainerNameByte {

    CONTAINER_RIFF (new byte[]{82, 73, 70, 70}),
    /**
     * FourCC that defines the form type
     */
    CHUNK_WAVE (new byte[]{87, 65, 86, 69}),
    /**
     * The chunk stories information about the form in
     * which audio data is stored in the file and how it should be play
     */
    CHUNK_FTM (new byte[]{102, 109, 116, 32}),
    /**
     * A chunk that stores data from digital samples of the audio signal
     */
    CHUNK_DATA (new byte[]{100, 97, 116, 97}),
    /**
     * The chunk stores the metadata of the file
     */
    CHUNK_INFO (new byte[]{73, 78, 70, 79}),
    ;

    private byte[] index;

    WAVContainerNameByte(byte[] bytes) {
        index = bytes;
    }

    /**
     * Returns an array of byte with the identifier name.
     *
     * @return an array of byte with the identifier name
     */
    public byte[] getByte() {
        return index;
    }

}
