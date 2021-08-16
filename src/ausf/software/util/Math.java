package ausf.software.util;

public class Math {
    /**
     * Converts a number to an array of bytes and returns this array.
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
}
