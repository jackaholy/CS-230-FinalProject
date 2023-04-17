package final_project;

import java.io.File;    
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundHelper {
    private SoundHelper() {
    };

    public static void playSound(String sound) {
        playSound(sound, new AtomicBoolean(true), false);
    }

    public static void playSound(String sound, AtomicBoolean isPlaying, boolean loop) {
        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                File soundFile = new File("assets/sound/" + sound);
                try (
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                        Clip clip = AudioSystem.getClip();) {
                    // Open an audio input stream from the sound file

                    // Get a sound clip resource from the audio input stream
                    clip.open(audioIn);

                    // Play the sound clip
                    clip.start();
                    if (loop)
                        clip.loop(clip.LOOP_CONTINUOUSLY);

                    // Check if the sound should be stopped
                    while (isPlaying.get() && clip.getFramePosition() < clip.getFrameLength()) {
                        Thread.sleep(100);
                    }
                    clip.stop();
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
