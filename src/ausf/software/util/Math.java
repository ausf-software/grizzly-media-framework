/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.util;

import ausf.software.file.Audio;

/**
 *
 * A class containing implementations of methods for
 * calculating the required quantities, as well as methods
 * for converting data from one type to another.
 *
 * @author  Shcherbina Daniil
 * @see     Audio
 * @since   0.1.0
 * @version 0.1.0
 */
public class Math {

    /**
     * Converts a number to an array of bytes and returns this array.
     *
     * @param i the number to be converted to a byte array
     * @return an array of bytes that the number was converted to
     */
    public static byte[] toBytes(short i) {
        byte[] result = new byte[2];

        result[0] = (byte) (i >> 8);
        result[1] = (byte) (i /*>> 0*/);

        return result;
    }

    /**
     * Converts a number to an array of bytes and returns this array.
     *
     * @param i the number to be converted to a byte array
     * @return an array of bytes that the number was converted to
     */
    public static byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }

    /**
     * Calculates and returns the number of bytes transmitted per second of playback.
     *
     * @param sampleRate audio sample rate
     * @param blockAlign the number of bytes for one sample, including all channels
     * @return the calculated number of bytes transmitted per second of playback.
     */
    public static int getByteRate(int sampleRate, int blockAlign){
        return sampleRate * blockAlign;
    }

    /**
     * Calculates and returns the number of bytes transmitted per second of playback.
     *
     * @param sampleRate audio sample rate
     * @param blockAlign the number of bytes for one sample, including all channels
     * @return the calculated number of bytes transmitted per second of playback.
     */
    public static int getByteRate(int sampleRate, short blockAlign){
        return sampleRate * blockAlign;
    }

    /**
     * Calculates and returns the number of bytes for a single sample, including all channels
     *
     * @param bitsPerSample the number of bits in the sample
     * @param numChannels number of audio channels
     * @return calculated number of bytes for one sample, including all channels
     */
    public static short getBlockAlign(int bitsPerSample, int numChannels) {
        return (short) (bitsPerSample / 8 * numChannels);
    }

    /**
     * Calculates and returns the number of bytes for a single sample, including all channels
     *
     * @param bitsPerSample the number of bits in the sample
     * @param numChannels number of audio channels
     * @return calculated number of bytes for one sample, including all channels
     */
    public static short getBlockAlign(short bitsPerSample, short numChannels) {
        return (short) (bitsPerSample / 8 * numChannels);
    }


}
