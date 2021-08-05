// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.file;

import ausf.software.constants.*;
import ausf.software.file.audio.WAVFile;
import ausf.software.util.ByteBufferReader;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class WAVFileReader extends WAVFile {

    private ByteBufferReader reader;
    private byte[] buffer;

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
        return Arrays.equals(Arrays.copyOfRange( buffer, WAVFieldsOffset.CONTAINER_RIFF.getPosition(),
                                                            WAVFieldSize.CONTAINER_RIFF.getSize() ),
                                                                WAVContainerNameByte.CONTAINER_RIFF.getByte());
    }
    private boolean isAvailabilityWAVE() {
        return Arrays.equals(
                Arrays.copyOfRange( buffer, WAVFieldsOffset.FORMAT_TAG.getPosition(),
                WAVFieldSize.FORMAT_TAG.getSize() + WAVFieldsOffset.FORMAT_TAG.getPosition() ),
                WAVContainerNameByte.CHUNK_WAVE.getByte());
    }
    private boolean isAvailabilityFTM() {
        return Arrays.equals(
                Arrays.copyOfRange( buffer, WAVFieldsOffset.CHUNK_FTM.getPosition(),
                        WAVFieldSize.CHUNK_FTM.getSize() + WAVFieldsOffset.CHUNK_FTM.getPosition() ),
                WAVContainerNameByte.CHUNK_FTM.getByte());
    }

    // read data and converting it to a number
    private void readFileSize() {
        fileSize = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, WAVFieldsOffset.RIFF_CONTAINER_SIZE.getPosition(),
                WAVFieldSize.RIFF_CONTAINER_SIZE.getSize() + WAVFieldsOffset.RIFF_CONTAINER_SIZE.getPosition()))).getInt();
    }
    private void readAudioCodec(){
        audioFormat = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, WAVFieldsOffset.AUDIO_FORMAT.getPosition(),
                WAVFieldSize.AUDIO_FORMAT.getSize() + WAVFieldsOffset.AUDIO_FORMAT.getPosition()))).getShort();
    }
    private void readNumberAudioChanel(){
        numChannels = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, WAVFieldsOffset.NUMBER_CHANNELS.getPosition(),
                WAVFieldSize.NUMBER_CHANNELS.getSize() + WAVFieldsOffset.NUMBER_CHANNELS.getPosition()))).getShort();
    }
    private void readSampleRate(){
        sampleRate = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, WAVFieldsOffset.SAMPLE_RATE.getPosition(),
                WAVFieldSize.SAMPLE_RATE.getSize() + WAVFieldsOffset.SAMPLE_RATE.getPosition()))).getInt();
    }
    private void readByteRate(){
        byteRate = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, WAVFieldsOffset.BYTE_RATE.getPosition(),
                WAVFieldSize.BYTE_RATE.getSize() + WAVFieldsOffset.BYTE_RATE.getPosition()))).getInt();
    }
    private void readBlockAlign(){
        blockAlign = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, WAVFieldsOffset.BLOCK_ALIGN.getPosition(),
                WAVFieldSize.BLOCK_ALIGN.getSize() + WAVFieldsOffset.BLOCK_ALIGN.getPosition()))).getShort();
    }
    private void readBitsPerSample(){
        bitsPerSample = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, WAVFieldsOffset.BITS_PER_SAMPLE.getPosition(),
                WAVFieldSize.BITS_PER_SAMPLE.getSize() + WAVFieldsOffset.BITS_PER_SAMPLE.getPosition()))).getShort();
    }

    private void findDataOffset(){
        for(int i = 0; i < buffer.length; i++){
            if(Arrays.equals(Arrays.copyOfRange( buffer, i, i + WAVFieldSize.CHUNK_DATA.getSize() ),
                                WAVContainerNameByte.CHUNK_DATA.getByte())){
                dataOffset = i;
                dataSize = ByteBuffer.wrap(reverseByteArray(Arrays.copyOfRange(buffer, i + 4,
                        WAVFieldSize.DATA_SIZE.getSize() + i + 4))).getInt();
                return;
            }
        }
        if(dataOffset == 0 && reader.getRemnant() == 0){
            ErrorMessage.WAV_NOT_FIND_DATA.printError();
            return;
        }
        buffer = reader.getBuffer(BufferSizes.WAVE_BUFFER_SIZE.getSize());
        findDataOffset();
    }

    // reverse byte array to converting it to a number
    private byte[] reverseByteArray(byte[] array){
        byte[] tmp = new byte[array.length];
        for(int i = 0; i < array.length; i++){
            tmp[i] = array[(array.length - 1) - i];
        }
        return tmp;
    }

}
