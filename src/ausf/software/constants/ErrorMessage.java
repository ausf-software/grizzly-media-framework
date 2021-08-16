/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants;

/**
 * Storage of constant values of the error message text.
 *
 * @see Error
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */

public enum ErrorMessage {

    /**
     * Error message if the identifier of the RIFF container is not found
     */
    WAV_NOT_FIND_RIFF (new Error("File does not contain 'RIFF' container.")),
    /**
     * Error message if the WAV file format identifier is not found.
     */
    WAV_NOT_FIND_WAVE (new Error("File ile is not a file WAVE.")),
    /**
     * Error message if the chunk identifier "ftm "is not found.
     */
    WAV_NOT_FIND_FTM (new Error("File does not contain 'ftm ' chunk.")),
    /**
     * Error message if the chunk identifier "data"is not found.
     */
    WAV_NOT_FIND_DATA (new Error("File does not contain 'data' chunk.")),
    ;

    private Error index;

    ErrorMessage(Error i) {
        index = i;
    }

    /**
     * Calls Exception with the error text.
     */
    public void printError() {
        index.print();
    }

    /**
     * Implementation of an object with the error message text.
     * Contains an implementation of the Exception call with the error text.
     * @see ErrorMessage
     */
    static class Error {
        /**
         * Error message
         */
        private String message;

        /**
         * Creates an object with the specified error text.
         * @param message the text of the error message
         */
        public Error(String message) {
            this.message = message;
        }

        /**
         * Calls Exception with the error text.
         */
        private void print() {
            try {
                throw new Exception(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Returns the text of the error message.
         * @return the text of the error message
         */
        private String getMessage() {
            return message;
        }

    }

}
