/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.constants;

public enum ErrorMessage {

    WAV_NOT_FIND_RIFF (new Error("File does not contain 'RIFF' container.")),
    WAV_NOT_FIND_WAVE (new Error("File ile is not a file WAVE.")),
    WAV_NOT_FIND_FTM (new Error("File does not contain 'ftm ' chunk.")),
    WAV_NOT_FIND_DATA (new Error("File does not contain 'data' chunk.")),
    ;

    private Error index;

    ErrorMessage(Error i) {
        index = i;
    }

    public void printError() {
        index.print();
    }

    static class Error {
        private String message;

        public Error(String message) {
            this.message = message;
        }

        private void print() {
            try {
                throw new Exception(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
