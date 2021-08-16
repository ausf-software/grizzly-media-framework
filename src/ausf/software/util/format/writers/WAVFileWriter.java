/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.util.format.writers;

import ausf.software.constants.WAVContainerNameByte;
import ausf.software.file.audio.WAVFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ausf.software.util.Math;

/**
 *
 * Implementation of the WAV file writer.
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public class WAVFileWriter {

    /**
     * An instance of the WAVE file being written
     */
    private WAVFile wavFile;
    /**
     * File recording stream
     */
    private FileOutputStream outputStream;

    /**
     * Creates an instance of the WAV file writer, specifying the file to be written.
     * @param wavFile the object of the WAV file to be recorded
     */
    public WAVFileWriter(WAVFile wavFile) {
        this.wavFile = wavFile;
        try {
            outputStream = new FileOutputStream(wavFile.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a WAV file.
     */
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

    /**
     * Writes the WAV file header to the file.
     */
    private void writeHeader() {
        try {
            outputStream.write(WAVContainerNameByte.CONTAINER_RIFF.getByte());
            outputStream.write(Math.toBytes(Integer.reverseBytes(wavFile.getFileSize())));
            outputStream.write(WAVContainerNameByte.CHUNK_WAVE.getByte());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the "ftm " chunk to the file.
     */
    private void writeFTM () {
        try {
            outputStream.write(WAVContainerNameByte.CHUNK_FTM.getByte());
            outputStream.write(Math.toBytes(Integer.reverseBytes(16)));
            outputStream.write(Math.toBytes(Short.reverseBytes(wavFile.getAudioCodec())));
            outputStream.write(Math.toBytes(Short.reverseBytes(wavFile.getNumberAudioChanel())));
            outputStream.write(Math.toBytes(Integer.reverseBytes(wavFile.getSampleRate())));
            outputStream.write(Math.toBytes(Integer.reverseBytes(wavFile.getByteRate())));
            outputStream.write(Math.toBytes(Short.reverseBytes(wavFile.getBlockAlign())));
            outputStream.write(Math.toBytes(Short.reverseBytes(wavFile.getBitsPerSample())));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the "data" chunk to the file.
     */
    private void writeDATA() {
        try {
            outputStream.write(WAVContainerNameByte.CHUNK_DATA.getByte());
            outputStream.write(Math.toBytes(Integer.reverseBytes(wavFile.getDataSize())));
            outputStream.write(wavFile.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
