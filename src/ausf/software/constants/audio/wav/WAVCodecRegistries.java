/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants.audio.wav;

/**
 * Storage of constant values of registered identifiers
 * of data compression formats for WAV.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public enum WAVCodecRegistries {

    /**
     * Pulse-code modulation
     */
   FORMAT_PCM ((byte) 1),
    /**
     * A-law algorithm
     */
   FORMAT_ALAW ((byte) 6),
    /**
     * μ-law algorithm
     */
   FORMAT_MULAW ((byte) 7)
    ;

   private byte index;

    WAVCodecRegistries(byte i) {
        index = i;
    }

    /**
     * Returns data compression format registration index.
     *
     * @return data compression format registration index
     */
    public byte getIndex() {
            return index;
    }
}

