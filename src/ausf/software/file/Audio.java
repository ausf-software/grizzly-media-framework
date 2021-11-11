/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.file;

import java.util.Arrays;

/**
 *
 * Implementation of an abstract object containing a
 * standard set of audio parameters.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public abstract class Audio {

    /**
     * Data compression format registration index
     */
    protected short audioFormat;
    /**
     * Number of audio channels.
     */
    protected short numChannels;
    /**
     * Audio sample rate.
     */
    protected int sampleRate;
    /**
     * The number of bytes transferred per second of playback.
     */
    protected int byteRate;
    /**
     * The number of bytes for one sample, including all channels.
     */
    protected short blockAlign;
    /**
     * The number of bits in the sample.
     */
    protected short bitsPerSample;

    /**
     * An array of audio data bytes.
     */
    protected byte[] data;

    /**
     * Returns data compression format registration index.
     *
     * @return data compression format registration index
     */
    public short getAudioCodec() {
        return audioFormat;
    }

    /**
     * Returns number of audio channels.
     *
     * @return number of audio channels
     */
    public short getNumberAudioChanel() {
        return numChannels;
    }

    /**
     * Returns audio sample rate.
     *
     * @return audio sample rate
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Returns the number of bytes transferred per second of playback.
     *
     * @return the number of bytes transferred per second of playback
     */
    public int getByteRate() {
        return byteRate;
    }

    /**
     * Returns the number of bytes for one sample.
     *
     * @return the number of bytes for one sample
     */
    public short getBlockAlign() {
        return blockAlign;
    }

    /**
     * Returns the number of bits in the sample.
     *
     * @return the number of bits in the sample
     */
    public short getBitsPerSample() {
        return bitsPerSample;
    }

    /**
     * Returns the size of the audio data chunk of the file.
     *
     * @return the size of the audio data chunk of the file
     */
    public int getDataSize() {
        return data.length;
    }

    /**
     * Calculates and returns the duration of the audio data.
     *
     * @return the value of the duration of the audio data
     *
     */
    public double getAudioDuration() {
        return (double) data.length / (numChannels * sampleRate * (bitsPerSample / 8));
    }

    /**
     * Returns an array of audio data bytes.
     *
     * @return an array of audio data bytes
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Returns an array of audio data for the specified frame interval.
     *
     * @return an array of audio data bytes for the specified frame interval
     * @param startPoint initial frame
     * @param stopPoint end frame
     */
    public byte[] getBytesTimeInterval(int startPoint, int stopPoint) {
        return Arrays.copyOfRange(data, startPoint * numChannels * sampleRate * (bitsPerSample / 8),
                stopPoint * numChannels * sampleRate * (bitsPerSample / 8));
    }

}
