// Copyright © 2021 Shcherbina Daniil
// License: http://opensource.org/licenses/MIT

package ausf.software.file;

public class AudioData {

    private short audioFormat;        // compression format
    private short numChannels;        // number of channels
    private int sampleRate;           // sampling frequency
    private int byteRate;             // the number of bytes transferred per second of playback
    private short blockAlign;         // number of bytes for one sample, including all channels
    private short bitsPerSample;      // the number of bits in the sample

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
