// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.constants;

public enum WAVField {

    CONTAINER_RIFF      (new Field(4, 0)),
    RIFF_CONTAINER_SIZE (new Field(4, 4)),
    FORMAT_TAG          (new Field(4, 8)),

    CHUNK_FTM           (new Field(4, 0)),
    CHUNK_FTM_SIZE      (new Field(4, 4)),
    AUDIO_FORMAT        (new Field(2, 8)),
    NUMBER_CHANNELS     (new Field(2, 10)),
    SAMPLE_RATE         (new Field(4, 12)),
    BYTE_RATE           (new Field(4, 16)),
    BLOCK_ALIGN         (new Field(2, 20)),
    BITS_PER_SAMPLE     (new Field(2, 22)),

    CHUNK_DATA          (new Field(4, 0)),
    DATA_SIZE           (new Field(4, 4)),

    LIST_CONTAINER      (new Field(4, 0)),
    CHUNK_INFO          (new Field(4, 0)),
    ;


    private Field index;

    WAVField(Field bytes) {
        index = bytes;
    }

    public int getSize() {
        return index.size;
    }

    public int getOffset() {
        return index.getOffset();
    }

    public int getFieldEnd() {
        return index.getOffset() + index.getSize();
    }


    static class Field {

        private int size;
        private int offset;

        public Field(int size, int offset) {
            this.offset = offset;
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public int getOffset() {
            return offset;
        }

    }

}
