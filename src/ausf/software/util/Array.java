/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.util;

/**
 *
 * A class containing implementations of methods for working with arrays.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public class Array {

    /**
     * Flips the incoming array
     *
     * @param array an array that needs to be flipped
     * @return an inverted array from the source
     */
    public static byte[] reverseByteArray(byte[] array) {
        byte[] tmp = new byte[array.length];
        for(int i = 0; i < array.length; i++) {
            tmp[i] = array[(array.length - 1) - i];
        }
        return tmp;
    }

}
