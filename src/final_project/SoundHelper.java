package final_project;

import kuusisto.tinysound.TinySound;

import java.util.HashMap;
import java.util.Map;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;

public class SoundHelper {
    private static SoundHelper instance = null;

    public static SoundHelper getInstance() {
        if (instance == null) {
            instance = new SoundHelper();
        }
        return instance;
    }

    private Map<String, Music> musicCache = new HashMap<>();
    private Map<String, Sound> soundCache = new HashMap<>();

    private SoundHelper() {
        TinySound.init();
    }

    public Sound playSound(String soundName) {
        Sound sound;
        if (soundCache.containsKey(soundName)) {
            sound = soundCache.get(soundName);
        } else {
            sound = TinySound.loadSound("assets/sounds/" + soundName);
            soundCache.put(soundName, sound);
        }
        sound.play();
        return sound;
    }

    public Music playMusic(String musicName) {
        Music music;
        if (musicCache.containsKey(musicName)) {
            music = musicCache.get(musicName);
        } else {
            music = TinySound.loadMusic("assets/sounds/" + musicName);
            musicCache.put(musicName, music);
        }
        music.play(true);
        return music;
    }
}
