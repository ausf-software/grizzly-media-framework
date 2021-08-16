/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.file.audio;

import ausf.software.constants.INFOListChunkID;
import ausf.software.constants.WAVCodecRegistries;
import ausf.software.constants.WAVContainerNameByte;
import ausf.software.constants.WAVField;
import ausf.software.file.AudioData;
import ausf.software.util.INFOChunkField;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Implementation of a structure object containing all the information
 * about the read, created, generated WAVE file. Contains the implementation
 * of all methods for primary work with the file.
 *
 * <p>Each WAVFile instance stores information about the file path,
 * the total file size, the data compression format, the number of audio channels,
 * the number of bytes transmitted per second of playback, the number of bytes for one sample,
 * the number of bits in the sample contained in the WAV chunk file.
 *
 * <p>Note that working with the object is not synchronized.
 * If several threads access an instance at the same time and at least one of the
 * threads changes the structure of the list, it must be synchronized from the outside.
 *
 * @author  Shcherbina Daniil
 * @see     ausf.software.util.format.writers.WAVFileWriter
 * @see     ausf.software.util.format.readers.WAVFileReader
 * @see     AudioData
 * @since   0.1.0
 * @version 0.1.0
 */

public class WAVFile {

    /**
     * File path.
     */
    protected String path;

    /**
     * Total file size.
     */
    protected int fileSize;
    /**
     * Data compression format registration index.
     */
    protected short audioFormat;
    /**
     * Number of audio channels.
     */
    protected short numChannels;
    /**
     * Audio sample rate.
     */
    protected int sampleRate;
    /**
     * The number of bytes transferred per second of playback.
     */
    protected int byteRate;
    /**
     * The number of bytes for one sample, including all channels.
     */
    protected short blockAlign;
    /**
     * The number of bits in the sample.
     */
    protected short bitsPerSample;

    /**
     *  The value of the offset of the audio data chunk in the file..
     */
    protected int dataOffset;
    /**
     * The size of the audio data chunk of the file.
     */
    protected int dataSize;

    /**
     * An array of audio data bytes.
     */
    protected byte[] data = new byte[0];

    /**
     * A list containing objects of WAV chunk identifiers found in the read file.
     */
    protected List<WAVContainerNameByte> chunks = new ArrayList<>();
    /**
     * A list containing objects of the INFOChunkField type containing the file metadata.
     */
    protected List<INFOChunkField> info = new ArrayList();

    /**
     * Creates a WAV file with the specified path from an audio data object
     *
     * @param path path to the file
     * @param audioData audio data to be placed in the file
     */
    public WAVFile(String path, AudioData audioData) {
        this.path = path;
        audioFormat = audioData.getAudioFormat();
        numChannels = audioData.getNumChannels();
        sampleRate = audioData.getSampleRate();
        byteRate = audioData.getByteRate();
        blockAlign = audioData.getBlockAlign();
        bitsPerSample = audioData.getBitsPerSample();
        data = audioData.getData();
        dataSize = audioData.getDataSize();
        fileSize = initFileSize();
    }

    /**
     * Creates an empty WAV file with standard settings
     */
    public WAVFile() {
        audioFormat = 1;
        numChannels = 1;
        fileSize = initFileSize();
    }

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

    /**
     * Returns data compression format registration index.
     *
     * @return data compression format registration index
     */
    public short getAudioCodec() {
        return audioFormat;
    }

    /**
     * Returns number of audio channels.
     *
     * @return number of audio channels
     */
    public short getNumberAudioChanel() {
        return numChannels;
    }

    /**
     * Returns audio sample rate.
     *
     * @return audio sample rate
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Returns the number of bytes transferred per second of playback.
     *
     * @return the number of bytes transferred per second of playback
     */
    public int getByteRate() {
        return byteRate;
    }

    /**
     * Returns the number of bytes for one sample.
     *
     * @return the number of bytes for one sample
     */
    public short getBlockAlign() {
        return blockAlign;
    }

    /**
     * Returns the number of bits in the sample.
     *
     * @return the number of bits in the sample
     */
    public short getBitsPerSample() {
        return bitsPerSample;
    }

    /**
     * Returns the size of the audio data chunk of the file.
     *
     * @return the size of the audio data chunk of the file
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * Returns the value of the offset of the audio data chunk in the file.
     *
     * @return the value of the offset of the audio data chunk in the file
     */
    public int getDataOffset() {
        return dataOffset;
    }

    // TODO: optimize - get rid of storage in memory of two almost identical arrays
    /**
     * Returns an array of audio data bytes.
     *
     * @return an array of audio data bytes
     */
    public byte[] getData() {
        if (data.length == 0) {
            byte[] tmp = new byte[0];
            try {
                FileInputStream inputStream = new FileInputStream(path);
                tmp = new byte[inputStream.available()];
                inputStream.read(tmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            data = Arrays.copyOfRange(tmp, dataOffset + 8, dataOffset + dataSize);
            tmp = new byte[0];
        }
        return data;
    }

    /**
     * Adds a field with information about the file.
     *
     * @param chunkId id of the type of line to add with information about the file
     * @param text the text of the file information field to be added
     */
    public void addInfo(INFOListChunkID chunkId, String text) {
        info.add(new INFOChunkField(chunkId, text));
    }

    /**
     * Returns a list containing objects of the INFOChunkField type containing the file metadata.
     *
     * @return a list containing objects of the INFOChunkField type containing the file metadata
     *
     * @see INFOChunkField
     */
    public List getInfoList() {
        return info;
    }

    // TODO: optimize - get rid of storage in memory of two almost identical arrays
    /**
     * Converts a WAV File type object to an Audio Data type object and returns it.
     *
     * @return an object of the AudioData type created from the WAVFile object
     *
     * @see AudioData
     */
    public AudioData getAudioData() {
        byte[] tmp = new byte[0];
        try {
            FileInputStream inputStream = new FileInputStream(path);
            tmp = new byte[inputStream.available()];
            inputStream.read(tmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AudioData(audioFormat, numChannels, sampleRate, byteRate, blockAlign, bitsPerSample,
                            Arrays.copyOfRange(tmp, dataOffset + 8, dataOffset + dataSize));
    }

    /**
     * calculates the estimated file size.
     */
    private int initFileSize() {
        int tmp = 0;
        if (audioFormat == WAVCodecRegistries.FORMAT_PCM.getIndex()) {
            tmp = dataSize + WAVField.CONTAINER_RIFF.getSize() + WAVField.RIFF_CONTAINER_SIZE.getSize()
                    + WAVField.FORMAT_TAG.getSize() + WAVField.CHUNK_FTM.getSize() + WAVField.CHUNK_FTM_SIZE.getSize()
                    + WAVField.AUDIO_FORMAT.getSize() + WAVField.NUMBER_CHANNELS.getSize() + WAVField.SAMPLE_RATE.getSize()
                    + WAVField.BYTE_RATE.getSize() + WAVField.BLOCK_ALIGN.getSize() + WAVField.BITS_PER_SAMPLE.getSize()
                    + WAVField.CHUNK_DATA.getSize() + WAVField.DATA_SIZE.getSize();

            if (info.size() != 0) {
                tmp += WAVField.CHUNK_INFO.getSize() + WAVField.LIST_CONTAINER.getSize();
                for (int i = 0; i < info.size(); i++) {
                    tmp += info.get(i).getSize();
                }
            }

        }

        return tmp;
    }

}
