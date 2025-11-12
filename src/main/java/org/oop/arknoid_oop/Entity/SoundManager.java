package org.oop.arknoid_oop.Entity;
// SoundManager.java
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
public class SoundManager {
    private static final SoundManager instance = new SoundManager();
    private HashMap<String, AudioClip> sfxCache;
    private MediaPlayer backgroundMusicPlayer;
    private double globalVolume = 0.3;
    private boolean isMuted = false;
    private SoundManager() {
        sfxCache = new HashMap<>();
        loadSound("bounce", "/sounds/bounce.mp3");
        loadSound("brickBreak", "/sounds/brickBreak.mp3");
        loadSound("brickUnbreakable", "/sounds/brickUnbreakable.mp3");
        loadSound("death", "/sounds/death.mp3");
        loadMusic("/sounds/background_music.mp3");
    }
    public static SoundManager getInstance() {
        return instance;
    }
    private void loadSound(String name, String filePath) {
        try {
            URL url = getClass().getResource(filePath);
            if (url != null) {
                AudioClip clip = new AudioClip(url.toExternalForm());
                sfxCache.put(name, clip);
            } else {
                System.err.println("Không tìm thấy file SFX: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadMusic(String filePath) {
        try {
            URL url = getClass().getResource(filePath);
            if (url != null) {
                Media media = new Media(url.toExternalForm());
                backgroundMusicPlayer = new MediaPlayer(media);
                // Cấu hình mặc định cho nhạc nền
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô tận
                backgroundMusicPlayer.setVolume(globalVolume);
            } else {
                System.err.println("Không tìm thấy file BGM: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(String name) {
        if (isMuted) return;
        AudioClip clip = sfxCache.get(name);
        if (clip != null) {
            if(name.equals("bounce")){
                clip.play(1);
            }
            else {
            clip.play(globalVolume); }
        } else {
            System.err.println("Không tìm thấy âm thanh tên: " + name);
        }
    }
    public void playMusic() {
        if (backgroundMusicPlayer != null && !isMuted) {
            backgroundMusicPlayer.play();
        }
    }
    public void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    public void pauseMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }
    public void stopAll() {
        stopMusic();

        for (AudioClip clip : sfxCache.values()) {
            clip.stop();
        }
    }

    public void setVolume(double volume) {
        if (volume < 0) volume = 0;
        if (volume > 1) volume = 1;
        this.globalVolume = volume;
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(globalVolume);
        }
    }
    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.pause(); // Tạm dừng nhạc nền khi tắt tiếng
            }
        } else {
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.play(); // Tiếp tục phát khi bật lại
            }
        }
    }

    public boolean isMuted() {
        return isMuted;
    }
}