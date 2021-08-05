// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.constants;

public enum WAVFieldsOffset {

    CONTAINER_RIFF (0),
    RIFF_CONTAINER_SIZE (4),
    FORMAT_TAG (8),

    CHUNK_FTM (12),
    CHUNK_FTM_SIZE (16),
    AUDIO_FORMAT (20),
    NUMBER_CHANNELS (22),
    SAMPLE_RATE (24),
    BYTE_RATE (28),
    BLOCK_ALIGN (32),
    BITS_PER_SAMPLE (34),
    ;

    private int index;

    WAVFieldsOffset(int bytes) {
        index = bytes;
    }

    public int getPosition(){
        return index;
    }

}
