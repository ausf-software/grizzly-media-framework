package ausf.software.constants;

public enum WAVField {

    CONTAINER_RIFF      (new Field(4, 0)),
    RIFF_CONTAINER_SIZE (new Field(4, 4)),
    FORMAT_TAG          (new Field(4, 8)),

    CHUNK_FTM           (new Field(4, 12)),
    CHUNK_FTM_SIZE      (new Field(4, 16)),
    AUDIO_FORMAT        (new Field(2, 20)),
    NUMBER_CHANNELS     (new Field(2, 22)),
    SAMPLE_RATE         (new Field(4, 24)),
    BYTE_RATE           (new Field(4, 28)),
    BLOCK_ALIGN         (new Field(2, 32)),
    BITS_PER_SAMPLE     (new Field(2, 34)),

    CHUNK_DATA          (new Field(4, 0)), // this field has a local offset
    DATA_SIZE           (new Field(4, 4)), // this field has a local offset

    CHUNK_INFO           (new Field(4, 0)), // this field has a local offset
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
