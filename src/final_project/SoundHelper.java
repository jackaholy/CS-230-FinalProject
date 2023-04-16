package final_project;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundHelper {
    private SoundHelper() {
    };

    public static void playSound(String sound) {
        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    // Open an audio input stream from the sound file
                    File soundFile = new File("assets/sound/" + sound);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

                    // Get a sound clip resource from the audio input stream
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);

                    // Play the sound clip
                    clip.start();
                    Thread.sleep(clip.getMicrosecondLength() / 100);
                    clip.close();
                    audioIn.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        audioThread.start();
    }

    public static void playSoundBackground(String sound) {
        try {
            // Open an audio input stream from the sound file
            File soundFile = new File("assets/sound/" + sound);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource from the audio input stream
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Play the sound clip
            clip.start();
            clip.loop(clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
