/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.file;

/**
 *
 * Implementation of an abstract object containing a
 * standard set of audio parameters, as well as a
 * string with a file path.
 *
 * @author  Shcherbina Daniil
 * @see     Audio
 * @since   0.1.0
 * @version 0.1.0
 */
public abstract class AudioFile extends Audio {

    /**
     * File path.
     */
    protected String path;
    /**
     * Total file size.
     */
    protected int fileSize;

    /**
     * Returns the path to the file.
     *
     * @return the path to the file
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns file size.
     *
     * @return file size
     */
    public int getFileSize() {
        return fileSize;
    }
}
