/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants;

public enum WAVCodecRegistries {

   FORMAT_PCM ((byte) 1),
   FORMAT_ALAW ((byte) 6),
   FORMAT_MULAW ((byte) 7)
    ;

   private byte index;

    WAVCodecRegistries(byte i) {
        index = i;
    }

    public byte getIndex() {
            return index;
    }
}

