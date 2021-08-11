// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WAVFile {

    protected String path;

    protected int fileSize;             // file size
    protected short audioFormat;        // compression format
    protected short numChannels;        // number of channels
    protected int sampleRate;           // sampling frequency
    protected int byteRate;             // the number of bytes transferred per second of playback
    protected short blockAlign;         // number of bytes for one sample, including all channels
    protected short bitsPerSample;      // the number of bits in the sample

    protected int dataOffset;           // data area offset
    protected int dataSize;             // audio data size

    protected byte[] data = new byte[0];

    protected List<WAVContainerNameByte> chunks;
    protected List<INFOChunkField> info = new ArrayList();

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

    public WAVFile() { }

    public String getPath() {
        return path;
    }

    public int getFileSize() {
        return fileSize;
    }

    public short getAudioCodec() {
        return audioFormat;
    }

    public short getNumberAudioChanel() {
        return numChannels;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getByteRate() {
        return byteRate;
    }

    public short getBlockAlign() {
        return blockAlign;
    }

    public short getBitsPerSample() {
        return bitsPerSample;
    }

    public int getDataSize() {
        return dataSize;
    }

    public int getDataOffset() {
        return dataOffset;
    }

    // TODO: optimize - get rid of storage in memory of two almost identical arrays
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

    public void addInfo(INFOListChunkID chunkId, String text) {
        info.add(new INFOChunkField(chunkId, text));
    }

    public List getInfoList() {
        return info;
    }

    // TODO: optimize - get rid of storage in memory of two almost identical arrays
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
