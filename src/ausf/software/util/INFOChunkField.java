package ausf.software.util;

import ausf.software.constants.INFOListChunkID;

public class INFOChunkField {

    INFOListChunkID infoListChunkID;
    String text;

    public INFOChunkField(INFOListChunkID infoListChunkID, String text) {
        this.infoListChunkID = infoListChunkID;
        this.text = text;
    }

    public INFOListChunkID getChunkID() {
        return infoListChunkID;
    }

    public String getText() {
        return text;
    }

    public int getSize() {
        return 4 + text.length() + 1;
    }

}
