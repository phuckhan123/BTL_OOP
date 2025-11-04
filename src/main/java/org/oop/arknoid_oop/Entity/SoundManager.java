package org.oop.arknoid_oop.Entity;
// SoundManager.java
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
public class SoundManager {
    // 1. Singleton Instance
    private static final SoundManager instance = new SoundManager();
    // 2. Bộ đệm cho Hiệu ứng (SFX)
    private HashMap<String, AudioClip> sfxCache;
    // 3. Trình phát cho Nhạc nền (BGM)
    private MediaPlayer backgroundMusicPlayer;
    // 4. Cài đặt Âm lượng chung
    private double globalVolume = 0.3; // Mặc định là 80%
    private boolean isMuted = false;
    // 5. Hàm khởi tạo PRIVATE (để đảm bảo là Singleton)
    private SoundManager() {
        sfxCache = new HashMap<>();
        // --- TẢI TẤT CẢ ÂM THANH NGAY KHI KHỞI TẠO ---
        // Tự động tải tất cả các file âm thanh cần thiết
        // Bằng cách này, chúng ta chỉ tải 1 lần duy nhất
        loadSound("bounce", "/sounds/bounce.mp3");
        loadSound("brickBreak", "/sounds/brickBreak.mp3");
        loadSound("brickUnbreakable", "/sounds/brickUnbreakable.mp3");
        loadSound("death", "/sounds/death.mp3");

        // Tải nhạc nền
        loadMusic("/sounds/background_music.mp3");
    }
    // 6. Phương thức để các lớp khác truy cập vào trình quản lý
    public static SoundManager getInstance() {
        return instance;
    }
    // --- CÁC PHƯƠNG THỨC TẢI ÂM THANH (NỘI BỘ) ---
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
    // --- CÁC PHƯƠNG THỨC ĐIỀU KHIỂN (PUBLIC) ---
    /**
     * Phát một hiệu ứng âm thanh (SFX) bằng tên đã đăng ký.
     */
    public void playSound(String name) {
        if (isMuted) return; // Nếu đang tắt tiếng thì không phát
        AudioClip clip = sfxCache.get(name);
        if (clip != null) {
            // Phát với mức âm lượng đã cài đặt
            if(name.equals("bounce")){
                clip.play(1);
            }
            else {
            clip.play(globalVolume); }
        } else {
            System.err.println("Không tìm thấy âm thanh tên: " + name);
        }
    }
    /**
     * Bắt đầu phát nhạc nền (BGM).
     */
    public void playMusic() {
        if (backgroundMusicPlayer != null && !isMuted) {
            backgroundMusicPlayer.play();
        }
    }
    /**
     * Dừng nhạc nền.
     */
    public void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }
    /**
     * Tạm dừng nhạc nền.
     */
    public void pauseMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }
    /**
     * Dừng tất cả âm thanh đang phát (cả SFX và BGM).
     * Rất quan trọng: Gọi hàm này khi đóng ứng dụng!
     */
    public void stopAll() {
        stopMusic(); // Dừng nhạc nền

        // Dừng tất cả các SFX đang phát (nếu cần)
        for (AudioClip clip : sfxCache.values()) {
            clip.stop();
        }
    }

    // --- CÁC PHƯƠNG THỨC CÀI ĐẶT ÂM LƯỢNG ---

    /**
     * Đặt mức âm lượng chung (từ 0.0 đến 1.0).
     */
    public void setVolume(double volume) {
        if (volume < 0) volume = 0;
        if (volume > 1) volume = 1;
        this.globalVolume = volume;
        // Cập nhật âm lượng cho nhạc nền ngay lập tức
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(globalVolume);
        }
    }

    /**
     * Bật/Tắt chế độ Tắt tiếng (Mute).
     */
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