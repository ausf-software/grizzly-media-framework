/*
 * Copyright © 2021 Shcherbina Daniil
 * License: http://opensource.org/licenses/MIT
 */

package ausf.software.util.recorders.audio;

import ausf.software.containers.AudioData;
import ausf.software.util.Math;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author  Shcherbina Daniil
 * @since   0.1.0
 * @version 0.1.0
 * @see AudioData
 */
public class Microphone {

    /**
     * Number of audio channels.
     */
    final private int numChannels;
    /**
     * Audio sample rate.
     */
    final private int sampleRate;
    /**
     * The number of bits in the sample.
     */
    final private int bitsPerSample;

    final private boolean signed = true;
    final private boolean bigEndian = false;

    /**
     * Current recording status.
     */
    private boolean isRecord = false;

    private TargetDataLine line;
    private AudioFormat audioFormat;
    private DataLine.Info dataLineInfo;
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Creates an Microphone object with the specified audio data parameters.
     *
     * @param numChannels number of audio channels
     * @param sampleRate audio sample rate.
     * @param bitsPerSample the number of bits in the sample.
     */
    public Microphone (int numChannels, int sampleRate, int bitsPerSample) {
        this.numChannels = numChannels;
        this.sampleRate = sampleRate;
        this.bitsPerSample = bitsPerSample;
    }

    /**
     * Enables audio recording.
     */
    public void startRecord() {
        try {
            audioFormat = new AudioFormat(sampleRate, bitsPerSample, numChannels, signed, bigEndian);
            dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

            line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            line.open(audioFormat);
            line.start();

            System.out.println("Start record...");

            Thread captureThread = new Thread(new CaptureThread());
            captureThread.start();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops audio recording.
     */
    public void stopRecord() {
        isRecord = false;
        line.stop();
        line.close();
        System.out.println("Finished record");
    }

    /**
     * Returns an array of audio data bytes.
     *
     * @return an array of audio data bytes
     */
    public byte[] getDataArray() {
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Returns an AudioData object with the specified audio recording parameters and an array of audio data.
     *
     * @return an AudioData object with the specified audio recording parameters and an array of audio data
     */
    public AudioData getAudioData() {
        return new AudioData((short) 1, (byte) numChannels, sampleRate,
                                Math.getByteRate(sampleRate, Math.getBlockAlign(bitsPerSample, numChannels)),
                                Math.getBlockAlign(bitsPerSample, numChannels), (byte) bitsPerSample, getDataArray());
    }

    /**
     * Returns an instance of Audio Format with audio recording parameters.
     *
     * @return an instance of Audio Format with audio recording parameters
     */
    public AudioFormat getAudioFormat() {
        return new AudioFormat(sampleRate, bitsPerSample, numChannels, signed, bigEndian);
    }

    private class CaptureThread extends Thread {
        byte buffer[] = new byte[bitsPerSample * 1000];
        public void run(){
            byteArrayOutputStream = new ByteArrayOutputStream();
            isRecord = true;
            try{
                while(isRecord){
                    int length = line.read(buffer, 0, buffer.length);
                    if(length > 0){
                        byteArrayOutputStream.write(buffer, 0, length);
                    }
                }
                byteArrayOutputStream.close();
            }catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }

}
