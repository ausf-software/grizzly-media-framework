// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.file.audio;

import ausf.software.constants.INFOListChunkID;
import ausf.software.constants.WAVCodecRegistries;
import ausf.software.constants.WAVField;
import ausf.software.file.AudioData;
import ausf.software.util.INFOChunkField;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WAVFile {

    protected String path;

    protected int fileSize;             // размер файла
    protected short audioFormat;        // формат сжатия
    protected short numChannels;        // количество каналов
    protected int sampleRate;           // частота дискретизации
    protected int byteRate;             // количество байт, переданных за секунду воспроизведения
    protected short blockAlign;         // количество байт для одного сэмпла, включая все каналы
    protected short bitsPerSample;      // количество бит в сэмпле

    protected int dataOffset;           // смещение области данных
    protected int dataSize;             // размер аудио данных

    protected byte[] data = new byte[0];

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

    public int getBitsPerSample() {
        return bitsPerSample;
    }

    public int getDataSize() {
        return dataSize;
    }

    public int getDataOffset() {
        return dataOffset;
    }

    public void addInfo(INFOListChunkID chunkId, String text) {
        info.add(new INFOChunkField(chunkId, text));
    }

    public List getInfoList() {
        return info;
    }

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
                            Arrays.copyOfRange(tmp, dataOffset, dataOffset + dataSize));
    }

    private int initFileSize() {
        int tmp = 0;
        if (audioFormat == WAVCodecRegistries.FORMAT_PCM.getIndex()) {
            tmp = dataSize + WAVField.CONTAINER_RIFF.getSize() + WAVField.RIFF_CONTAINER_SIZE.getSize()
                    + WAVField.FORMAT_TAG.getSize() + WAVField.CHUNK_FTM.getSize() + WAVField.CHUNK_FTM_SIZE.getSize()
                    + WAVField.AUDIO_FORMAT.getSize() + WAVField.NUMBER_CHANNELS.getSize() + WAVField.SAMPLE_RATE.getSize()
                    + WAVField.BYTE_RATE.getSize() + WAVField.BLOCK_ALIGN.getSize() + WAVField.BITS_PER_SAMPLE.getSize();

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
