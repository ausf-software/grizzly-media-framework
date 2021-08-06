package ausf.software.constants;

public enum INFOListChunkID {

    IARL (new byte[]{73, 65, 82, 76}),    // Archival Location
    IART (new byte[]{73, 65, 82, 84}),    // Artist
    ICMS (new byte[]{73, 67, 77, 83}),    // Commissioned
    ICMT (new byte[]{73, 67, 77, 84}),    // Comments
    ICOP (new byte[]{73, 67, 79, 80}),    // Copyright
    ICRD (new byte[]{73, 67, 82, 68}),    // Creation date
    ICRP (new byte[]{73, 67, 82, 80}),    // Cropped
    IDIM (new byte[]{73, 68, 73, 77}),    // Dimensions
    IDPI (new byte[]{73, 68, 80, 73}),    // Dots Per Inch
    IENG (new byte[]{73, 69, 78, 71}),    // Engineer
    IGNR (new byte[]{73, 71, 78, 82}),    // Genre
    IKEY (new byte[]{73, 75, 69, 89}),    // Keywords
    ILGT (new byte[]{73, 76, 71, 84}),    // Lightness
    IMED (new byte[]{73, 77, 69, 68}),    // Medium
    INAM (new byte[]{73, 78, 65, 77}),    // Name
    IPLT (new byte[]{73, 80, 76, 84}),    // Palette Setting
    IPRD (new byte[]{73, 80, 82, 68}),    // Product
    ISBJ (new byte[]{73, 83, 66, 74}),    // Subject
    ISFT (new byte[]{73, 83, 70, 84}),    // Software
    ISHP (new byte[]{73, 83, 72, 80}),    // Sharpness
    ISRC (new byte[]{73, 83, 82, 67}),    // Source
    ISRF (new byte[]{73, 83, 82, 70}),    // Source Form
    ITCH (new byte[]{73, 84, 67, 72}),    // Technician
    ;

    private byte[] index;

    INFOListChunkID(byte[] bytes) {
        index = bytes;
    }

    public byte[] getByte(){
        return index;
    }

}
