/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.util.format.readers;

import ausf.software.constants.*;
import ausf.software.constants.WAVField;
import ausf.software.file.audio.WAVFile;
import ausf.software.util.ByteBufferReader;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Implementation of the WAV file reader.
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 */
public class WAVFileReader extends WAVFile {

    /**
     * An instance of a reader with a set buffer size for reading
     */
    private ByteBufferReader reader;
    /**
     * An array of bytes of the buffer read from the file
     */
    private byte[] buffer;
    /**
     * The number of the read buffer
     */
    private int bufferPageNumber;

    /**
     * Creates an instance of a WAV file reader with the specified path to the file to be read.
     * @param path file path
     */
    public WAVFileReader(String path) {
        this.path = path;
        reader = new ByteBufferReader(path);
        read();
        reader.close();
        checkChunks();
    }

    /**
     * Reads data from a file.
     */
    private void read() {
        buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
        readHeader();
        findChunkID();
    }


    /**
     * Reads the WAV file header.
     */
    private void readHeader() {
        if(isWAVEFile() == false){
            buffer = new byte[0];
            return;
        }
        readFileSize();
    }

    /**
     * Reads the "ftm " chunk.
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
     * @param offset the chunk offset in the buffer array
     */
    private void readInfoChunk(int offset) {
        chunks.add(2, WAVContainerNameByte.CHUNK_INFO);
    }

    /**
     * Reads the field with information about the file size.
     */
    private void readFileSize() {
        fileSize = convertAreaArrayToBuffer(WAVField.RIFF_CONTAINER_SIZE.getOffset(),
                WAVField.RIFF_CONTAINER_SIZE.getFieldEnd()).getInt();
    }

    /**
     * Reads the field with information about the file data
     * compression format.
     * @param offset the field offset in the buffer array
     */
    private void readAudioCodec(int offset) {
        audioFormat = convertAreaArrayToBuffer(offset + WAVField.AUDIO_FORMAT.getOffset(),
                offset + WAVField.AUDIO_FORMAT.getFieldEnd()).getShort();
    }

    /**
     * Reads the field with information about the number of audio channels.
     * @param offset the field offset in the buffer array
     */
    private void readNumberAudioChanel(int offset) {
        numChannels = convertAreaArrayToBuffer(offset + WAVField.NUMBER_CHANNELS.getOffset(),
                offset +WAVField.NUMBER_CHANNELS.getFieldEnd()).getShort();
    }

    /**
     * Reads the field with information about the audio sample rate.
     * @param offset the field offset in the buffer array
     */
    private void readSampleRate(int offset) {
        sampleRate = convertAreaArrayToBuffer(offset + WAVField.SAMPLE_RATE.getOffset(),
                offset + WAVField.SAMPLE_RATE.getFieldEnd()).getInt();
    }

    /**
     * Reads the information field with the number of bytes transmitted per second of playback.
     * @param offset the field offset in the buffer array
     */
    private void readByteRate(int offset) {
        byteRate = convertAreaArrayToBuffer(offset + WAVField.BYTE_RATE.getOffset(),
                offset + WAVField.BYTE_RATE.getFieldEnd()).getInt();
    }

    /**
     * Read a field with information about the number of bytes for a single sample.
     * @param offset the field offset in the buffer array
     */
    private void readBlockAlign(int offset) {
        blockAlign = convertAreaArrayToBuffer(offset + WAVField.BLOCK_ALIGN.getOffset(),
                offset + WAVField.BLOCK_ALIGN.getFieldEnd()).getShort();
    }

    /**
     * Reads a field with information about the number of bits in the sample.
     * @param offset the field offset in the buffer array
     */
    private void readBitsPerSample(int offset) {
        bitsPerSample = convertAreaArrayToBuffer(offset + WAVField.BITS_PER_SAMPLE.getOffset(),
                offset + WAVField.BITS_PER_SAMPLE.getFieldEnd()).getShort();
    }

    /**
     * Returns true if the file is a WAV file.
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
     * @return true if the RIFF container is present in the file
     */
    private boolean isAvailabilityRIFF() {
        return Arrays.equals(
                        getBufferArea(WAVField.CONTAINER_RIFF.getOffset(), WAVField.CONTAINER_RIFF.getFieldEnd()),
                        WAVContainerNameByte.CONTAINER_RIFF.getByte());
    }

    /**
     * Returns true if the file format identifier is WAV.
     * @return true if the file format identifier is WAV
     */
    private boolean isAvailabilityWAVE() {
        return Arrays.equals(
                getBufferArea(WAVField.FORMAT_TAG.getOffset(), WAVField.FORMAT_TAG.getFieldEnd()),
                WAVContainerNameByte.CHUNK_WAVE.getByte());
    }

    /**
     * Returns true if the chunk identifier "data" is found by the offset in the array.
     * @param offset the field offset in the buffer array
     * @return true if the chunk identifier "data" is found by the offset in the array
     */
    private boolean isDataChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_DATA.getFieldEnd()),
                                WAVContainerNameByte.CHUNK_DATA.getByte());
    }

    /**
     * Returns true if the chunk identifier "ftm " is found by the offset in the array.
     * @param offset the field offset in the buffer array
     * @return true if the chunk identifier "ftm " is found by the offset in the array
     */
    private boolean isFTMChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_FTM.getFieldEnd()),
                                WAVContainerNameByte.CHUNK_FTM.getByte());
    }

    /**
     * Returns true if the chunk identifier "INFO" is found by the offset in the array.
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
     * Converting an array to a buffer to further convert the buffer to a number.
     * @param offset the offset of the fragment in the buffer byte array
     * @param endField the end point of the fragment in the buffer byte array
     * @return the ByteBuffer received during the conversion of the input array
     */
    private ByteBuffer convertAreaArrayToBuffer(int offset, int endField) {
        return ByteBuffer.wrap(getByteArrayField(offset, endField));
    }

    /**
     * Copy area buffer and revers array.
     * @param offset the offset of the fragment in the buffer byte array
     * @param endField the end point of the fragment in the buffer byte array
     * @return returns an inverted array from the desired one
     */
    private byte[] getByteArrayField(int offset, int endField) {
        return reverseByteArray(getBufferArea(offset, endField));
    }

    /**
     * Reads a fragment at the specified offset from the buffer byte array, and returns the read array.
     * @param offset the offset of the fragment in the buffer byte array
     * @param endArea the end point of the fragment in the buffer byte array
     * @return an array with a fragment of the buffer byte array
     */
    private byte[] getBufferArea(int offset, int endArea) {
        return Arrays.copyOfRange(buffer, offset, endArea);
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

    /**
     * Flips the incoming array
     * @param array an array that needs to be flipped
     * @return an inverted array from the source
     */
    private byte[] reverseByteArray(byte[] array) {
        byte[] tmp = new byte[array.length];
        for(int i = 0; i < array.length; i++) {
            tmp[i] = array[(array.length - 1) - i];
        }
        return tmp;
    }

}
