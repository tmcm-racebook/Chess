package com.chess.util;

import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, AudioClip> soundCache = new HashMap<>();
    private static boolean soundEnabled = true;

    public enum SoundType {
        MOVE("move"),
        CAPTURE("capture"),
        CHECK("check"),
        CHECKMATE("checkmate");

        private final String soundName;

        SoundType(String soundName) {
            this.soundName = soundName;
        }

        public String getSoundName() {
            return soundName;
        }
    }

    public static void playSound(SoundType type) {
        if (!soundEnabled) return;

        AudioClip sound = getSound(type);
        if (sound != null) {
            sound.play();
        }
    }

    private static AudioClip getSound(SoundType type) {
        String soundName = type.getSoundName();
        
        // Check cache first
        if (soundCache.containsKey(soundName)) {
            return soundCache.get(soundName);
        }

        // Try to load the sound
        try {
            String soundPath = String.format("/sounds/%s.wav", soundName);
            AudioClip sound = new AudioClip(SoundManager.class.getResource(soundPath).toString());
            soundCache.put(soundName, sound);
            return sound;
        } catch (Exception e) {
            System.out.println("Error loading sound: " + soundName + " - " + e.getMessage());
            return null;
        }
    }

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
} 