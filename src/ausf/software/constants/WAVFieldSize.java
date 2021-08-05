// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.constants;

public enum WAVFieldSize {

    CONTAINER_RIFF (4),
    RIFF_CONTAINER_SIZE (4),
    FORMAT_TAG (4),

    CHUNK_FTM (4),
    CHUNK_FTM_SIZE (4),
    AUDIO_FORMAT (2),
    NUMBER_CHANNELS (2),
    SAMPLE_RATE (4),
    BYTE_RATE (4),
    BLOCK_ALIGN (2),
    BITS_PER_SAMPLE (2),

    CHUNK_DATA (4),
    DATA_SIZE (4),
    ;


    private int index;

    WAVFieldSize(int bytes) {
        index = bytes;
    }

    public int getSize(){
        return index;
    }

}
