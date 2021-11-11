/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.containers;

import ausf.software.constants.INFOListChunkID;

/**
 * Implementation of a container for storing information
 * about a single field of file metadata.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */

public class INFOChunkField {

    /**
     * The identifier of the metadata field.
     */
    private INFOListChunkID infoListChunkID;
    /**
     *  The text of the metadata field.
     */
    private String text;

    /**
     * Creates an object of INFOChunkField by the identifier of the metadata field and text that is in it
     *
     * @param infoListChunkID the identifier of the metadata field
     * @param text  the text of the metadata field
     */
    public INFOChunkField(INFOListChunkID infoListChunkID, String text) {
        this.infoListChunkID = infoListChunkID;
        this.text = text;
    }

    /**
     * Return the identifier of the metadata field.
     *
     * @return the identifier of the metadata field
     */
    public INFOListChunkID getChunkID() {
        return infoListChunkID;
    }

    /**
     * Return the text of the metadata field.
     *
     * @return the text of the metadata field
     */
    public String getText() {
        return text;
    }

    /**
     * Return the estimated size of the metadata field.
     *
     * @return the estimated size of the metadata field
     */
    public int getSize() {
        return 4 + text.length() + 1;
    }

}
