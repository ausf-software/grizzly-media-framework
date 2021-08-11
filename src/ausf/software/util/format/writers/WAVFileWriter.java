// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.util.format.writers;

import ausf.software.constants.WAVContainerNameByte;
import ausf.software.file.audio.WAVFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WAVFileWriter {

    private WAVFile wavFile;
    private FileOutputStream outputStream;

    public WAVFileWriter(WAVFile wavFile) {
        this.wavFile = wavFile;
        try {
            outputStream = new FileOutputStream(wavFile.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        writeHeader();
        writeFTM();
        writeDATA();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHeader() {
        try {
            outputStream.write(WAVContainerNameByte.CONTAINER_RIFF.getByte());
            outputStream.write(toBytes(Integer.reverseBytes(wavFile.getFileSize())));
            outputStream.write(WAVContainerNameByte.CHUNK_WAVE.getByte());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFTM () {
        try {
            outputStream.write(WAVContainerNameByte.CHUNK_FTM.getByte());
            outputStream.write(toBytes(Integer.reverseBytes(16)));
            outputStream.write(toBytes(Short.reverseBytes(wavFile.getAudioCodec())));
            outputStream.write(toBytes(Short.reverseBytes(wavFile.getNumberAudioChanel())));
            outputStream.write(toBytes(Integer.reverseBytes(wavFile.getSampleRate())));
            outputStream.write(toBytes(Integer.reverseBytes(wavFile.getByteRate())));
            outputStream.write(toBytes(Short.reverseBytes(wavFile.getBlockAlign())));
            outputStream.write(toBytes(Short.reverseBytes(wavFile.getBitsPerSample())));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDATA() {
        try {
            outputStream.write(WAVContainerNameByte.CHUNK_DATA.getByte());
            outputStream.write(toBytes(Integer.reverseBytes(wavFile.getDataSize())));
            outputStream.write(wavFile.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: transfer to tools
    private byte[] toBytes(short i) {
        byte[] result = new byte[2];

        result[0] = (byte) (i >> 8);
        result[1] = (byte) (i /*>> 0*/);

        return result;
    }
    private byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }
    //

}
