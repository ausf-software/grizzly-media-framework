/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.file.audio;

import ausf.software.constants.INFOListChunkID;
import ausf.software.constants.audio.wav.WAVCodecRegistries;
import ausf.software.constants.audio.wav.WAVContainerNameByte;
import ausf.software.constants.audio.wav.WAVField;
import ausf.software.containers.AudioData;
import ausf.software.file.AudioFile;
import ausf.software.containers.INFOChunkField;
import ausf.software.util.Math;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Implementation of a structure object containing all the information
 * about the read, created, generated WAVE file. Contains the implementation
 * of all methods for primary work with the file.
 *
 * <p>Each WAVFile instance stores information about: the path to the file;
 * the total file size; the data compression format; the number of audio channels;
 * the number of bytes transmitted per second during playback; the number of bytes
 * for one sample; the number of bits in the sample; chunks contained in the file;
 * a list of metadata fields.
 *
 * <p>An object of this type is created using WAVFileBuilder.
 *
 * <p>Note that working with the object is not synchronized.
 * If several threads access an instance at the same time and at least one of the
 * threads changes the structure of the list, it must be synchronized from the outside.
 *
 * @author  Shcherbina Daniil
 * @see     ausf.software.io.writers.WAVFileWriter
 * @see     ausf.software.io.readers.WAVFileReader
 * @see     AudioData
 * @see     WAVFileBuilder
 * @see     AudioFile
 * @since   0.1.0
 * @version 0.1.0
 */

public class WAVFile extends AudioFile {

    /**
     *  The value of the offset of the audio data chunk in the file.
     */
    protected int dataOffset;

    /**
     * A list containing objects of WAV chunk identifiers found in the read file.
     */
    protected List<WAVContainerNameByte> chunks = new ArrayList<>();
    /**
     * A list containing objects of the INFOChunkField type containing the file metadata.
     */
    protected List<INFOChunkField> infoFields = new ArrayList();

    /**
     * Creates a WAV file using an instance of the specified builder class.
     *
     * @param builder an instance builder class
     */
    private WAVFile(WAVFileBuilder builder) {

        fileSize = builder.fileSize;
        audioFormat = builder.audioFormat;
        numChannels = builder.numChannels;
        sampleRate = builder.sampleRate;
        byteRate = builder.byteRate;
        blockAlign = builder.blockAlign;
        bitsPerSample = builder.bitsPerSample;
        chunks = builder.chunks;
        infoFields = builder.infoFields;

        if (!builder.filePath.equals("") || builder.filePath != null) {
            path = builder.filePath;
            dataOffset = builder.dataOffset;
        } else {
                data = builder.data;
                fileSize = initFileSize();
        }
    }

    // without this, the code will fall  :D
    public WAVFile(){}

    /**
     * Returns an instance WAVFileBuilder.
     *
     * @return an instance WAVFileBuilder
     */
    public static WAVFileBuilder builder() {
        return new WAVFileBuilder();
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
    @Override
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
            data = Arrays.copyOfRange(tmp, dataOffset + 8, dataOffset + data.length);
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
        infoFields.add(new INFOChunkField(chunkId, text));
    }

    /**
     * Returns a list containing objects of the INFOChunkField type containing the file metadata.
     *
     * @return a list containing objects of the INFOChunkField type containing the file metadata
     * @see INFOChunkField
     */
    public List getInfoList() {
        return infoFields;
    }

    // TODO: optimize - get rid of storage in memory of two almost identical arrays
    /**
     * Converts a WAV File type object to an Audio Data type object and returns it.
     *
     * @return an object of the AudioData type created from the WAVFile object
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
                            Arrays.copyOfRange(tmp, dataOffset + 8, dataOffset + data.length));
    }

    /**
     * Returns calculated the estimated file size.
     *
     * @return calculated the estimated file size
     */
    //TODO: review algorithm
    private int initFileSize() {
        int tmp = 0;
        if (audioFormat == WAVCodecRegistries.FORMAT_PCM.getIndex()) {
            tmp = data.length + WAVField.CONTAINER_RIFF.getSize() + WAVField.RIFF_CONTAINER_SIZE.getSize()
                    + WAVField.FORMAT_TAG.getSize() + WAVField.CHUNK_FTM.getSize() + WAVField.CHUNK_FTM_SIZE.getSize()
                    + WAVField.AUDIO_FORMAT.getSize() + WAVField.NUMBER_CHANNELS.getSize() + WAVField.SAMPLE_RATE.getSize()
                    + WAVField.BYTE_RATE.getSize() + WAVField.BLOCK_ALIGN.getSize() + WAVField.BITS_PER_SAMPLE.getSize()
                    + WAVField.CHUNK_DATA.getSize() + WAVField.DATA_SIZE.getSize();

            if (infoFields.size() != 0) {
                tmp += WAVField.CHUNK_INFO.getSize() + WAVField.LIST_CONTAINER.getSize();
                for (int i = 0; i < infoFields.size(); i++) {
                    tmp += infoFields.get(i).getSize();
                }
            }

        }

        return tmp;
    }


    /**
     * Sequence of WAV file data. This class provides an API for
     * creating a WAVFile, but without a synchronization guarantee.
     *
     * <p>The main operations in WAVFileBuilder are methods for adding
     * WAV file data to the builder class. Each of them adds data to the
     * builder and returns a new instance containing all the data. To build
     * a WAV file, the appropriate method is used, which will create an
     * instance of WAVFile, provided that this is possible.
     *
     * <p>WAV File Builder is not safe for use by multiple threads.
     *
     * @author  Shcherbina Daniil
     * @see     ausf.software.io.writers.WAVFileWriter
     * @see     ausf.software.io.readers.WAVFileReader
     * @see     WAVFile
     * @since   0.1.0
     * @version 0.1.0
     */
    public static class WAVFileBuilder {

        /**
         * File path.
         */
        private String filePath;
        /**
         * Total file size.
         */
        private int fileSize;

        /**
         * Audio data to be placed in the file
         */
        private AudioData audioData;

        /**
         * Data compression format registration index
         */
        private short audioFormat;
        /**
         * Number of audio channels.
         */
        private short numChannels;
        /**
         * Audio sample rate.
         */
        private int sampleRate;
        /**
         * The number of bytes transferred per second of playback.
         */
        private int byteRate;
        /**
         * The number of bytes for one sample, including all channels.
         */
        private short blockAlign;
        /**
         * The number of bits in the sample.
         */
        private short bitsPerSample;

        /**
         * An array of audio data bytes.
         */
        private byte[] data;

        /**
         *  The value of the offset of the audio data chunk in the file..
         */
        private int dataOffset;

        /**
         * A list containing objects of WAV chunk identifiers found in the read file.
         */
        private List<WAVContainerNameByte> chunks = new ArrayList<>();
        /**
         * A list containing objects of the INFOChunkField type containing the file metadata.
         */
        private List<INFOChunkField> infoFields = new ArrayList();

        /**
         * Returns an instance of the WAVFileBuilder class with the modified file path field
         *
         * @return an instance of the WAVFileBuilder class with the modified file path field
         * @param filePath file path
         */
        public WAVFileBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified file size field
         *
         * @return an instance of the WAVFileBuilder class with the modified file size field
         * @param fileSize file size
         */
        public WAVFileBuilder fileSize(int fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified field
         * with the data compression ID
         *
         * @return an instance of the WAVFileBuilder class with the modified field
         * with the data compression ID
         * @param audioFormat data compression ID
         */
        public WAVFileBuilder fileAudioFormat(short audioFormat) {
            this.audioFormat = audioFormat;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified field
         * with the number of audio channels
         *
         * @return an instance of the WAVFileBuilder class with the modified field
         * with the number of audio channels
         * @param numChannels number of audio channels
         */
        public WAVFileBuilder numChannels(short numChannels) {
            this.numChannels = numChannels;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified sampling rate field
         *
         * @return an instance of the WAVFileBuilder class with the modified sampling rate field
         * @param sampleRate sampling rate
         */
        public WAVFileBuilder fileSampleRate(int sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified field
         * number of bytes transmitted per second of playback
         *
         * @return an instance of the WAVFileBuilder class with the modified field
         * number of bytes transmitted per second of playback
         * @param byteRate number of bytes transferred per second of playback
         */
        public WAVFileBuilder byteRate(int byteRate) {
            this.byteRate = byteRate;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified file size field
         * number of bytes for one sample
         *
         * @return an instance of the WAVFileBuilder class with the modified file size field
         * number of bytes for one sample
         * @param blockAlign number of bytes for one sample
         */
        public WAVFileBuilder blockAlign(short blockAlign) {
            this.blockAlign = blockAlign;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified file size field
         * number of bits in the sample
         *
         * @return an instance of the WAVFileBuilder class with the modified file size field
         * number of bits in the sample
         * @param bitsPerSample number of bits in the sample
         */
        public WAVFileBuilder bitsPerSample(short bitsPerSample) {
            this.bitsPerSample = bitsPerSample;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified array of audio data bytes
         *
         * @return an instance of the WAVFileBuilder class with the modified array of audio data bytes
         * @param data array of audio data bytes
         */
        public WAVFileBuilder dataArray(byte[] data) {
            this.data = data;
            return this;
        }

        /**
         * Returns an instance of WAV File Builder with the changed offset position in the audio data file
         *
         * @return returns an instance of WAV File Builder with the changed offset position in the audio data file
         * @param dataOffset offset position in the audio data file
         */
        public WAVFileBuilder dataOffset(int dataOffset) {
            this.dataOffset = dataOffset;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified array list of all chunks
         *
         * @return an instance of the WAVFileBuilder class with the modified list of all chunks
         * @param chunks list of all chunks
         */
        public WAVFileBuilder chunks(List<WAVContainerNameByte> chunks) {
            this.chunks = chunks;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified list
         * of INFO chunk information fields
         *
         * @return an instance of the WAVFileBuilder class with the modified list
         * of INFO chunk information fields
         * @param infoFields list of INFO chunk information fields
         */
        public WAVFileBuilder infoFields(List<INFOChunkField> infoFields) {
            this.infoFields = infoFields;
            return this;
        }

        /**
         * Returns an instance of the WAVFileBuilder class with the modified field containing
         * a container with all information about audio data
         *
         * @return an instance of the WAVFileBuilder class with the modified field containing
         * a container with all information about audio data
         * @param audioData a container with all information about audio data
         */
        public WAVFileBuilder audioDataObject(AudioData audioData) {
            this.audioData = audioData;
            return this;
        }

        /**
         * Returns the collected WAV file.
         *
         * @return the collected WAV file
         */
        public WAVFile build() {
            WAVFile file = null;
            if(isValid()) {
                if(audioData != null) {
                    sampleRate = audioData.getSampleRate();
                    blockAlign = audioData.getBlockAlign();
                    byteRate = audioData.getByteRate();
                    audioFormat = audioData.getAudioCodec();
                    data = audioData.getData();
                    bitsPerSample = audioData.getBitsPerSample();
                    numChannels = audioData.getNumberAudioChanel();
                    audioData = null;
                }
                if(blockAlign == 0) {
                    blockAlign = Math.getBlockAlign(bitsPerSample, numChannels);
                }
                if(byteRate == 0) {
                    byteRate = Math.getByteRate(sampleRate, blockAlign);
                }
                if(data == null){
                    data = new byte[0];
                }

                file = new WAVFile(this);
            }
            return file;
        }

        /**
         * Returns true if WAV file assembly is possible.
         *
         * @return true if WAV file assembly is possible
         */
        private boolean isValid(){
            if(numChannels != 0 && sampleRate != 0 && bitsPerSample != 0 && audioFormat != 0) {
                return true;
            }
            if(audioData != null) {
                return true;
            }
            return false;
        }

    }

}
