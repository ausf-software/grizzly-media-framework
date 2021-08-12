/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants;

public enum WAVContainerNameByte {

    CONTAINER_RIFF (new byte[]{82, 73, 70, 70}),
    CHUNK_WAVE (new byte[]{87, 65, 86, 69}),
    CHUNK_FTM (new byte[]{102, 109, 116, 32}),
    CHUNK_DATA (new byte[]{100, 97, 116, 97}),
    CHUNK_INFO (new byte[]{73, 78, 70, 79}),
    ;

    private byte[] index;

    WAVContainerNameByte(byte[] bytes) {
        index = bytes;
    }

    public byte[] getByte() {
        return index;
    }

}
