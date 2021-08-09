package ausf.software.file;

public class AudioData {

    private short audioFormat;        // формат сжатия
    private short numChannels;        // количество каналов
    private int sampleRate;           // частота дискретизации
    private int byteRate;             // количество байт, переданных за секунду воспроизведения
    private short blockAlign;         // количество байт для одного сэмпла, включая все каналы
    private short bitsPerSample;      // количество бит в сэмпле

    private byte[] data;

    public AudioData(short audioFormat, short numChannels, int sampleRate,
                     int byteRate, short blockAlign, short bitsPerSample, byte[] data) {
        this.audioFormat = audioFormat;
        this.numChannels = numChannels;
        this.sampleRate = sampleRate;
        this.byteRate = byteRate;
        this.blockAlign = blockAlign;
        this.bitsPerSample = bitsPerSample;
        this.data = data;
    }

    public int getByteRate() {
        return byteRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public short getBitsPerSample() {
        return bitsPerSample;
    }

    public short getAudioFormat() {
        return audioFormat;
    }

    public short getBlockAlign() {
        return blockAlign;
    }

    public short getNumChannels() {
        return numChannels;
    }

    public int getDataSize() {
        return data.length;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isDataCorrect() {
        return data.length != 0;
    }

}
