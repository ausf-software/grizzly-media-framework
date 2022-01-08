package ausf.software.util.player.audio;

import ausf.software.containers.AudioData;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AudioPlayer {

    private AudioFormat audioFormat;

    private AudioInputStream audioInputStream;
    private SourceDataLine sourceDataLine;

    private byte[] data;

    public AudioPlayer(byte[] data, int sampleRate, int bitsPerSample,
                       int numChannels, boolean signed, boolean bigEndian) throws LineUnavailableException {
        this.data = data;
        setAudioFormat(sampleRate, bitsPerSample, numChannels, signed, bigEndian);
    }

    public AudioPlayer(AudioData audioData){
        data = audioData.getData();
        setAudioFormat(audioData.getSampleRate(), audioData.getBitsPerSample(),
                        audioData.getNumberAudioChanel(), true, false);
    }

    public void setAudioFormat(int sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian){
        audioFormat = new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    public void playAudio() {
        try{
            InputStream byteArrayInputStream = new ByteArrayInputStream(data);

            audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat,
                                                data.length/audioFormat.getFrameSize());

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            Thread playThread = new Thread(new AudioPlayer.PlayThread());
            playThread.start();

        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    class PlayThread extends Thread{
        byte tempBuffer[] = new byte[10000];

        public void run(){
            try{
                int cnt;

                while((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1){
                    if(cnt > 0){
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }

                sourceDataLine.drain();
                sourceDataLine.close();
            }catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

}

