// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.file.audio;

import java.nio.ByteBuffer;

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

    public String getPath() {
        return path;
    }

    public int getFileSize(){
        return fileSize;
    }

    public short getAudioCodec(){
        return audioFormat;
    }

    public short getNumberAudioChanel(){
        return numChannels;
    }

    public int getSampleRate(){
        return sampleRate;
    }

    public int getByteRate(){
        return byteRate;
    }

    public int getBitsPerSample(){
        return bitsPerSample;
    }

    public int getDataSize(){
        return dataSize;
    }

    public int getDataOffset(){
        return dataOffset;
    }

}
