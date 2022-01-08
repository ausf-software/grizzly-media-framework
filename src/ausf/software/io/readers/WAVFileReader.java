/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.io.readers;

import ausf.software.constants.*;
import ausf.software.constants.audio.wav.WAVContainerNameByte;
import ausf.software.constants.audio.wav.WAVField;
import ausf.software.containers.INFOChunkField;
import ausf.software.file.audio.WAVFile;
import ausf.software.io.Reader;
import ausf.software.util.ByteBufferReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the WAV file reader.
 *
 * @see     Reader
 * @see     WAVFile
 * @see     ausf.software.file.audio.WAVFile.WAVFileBuilder
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public class WAVFileReader extends Reader {

    /**
     * A list containing objects of WAV chunk identifiers found in the read file.
     */
    protected List<WAVContainerNameByte> chunks = new ArrayList<>();
    /**
     * A list containing objects of the INFOChunkField type containing the file metadata.
     */
    protected List<INFOChunkField> infoFields = new ArrayList();

    /**
     * Size of the audio data array
     */
    private int dataSize;

    /**
     *  The value of the offset of the audio data chunk in the file.
     */
    private int dataOffset;

    private WAVFile.WAVFileBuilder wavFileBuilder;

    /**
     * Creates an instance of a WAV file reader with the specified path to the file to be read.
     *
     * @param path file path
     */
    public WAVFileReader(String path) {
        this.path = path;
        reader = new ByteBufferReader(path);
        wavFileBuilder = new WAVFile.WAVFileBuilder();
    }

    /**
     * Read data from a file and returns a WAVFile object.
     *
     * @return returns a WAV file object created based on the read data
     */
    public WAVFile read() {
        buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
        if(checkHeader()){
            readFileSize();
            findChunkID();
            reader.close();
        }
        checkChunks();

        wavFileBuilder = wavFileBuilder.chunks(chunks);
        wavFileBuilder = wavFileBuilder.infoFields(infoFields);
        wavFileBuilder = wavFileBuilder.filePath(path);
        wavFileBuilder = wavFileBuilder.dataOffset(dataOffset);
        wavFileBuilder = wavFileBuilder.dataSize(dataSize);

        return wavFileBuilder.build();
    }

    /**
     * Reads and checks the WAV file header, returns
     * true if the header is present and has all the necessary data.
     *
     * @return true if the header is present and has all the necessary data
     */
    private boolean checkHeader() {
        if(isWAVEFile() == false){
            buffer = new byte[0];
            return false;
        }
        return true;
    }

    /**
     * Reads the "ftm " chunk.
     *
     * @param offset the chunk offset in the buffer array
     */
    private void readFTMChunk(int offset) {
        chunks.add(0, WAVContainerNameByte.CHUNK_FTM);
            readAudioCodec(offset);
            readNumberAudioChanel(offset);
            readSampleRate(offset);
            readByteRate(offset);
            readBlockAlign(offset);
            readBitsPerSample(offset);
    }

    /**
     * Reads the "data" chunk.
     *
     * @param offset the chunk offset in the buffer array
     */
    private void readDataChunk (int offset) {
        chunks.add(1, WAVContainerNameByte.CHUNK_DATA);
        dataOffset = offset + (bufferPageNumber * BufferSizes.WAVE_BUFFER_SIZE.getSize());
        dataSize = convertAreaArrayToBuffer( offset + WAVField.CHUNK_DATA.getSize(),
                WAVField.DATA_SIZE.getFieldEnd() + offset).getInt();
        jumpBuffer();
    }

    /**
     * Reads the "INFO" chunk.
     *
     * @param offset the chunk offset in the buffer array
     */
    private void readInfoChunk(int offset) {
        chunks.add(2, WAVContainerNameByte.CHUNK_INFO);
    }

    /**
     * Reads the field with information about the file size.
     */
    private void readFileSize() {
        wavFileBuilder = wavFileBuilder.fileSize(
                            convertAreaArrayToBuffer(WAVField.RIFF_CONTAINER_SIZE.getOffset(),
                                                        WAVField.RIFF_CONTAINER_SIZE.getFieldEnd()).getInt());
    }

    /**
     * Reads the field with information about the file data
     * compression format.
     *
     * @param offset the field offset in the buffer array
     */
    private void readAudioCodec(int offset) {
        wavFileBuilder = wavFileBuilder.fileAudioFormat(
                            convertAreaArrayToBuffer(offset + WAVField.AUDIO_FORMAT.getOffset(),
                                                    offset + WAVField.AUDIO_FORMAT.getFieldEnd()).getShort());
    }

    /**
     * Reads the field with information about the number of audio channels.
     *
     * @param offset the field offset in the buffer array
     */
    private void readNumberAudioChanel(int offset) {
        wavFileBuilder = wavFileBuilder.numChannels(
                            convertAreaArrayToBuffer(offset + WAVField.NUMBER_CHANNELS.getOffset(),
                                                    offset +WAVField.NUMBER_CHANNELS.getFieldEnd()).getShort());
    }

    /**
     * Reads the field with information about the audio sample rate.
     *
     * @param offset the field offset in the buffer array
     */
    private void readSampleRate(int offset) {
        wavFileBuilder = wavFileBuilder.fileSampleRate(
                            convertAreaArrayToBuffer(offset + WAVField.SAMPLE_RATE.getOffset(),
                                                    offset + WAVField.SAMPLE_RATE.getFieldEnd()).getInt());
    }

    /**
     * Reads the information field with the number of bytes transmitted per second of playback.
     *
     * @param offset the field offset in the buffer array
     */
    private void readByteRate(int offset) {
        wavFileBuilder = wavFileBuilder.byteRate(
                            convertAreaArrayToBuffer(offset + WAVField.BYTE_RATE.getOffset(),
                                                    offset + WAVField.BYTE_RATE.getFieldEnd()).getInt());
    }

    /**
     * Read a field with information about the number of bytes for a single sample.
     *
     * @param offset the field offset in the buffer array
     */
    private void readBlockAlign(int offset) {
        wavFileBuilder = wavFileBuilder.blockAlign(
                            convertAreaArrayToBuffer(offset + WAVField.BLOCK_ALIGN.getOffset(),
                                                    offset + WAVField.BLOCK_ALIGN.getFieldEnd()).getShort());
    }

    /**
     * Reads a field with information about the number of bits in the sample.
     *
     * @param offset the field offset in the buffer array
     */
    private void readBitsPerSample(int offset) {
        wavFileBuilder = wavFileBuilder.bitsPerSample(
                            convertAreaArrayToBuffer(offset + WAVField.BITS_PER_SAMPLE.getOffset(),
                                                    offset + WAVField.BITS_PER_SAMPLE.getFieldEnd()).getShort());
    }

    /**
     * Returns true if the file is a WAV file.
     *
     * @return true if the file is a WAV file
     */
    private boolean isWAVEFile() {
        if(isAvailabilityRIFF() == false) {
            ErrorMessage.WAV_NOT_FIND_RIFF.printError();
            return false;
        }
        if(isAvailabilityWAVE() == false) {
            ErrorMessage.WAV_NOT_FIND_WAVE.printError();
            return false;
        }
        return true;
    }

    /**
     * Returns true if the RIFF container is present in the file.
     *
     * @return true if the RIFF container is present in the file
     */
    private boolean isAvailabilityRIFF() {
        return Arrays.equals(
                        getBufferArea(WAVField.CONTAINER_RIFF.getOffset(), WAVField.CONTAINER_RIFF.getFieldEnd()),
                        WAVContainerNameByte.CONTAINER_RIFF.getByte());
    }

    /**
     * Returns true if the file format identifier is WAV.
     *
     * @return true if the file format identifier is WAV
     */
    private boolean isAvailabilityWAVE() {
        return Arrays.equals(
                getBufferArea(WAVField.FORMAT_TAG.getOffset(), WAVField.FORMAT_TAG.getFieldEnd()),
                WAVContainerNameByte.CHUNK_WAVE.getByte());
    }

    /**
     * Returns true if the chunk identifier "data" is found by the offset in the array.
     *
     * @param offset the field offset in the buffer array
     * @return true if the chunk identifier "data" is found by the offset in the array
     */
    private boolean isDataChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_DATA.getFieldEnd()),
                                WAVContainerNameByte.CHUNK_DATA.getByte());
    }

    /**
     * Returns true if the chunk identifier "ftm " is found by the offset in the array.
     *
     * @param offset the field offset in the buffer array
     * @return true if the chunk identifier "ftm " is found by the offset in the array
     */
    private boolean isFTMChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_FTM.getFieldEnd()),
                                WAVContainerNameByte.CHUNK_FTM.getByte());
    }

    /**
     * Returns true if the chunk identifier "INFO" is found by the offset in the array.
     *
     * @param offset the field offset in the buffer array
     * @return true if the chunk identifier "INFO" is found by the offset in the array
     */
    private boolean isInfoChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_INFO.getSize()),
                                WAVContainerNameByte.CHUNK_INFO.getByte());
    }

    /**
     * Searches for chunk identifiers in the file.
     */
    private void findChunkID () {
        // search for the offset of the chunk id by iterating through the buffer
        for(int i = 0; i < buffer.length; i++) {
            if (isFTMChunk(i)) {
                readFTMChunk(i);
            }
            if (isDataChunk(i)) {
                readDataChunk(i);
                i = 0;
            }
            if (isInfoChunk(i)) {
                readInfoChunk(i);
            }
        }
        // checking for a complete file analysis
        if(reader.getRemnant() == 0) {
            return;
        }
        // uploading the remaining part of the file and starting re-analysis
        buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
        bufferPageNumber++;
        findChunkID();
    }

    /**
     * Checks for the presence of all required chunks.
     */
    private void checkChunks () {
        if (chunks.get(0).equals(WAVContainerNameByte.CHUNK_FTM) == false) {
            ErrorMessage.WAV_NOT_FIND_FTM.printError();
        }
        if (chunks.get(1).equals(WAVContainerNameByte.CHUNK_DATA) == false) {
            ErrorMessage.WAV_NOT_FIND_DATA.printError();
        }
    }

    /**
     * Jump buffer page after data chunk
     */
    private void jumpBuffer() {
        int endData = dataOffset + dataSize;

        int startInBuffer = (int) Math.floor(((double) endData) / ((double) BufferSizes.WAVE_BUFFER_SIZE.getSize()));

        for(int i = bufferPageNumber; i < startInBuffer; i++) {
            buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
            bufferPageNumber ++;
        }
    }


}
