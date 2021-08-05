// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.constants;

public enum BufferSizes {

    WAVE_BUFFER_SIZE ( 10240),
    ;

    private int index;

    BufferSizes(int i) {
        index = i;
    }

    public int getSize() {
        return index;
    }
}
