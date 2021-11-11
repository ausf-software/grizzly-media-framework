/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.containers;

import ausf.software.file.Audio;

/**
 *
 * Implementation of a container object for convenient transportation
 * of audio data for subsequent processing.
 *
 * <p>Each instance of AudioData stores information about the data compression
 * format, the number of audio channels, the number of bytes transmitted per
 * second of playback, the number of bytes for one sample, the number of bits in the sample
 *
 * @author  Shcherbina Daniil
 * @see     Audio
 * @since   0.1.0
 * @version 0.1.0
 */

public class AudioData extends Audio {

    /**
     * Creates an instance with the existing audio data
     *
     * @param audioFormat data compression format registration index
     * @param numChannels number of audio channels
     * @param sampleRate audio sample rate
     * @param byteRate the number of bytes transferred per second of playback
     * @param blockAlign the number of bytes for one sample
     * @param bitsPerSample the number of bits in the sample
     * @param data an array of audio data bytes
     */
    public AudioData(short audioFormat, short numChannels, int sampleRate,
                     int byteRate, short blockAlign, short bitsPerSample, byte[] data) {
        this.audioFormat = audioFormat;
        this.numChannels = numChannels;
        this.sampleRate = sampleRate;
        this.byteRate = byteRate;
        this.blockAlign = blockAlign;
        this.bitsPerSample = bitsPerSample;
        this.data = data;
    }

    /**
     * Returns true if the size of the data array is not zero.
     *
     * @return true if the size of the data array is not zero
     */
    public boolean isDataCorrect() {
        return data.length != 0;
    }

}
