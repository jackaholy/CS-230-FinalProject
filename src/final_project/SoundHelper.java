package final_project;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundHelper {
    private static SoundHelper instance = null;

    public static SoundHelper getInstance() {
        if (instance == null) {
            instance = new SoundHelper();
        }
        return instance;
    }

    private SoundHelper() {
    }

    public void playSound(String sound) {
        playSound(sound, new AtomicBoolean(true), false);
    }

    public void playSound(String sound, AtomicBoolean isPlaying, boolean loop) {
        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                File soundFile = new File("assets/sound/" + sound);
                try (AudioInputStream audioInputStream = AudioSystem
                        .getAudioInputStream(soundFile);
                        Clip clip = AudioSystem.getClip();) {
                    clip.open(audioInputStream);
                    clip.start();
                    if (loop)
                        clip.loop(Clip.LOOP_CONTINUOUSLY);

                    // Check if the sound should be stopped
                    do {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    } while (isPlaying.get() && clip.isRunning());
                    clip.stop();
                    clip.close();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        audioThread.start();
    }
}
