/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.file;

import java.util.Arrays;

/**
 *
 * Implementation of a container object for convenient transportation
 * of audio data for subsequent processing.
 *
 * <p>Each instance of Audio Data stores information about the data compression
 * format, the number of audio channels, the number of bytes transmitted per
 * second of playback, the number of bytes for one sample, the number of bits in the sample
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */

public class AudioData {

    /**
     * Data compression format registration index.
     */
    private short audioFormat;
    /**
     * Number of audio channels.
     */
    private short numChannels;
    /**
     * Audio sample rate.
     */
    private int sampleRate;
    /**
     * The number of bytes transferred per second of playback.
     */
    private int byteRate;
    /**
     * The number of bytes for one sample, including all channels.
     */
    private short blockAlign;
    /**
     * The number of bits in the sample.
     */
    private short bitsPerSample;

    /**
     * An array of audio data bytes.
     */
    private byte[] data;

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
     * Returns the number of bytes transferred per second of playback.
     *
     * @return the number of bytes transferred per second of playback
     */
    public int getByteRate() {
        return byteRate;
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
     * Returns the number of bits in the sample.
     *
     * @return the number of bits in the sample
     */
    public short getBitsPerSample() {
        return bitsPerSample;
    }

    /**
     * Returns data compression format registration index.
     *
     * @return data compression format registration index
     */
    public short getAudioFormat() {
        return audioFormat;
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
     * Returns number of audio channels.
     *
     * @return number of audio channels
     */
    public short getNumChannels() {
        return numChannels;
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
     * Returns an array of audio data bytes.
     *
     * @return an array of audio data bytes
     */
    public byte[] getData() {
        return data;
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
     * Returns an array of audio data for the specified frame interval.
     *
     * @return an array of audio data bytes for the specified frame interval
     *
     * @param startPoint initial frame
     * @param stopPoint end frame
     */
    public byte[] getBytesTimeInterval(int startPoint, int stopPoint) {
        return Arrays.copyOfRange(data, startPoint * numChannels * sampleRate * (bitsPerSample / 8),
                stopPoint * numChannels * sampleRate * (bitsPerSample / 8));
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
