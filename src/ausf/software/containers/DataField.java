package ausf.software.containers;

/**
 * A container containing information about the size
 * and offset of a field in an array of bytes.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public class DataField {

    /**
     *  The length of the field
     */
    private int size;
    /**
     * The offset of the field
     */
    private int offset;

    /**
     * Creates an instance of the information storage with the specified size and offset.
     *
     * @param size length of the field
     * @param offset offset of the field
     */
    public DataField(int size, int offset) {
        this.offset = offset;
        this.size = size;
    }

    /**
     * Returns the length of the field.
     *
     * @return the length of the field
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the offset of the field.
     *
     * @return the offset of the field
     */
    public int getOffset() {
        return offset;
    }

}
