/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants.audio.wav;

/**
 * Storage of constant values of field sizes and
 * their offsets in an array of bytes.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */

public enum WAVField {

    /**
     * Container RIFF
     */
    CONTAINER_RIFF      (new Field(4, 0)),
    /**
     * RIFF container size
     */
    RIFF_CONTAINER_SIZE (new Field(4, 4)),
    /**
     * FourCC that defines the form type
     */
    FORMAT_TAG          (new Field(4, 8)),

    /**
     * The chunk stories information about the form in
     * which audio data is stored in the file and how it should be play
     */
    CHUNK_FTM           (new Field(4, 0)),
    /**
     * The size of the fragment containing information
     * about the form in which the audio data is stored in
     * the file, and how they should be played
     */
    CHUNK_FTM_SIZE      (new Field(4, 4)),
    /**
     * Data compression format registration index.
     */
    AUDIO_FORMAT        (new Field(2, 8)),
    /**
     * Number of audio channels.
     */
    NUMBER_CHANNELS     (new Field(2, 10)),
    /**
     * Audio sample rate.
     */
    SAMPLE_RATE         (new Field(4, 12)),
    /**
     * The number of bytes transferred per second of playback.
     */
    BYTE_RATE           (new Field(4, 16)),
    /**
     * The number of bytes for one sample, including all channels.
     */
    BLOCK_ALIGN         (new Field(2, 20)),
    /**
     * The number of bits in the sample.
     */
    BITS_PER_SAMPLE     (new Field(2, 22)),

    /**
     * A chunk that stores data from digital samples of the audio signal
     */
    CHUNK_DATA          (new Field(4, 0)),
    /**
     * The size of the fragment in which data from digital samples of
     * the audio signal is stored
     */
    DATA_SIZE           (new Field(4, 4)),

    LIST_CONTAINER      (new Field(4, 0)),
    /**
     * The chunk stores the metadata of the file
     */
    CHUNK_INFO          (new Field(4, 0)),
    ;


    private Field index;

    WAVField(Field bytes) {
        index = bytes;
    }

    /**
     * Returns the length of the field.
     *
     * @return the length of the field
     */
    public int getSize() {
        return index.size;
    }

    /**
     * Returns the offset of the field.
     *
     * @return the offset of the field
     */
    public int getOffset() {
        return index.getOffset();
    }

    /**
     * Returns the final position of the field in the byte array.
     *
     * @return the final position of the field in the byte array
     */
    public int getFieldEnd() {
        return index.getOffset() + index.getSize();
    }


    /**
     * A container containing information about the size
     * and offset of a field in an array of bytes.
     *
     * @author  Shcherbina Daniil
     * @since   0.1.0
     * @version 0.1.0
     */
    static class Field {

        /**
         *  The length of the field
         */
        private int size;
        /**
         * The offset of the field
         */
        private int offset;

        /**
         * Creates an instance of the information storage with the specified size and offset.
         *
         * @param size length of the field
         * @param offset offset of the field
         */
        public Field(int size, int offset) {
            this.offset = offset;
            this.size = size;
        }

        /**
         * Returns the length of the field.
         *
         * @return the length of the field
         */
        public int getSize() {
            return size;
        }

        /**
         * Returns the offset of the field.
         *
         * @return the offset of the field
         */
        public int getOffset() {
            return offset;
        }

    }

}
