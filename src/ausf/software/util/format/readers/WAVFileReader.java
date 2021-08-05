// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

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
        reader = new ByteBufferReader(path);
        read();
    }

    private void read() {
        buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
        readHeader();
        findDataOffset();
    }

    // read data info
    private void readHeader() {
        if(isWAVEFile() == false){
            buffer = new byte[0];
            return;
        }
        readFileSize();
        readFTMChunk();
    }

    private void readFTMChunk() {
        if(isAvailabilityFTM()) {
            readAudioCodec();
            readNumberAudioChanel();
            readSampleRate();
            readByteRate();
            readBlockAlign();
            readBitsPerSample();
        } else {
            ErrorMessage.WAV_NOT_FIND_FTM.printError();
        }
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
    private boolean isAvailabilityFTM() {
        return Arrays.equals(
                getBufferArea(WAVField.CHUNK_FTM.getOffset(), WAVField.CHUNK_FTM.getFieldEnd()),
                WAVContainerNameByte.CHUNK_FTM.getByte());
    }

    // read data and converting it to a number
    private void readFileSize() {
        fileSize = convertAreaArrayToBuffer(WAVField.RIFF_CONTAINER_SIZE.getOffset(),
                WAVField.RIFF_CONTAINER_SIZE.getFieldEnd()).getInt();
    }
    private void readAudioCodec() {
        audioFormat = convertAreaArrayToBuffer(WAVField.AUDIO_FORMAT.getOffset(),
                WAVField.AUDIO_FORMAT.getFieldEnd()).getShort();
    }
    private void readNumberAudioChanel() {
        numChannels = convertAreaArrayToBuffer(WAVField.NUMBER_CHANNELS.getOffset(),
                WAVField.NUMBER_CHANNELS.getFieldEnd()).getShort();
    }
    private void readSampleRate() {
        sampleRate = convertAreaArrayToBuffer(WAVField.SAMPLE_RATE.getOffset(),
                WAVField.SAMPLE_RATE.getFieldEnd()).getInt();
    }
    private void readByteRate() {
        byteRate = convertAreaArrayToBuffer(WAVField.BYTE_RATE.getOffset(),
                WAVField.BYTE_RATE.getFieldEnd()).getInt();
    }
    private void readBlockAlign() {
        blockAlign = convertAreaArrayToBuffer(WAVField.BLOCK_ALIGN.getOffset(),
                WAVField.BLOCK_ALIGN.getFieldEnd()).getShort();
    }
    private void readBitsPerSample() {
        bitsPerSample = convertAreaArrayToBuffer(WAVField.BITS_PER_SAMPLE.getOffset(),
                WAVField.BITS_PER_SAMPLE.getFieldEnd()).getShort();
    }
    //

    // TODO: optimize
    private void findDataOffset() {
        // search for the offset of the chunk id by iterating through the buffer
        for(int i = 0; i < buffer.length; i++) {
            if(Arrays.equals(getBufferArea(i, i + WAVField.CHUNK_DATA.getSize()),
                                WAVContainerNameByte.CHUNK_DATA.getByte())) {
                dataOffset = i + (bufferPageNumber * BufferSizes.WAVE_BUFFER_SIZE.getSize());
                dataSize = convertAreaArrayToBuffer( i + WAVField.CHUNK_DATA.getSize(),
                        WAVField.DATA_SIZE.getFieldEnd() + i).getInt();
                return;
            }
        }
        // checking for a complete file analysis
        if(dataOffset == 0 && reader.getRemnant() == 0) {
            ErrorMessage.WAV_NOT_FIND_DATA.printError();
            return;
        }
        // uploading the remaining part of the file and starting re-analysis
        buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
        findDataOffset();
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

    // reverse byte array
    private byte[] reverseByteArray(byte[] array) {
        byte[] tmp = new byte[array.length];
        for(int i = 0; i < array.length; i++) {
            tmp[i] = array[(array.length - 1) - i];
        }
        return tmp;
    }

}
