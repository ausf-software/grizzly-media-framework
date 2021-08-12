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

public class WAVFileReader extends WAVFile {

    private ByteBufferReader reader;
    private byte[] buffer;
    private int bufferPageNumber;

    public WAVFileReader(String path) {
        this.path = path;
        reader = new ByteBufferReader(path);
        read();
        reader.close();
        checkChunks();
    }

    private void read() {
        buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
        readHeader();
        findChunkID();
    }

    // read data info
    private void readHeader() {
        if(isWAVEFile() == false){
            buffer = new byte[0];
            return;
        }
        readFileSize();
    }

    private void readFTMChunk(int offset) {
        chunks.add(0, WAVContainerNameByte.CHUNK_FTM);
            readAudioCodec(offset);
            readNumberAudioChanel(offset);
            readSampleRate(offset);
            readByteRate(offset);
            readBlockAlign(offset);
            readBitsPerSample(offset);
    }
    private void readDataChunk (int offset) {
        chunks.add(1, WAVContainerNameByte.CHUNK_DATA);
        dataOffset = offset + (bufferPageNumber * BufferSizes.WAVE_BUFFER_SIZE.getSize());
        dataSize = convertAreaArrayToBuffer( offset + WAVField.CHUNK_DATA.getSize(),
                WAVField.DATA_SIZE.getFieldEnd() + offset).getInt();
        jumpBuffer();
    }
    private void readInfoChunk(int offset) {
        chunks.add(2, WAVContainerNameByte.CHUNK_INFO);
    }

    // read byte array data and converting it to a number
    private void readFileSize() {
        fileSize = convertAreaArrayToBuffer(WAVField.RIFF_CONTAINER_SIZE.getOffset(),
                WAVField.RIFF_CONTAINER_SIZE.getFieldEnd()).getInt();
    }
    private void readAudioCodec(int offset) {
        audioFormat = convertAreaArrayToBuffer(offset + WAVField.AUDIO_FORMAT.getOffset(),
                offset + WAVField.AUDIO_FORMAT.getFieldEnd()).getShort();
    }
    private void readNumberAudioChanel(int offset) {
        numChannels = convertAreaArrayToBuffer(offset + WAVField.NUMBER_CHANNELS.getOffset(),
                offset +WAVField.NUMBER_CHANNELS.getFieldEnd()).getShort();
    }
    private void readSampleRate(int offset) {
        sampleRate = convertAreaArrayToBuffer(offset + WAVField.SAMPLE_RATE.getOffset(),
                offset + WAVField.SAMPLE_RATE.getFieldEnd()).getInt();
    }
    private void readByteRate(int offset) {
        byteRate = convertAreaArrayToBuffer(offset + WAVField.BYTE_RATE.getOffset(),
                offset + WAVField.BYTE_RATE.getFieldEnd()).getInt();
    }
    private void readBlockAlign(int offset) {
        blockAlign = convertAreaArrayToBuffer(offset + WAVField.BLOCK_ALIGN.getOffset(),
                offset + WAVField.BLOCK_ALIGN.getFieldEnd()).getShort();
    }
    private void readBitsPerSample(int offset) {
        bitsPerSample = convertAreaArrayToBuffer(offset + WAVField.BITS_PER_SAMPLE.getOffset(),
                offset + WAVField.BITS_PER_SAMPLE.getFieldEnd()).getShort();
    }

    // check file
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
    private boolean isAvailabilityRIFF() {
        return Arrays.equals(
                        getBufferArea(WAVField.CONTAINER_RIFF.getOffset(), WAVField.CONTAINER_RIFF.getFieldEnd()),
                        WAVContainerNameByte.CONTAINER_RIFF.getByte());
    }
    private boolean isAvailabilityWAVE() {
        return Arrays.equals(
                getBufferArea(WAVField.FORMAT_TAG.getOffset(), WAVField.FORMAT_TAG.getFieldEnd()),
                WAVContainerNameByte.CHUNK_WAVE.getByte());
    }

    // checking the correspondence of the offset data with the chunk id
    private boolean isDataChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_DATA.getFieldEnd()),
                                WAVContainerNameByte.CHUNK_DATA.getByte());
    }
    private boolean isFTMChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_FTM.getFieldEnd()),
                                WAVContainerNameByte.CHUNK_FTM.getByte());
    }
    private boolean isInfoChunk (int offset) {
        return Arrays.equals(getBufferArea(offset, offset + WAVField.CHUNK_INFO.getSize()),
                                WAVContainerNameByte.CHUNK_INFO.getByte());
    }

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

    private void checkChunks () {
        if (chunks.get(0).equals(WAVContainerNameByte.CHUNK_FTM) == false) {
            ErrorMessage.WAV_NOT_FIND_FTM.printError();
        }
        if (chunks.get(1).equals(WAVContainerNameByte.CHUNK_DATA) == false) {
            ErrorMessage.WAV_NOT_FIND_DATA.printError();
        }
    }

    // converting an array to a buffer to further convert the buffer to a number
    private ByteBuffer convertAreaArrayToBuffer(int offset, int endField) {
        return ByteBuffer.wrap(getByteArrayField(offset, endField));
    }

    // copy area buffer and revers array
    private byte[] getByteArrayField(int offset, int endField) {
        return reverseByteArray(getBufferArea(offset, endField));
    }

    private byte[] getBufferArea(int offset, int endArea) {
        return Arrays.copyOfRange(buffer, offset, endArea);
    }

    // jump buffer page after data chunk
    private void jumpBuffer() {
        int endData = dataOffset + dataSize;

        int startInBuffer = (int) Math.floor(((double) endData) / ((double) BufferSizes.WAVE_BUFFER_SIZE.getSize()));

        for(int i = bufferPageNumber; i < startInBuffer; i++) {
            buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
            bufferPageNumber ++;
        }
    }

    // reverse byte array
    private byte[] reverseByteArray(byte[] array) {
        byte[] tmp = new byte[array.length];
        for(int i = 0; i < array.length; i++) {
            tmp[i] = array[(array.length - 1) - i];
        }
        return tmp;
    }

}
