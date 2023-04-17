package final_project;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    private Map<String, AudioInputStream> soundCache = new HashMap<>();

    private SoundHelper() {
    }

    public void playSound(String sound) {
        playSound(sound, new AtomicBoolean(true), false);
    }

    public void playSound(String sound, AtomicBoolean isPlaying, boolean loop) {
        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Clip clip;
                if (soundCache.containsKey(sound)) {
                     AudioInputStream audioIn = soundCache.get(sound);
                    try {
                        audioIn.reset();
                        clip = AudioSystem.getClip();
                        clip.open(audioIn);
                    } catch (IOException | LineUnavailableException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    File soundFile = new File("assets/sound/" + sound);
                    try {
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                        audioIn.mark(0);
                        soundCache.put(sound, audioIn);
                        clip = AudioSystem.getClip();
                        clip.open(audioIn);
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                clip.setFramePosition(0);
                clip.start();
                if (loop)
                    clip.loop(clip.LOOP_CONTINUOUSLY);

                // Check if the sound should be stopped
                do {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                } while (isPlaying.get() && clip.isRunning());
                clip.stop();
            }
        });
        audioThread.start();
    }
}
