/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants;

/**
 *
 * Storage of byte values for metadata field identifiers.
 *
 * <p>To describe the metadata fields, information from the book "Multimedia
 * Programming Interface and Data Specifications 1.0" by IBM Corporation and
 * Microsoft Corporation published in August 1991, was used.
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */

public enum INFOListChunkID {

    /**
     * Archival Location. Indicates where the subject
     * of the file is archived.
     */
    IARL (new byte[]{73, 65, 82, 76}),
    /**
     * Artist. Lists the artist of the original
     * subject of the file. For example, “ Michaelangelo.”
     */
    IART (new byte[]{73, 65, 82, 84}),
    /**
     * Commissioned. Lists the name of the person
     * or organization that commissioned the subject
     * of the file. For example, “ Pope Julian II.”
     */
    ICMS (new byte[]{73, 67, 77, 83}),
    /**
     * Comments. Provides general comments about the file or the subject of the
     * file. If the comment is several sentences long, end each sentence with a
     * period. Do not include newline characters.
     */
    ICMT (new byte[]{73, 67, 77, 84}),
    /**
     * Copyright. Records the copyright information for the file. For example,
     * “ Copyright Encyclopedia International 1991.” If there are multiple
     * copyrights, separate them by a semicolon followed by a space.
     */
    ICOP (new byte[]{73, 67, 79, 80}),
    /**
     * Creation date. Specifies the date the subject of the file was created. List dates
     * in year-month-day format, padding one-digit months and days with a zero on
     * the left. For example, “ 1553-05-03” for May 3, 1553.
     */
    ICRD (new byte[]{73, 67, 82, 68}),
    /**
     * Cropped. Describes whether an image has been cropped and, if so, how it was
     * cropped. For example, “ lower right corner.”
     */
    ICRP (new byte[]{73, 67, 82, 80}),
    /**
     * Dimensions. Specifies the size of the original subject of the file. For example,
     * “ 8.5 in h, 11 in w.”
     */
    IDIM (new byte[]{73, 68, 73, 77}),
    /**
     * Dots Per Inch. Stores dots per inch setting of the digitizer used to produce the
     * file, such as “ 300.”
     */
    IDPI (new byte[]{73, 68, 80, 73}),
    /**
     * Engineer. Stores the name of the engineer who worked on the file. If there are
     * multiple engineers, separate the names by a semicolon and a blank. For
     * example, “ Smith, John; Adams, Joe.”
     */
    IENG (new byte[]{73, 69, 78, 71}),
    /**
     * Genre. Describes the original work, such as, “ landscape,” “ portrait,” “ still
     * life,” etc.
     */
    IGNR (new byte[]{73, 71, 78, 82}),
    /**
     * Keywords. Provides a list of keywords that refer to the file or subject of the
     * file. Separate multiple keywords with a semicolon and a blank. For example,
     * “ Seattle; aerial view; scenery.”
     */
    IKEY (new byte[]{73, 75, 69, 89}),
    /**
     * Lightness. Describes the changes in lightness settings on the digitizer required
     * to produce the file. Note that the format of this information depends on
     * hardware used.
     */
    ILGT (new byte[]{73, 76, 71, 84}),
    /**
     * Medium. Describes the original subject of the file, such as, “ computer
     * image,” “ drawing,” “ lithograph,” and so forth.
     */
    IMED (new byte[]{73, 77, 69, 68}),
    /**
     * Name. Stores the title of the subject of the file, such as, “ Seattle From
     * Above.”
     */
    INAM (new byte[]{73, 78, 65, 77}),
    /**
     * Palette Setting. Specifies the number of colors requested when digitizing an
     * image, such as “ 256.”
     */
    IPLT (new byte[]{73, 80, 76, 84}),
    /**
     * Product. Specifies the name of the title the file was originally intended for,
     * such as “ Encyclopedia of Pacific Northwest Geography.”
     */
    IPRD (new byte[]{73, 80, 82, 68}),
    /**
     * Subject. Describes the conbittents of the file, such as “ Aerial view of
     * Seattle.”
     */
    ISBJ (new byte[]{73, 83, 66, 74}),
    /**
     * Software. . Identifies the name of the software package used to create the file,
     * such as “ Microsoft WaveEdit.”
     */
    ISFT (new byte[]{73, 83, 70, 84}),
    /**
     * Sharpness. Identifies the changes in sharpness for the digitizer required to
     * produce the file (the format depends on the hardware used).
     */
    ISHP (new byte[]{73, 83, 72, 80}),
    /**
     * Source. Identifies the name of the person or organization who supplied the
     * original subject of the file. For example, “ Trey Research.”
     */
    ISRC (new byte[]{73, 83, 82, 67}),
    /**
     *  Source Form. Identifies the original form of the material that was digitized,
     * such as “ slide,” “ paper,” “ map,” and so forth. This is not necessarily the
     * same as IMED.
     */
    ISRF (new byte[]{73, 83, 82, 70}),
    /**
     * Technician. Identifies the technician who digitized the subject file. For
     * example, “ Smith, John.
     */
    ITCH (new byte[]{73, 84, 67, 72}),
    ;

    private byte[] index;

    INFOListChunkID(byte[] bytes) {
        index = bytes;
    }

    /**
     * Returns an array of byte with the identifier of the metadata field.
     *
     * @return an array of byte with the identifier of the metadata field
     */
    public byte[] getByte() {
        return index;
    }

}
