/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants.audio.wav;

import ausf.software.containers.DataField;

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
    CONTAINER_RIFF      (new DataField(4, 0)),
    /**
     * RIFF container size
     */
    RIFF_CONTAINER_SIZE (new DataField(4, 4)),
    /**
     * FourCC that defines the form type
     */
    FORMAT_TAG          (new DataField(4, 8)),

    /**
     * The chunk stories information about the form in
     * which audio data is stored in the file and how it should be play
     */
    CHUNK_FTM           (new DataField(4, 0)),
    /**
     * The size of the fragment containing information
     * about the form in which the audio data is stored in
     * the file, and how they should be played
     */
    CHUNK_FTM_SIZE      (new DataField(4, 4)),
    /**
     * Data compression format registration index.
     */
    AUDIO_FORMAT        (new DataField(2, 8)),
    /**
     * Number of audio channels.
     */
    NUMBER_CHANNELS     (new DataField(2, 10)),
    /**
     * Audio sample rate.
     */
    SAMPLE_RATE         (new DataField(4, 12)),
    /**
     * The number of bytes transferred per second of playback.
     */
    BYTE_RATE           (new DataField(4, 16)),
    /**
     * The number of bytes for one sample, including all channels.
     */
    BLOCK_ALIGN         (new DataField(2, 20)),
    /**
     * The number of bits in the sample.
     */
    BITS_PER_SAMPLE     (new DataField(2, 22)),

    /**
     * A chunk that stores data from digital samples of the audio signal
     */
    CHUNK_DATA          (new DataField(4, 0)),
    /**
     * The size of the fragment in which data from digital samples of
     * the audio signal is stored
     */
    DATA_SIZE           (new DataField(4, 4)),

    LIST_CONTAINER      (new DataField(4, 0)),
    /**
     * The chunk stores the metadata of the file
     */
    CHUNK_INFO          (new DataField(4, 0)),
    ;


    private DataField index;

    WAVField(DataField bytes) {
        index = bytes;
    }

    /**
     * Returns the length of the field.
     *
     * @return the length of the field
     */
    public int getSize() {
        return index.getSize();
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

}
