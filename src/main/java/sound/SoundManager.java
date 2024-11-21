package sound;

import main.GamePanel;
import main.Updatable;

import java.util.HashMap;
import java.util.Map;

public class SoundManager implements Updatable {
    public static final String NO_SOUND = "no_sound";
    public static final String THEME_SONG = "blue_boy_adventure";
    public static final String COIN_SOUND = "coin";
    public static final String POWER_UP_SOUND = "power_up";
    public static final String UNLOCK_SOUND = "unlock";
    public static final String FANFARE_SOUND = "fanfare";
    public static final String HIT_MONSTER_SOUND = "hit_monster";
    public static final String RECEIVE_DAMAGE_SOUND = "receive_damage";
    public static final String WEAPON_SWING_SOUND = "weapon_swing";
    public static final String CURSOR_SOUND = "cursor";


    private final Map<String, Sound> sounds = new HashMap<>();
    private final Map<String, Integer> framesSinceLastPlay = new HashMap<>(); // Track frames for each sound

    public SoundManager() {
        sounds.put(THEME_SONG, new Sound(THEME_SONG, "/sounds/BlueBoyAdventure.wav", GamePanel.FPS / 4));
        sounds.put(COIN_SOUND, new Sound(COIN_SOUND,"/sounds/Coin.wav", GamePanel.FPS / 4));
        sounds.put(POWER_UP_SOUND, new Sound(POWER_UP_SOUND, "/sounds/PowerUp.wav", GamePanel.FPS / 4));
        sounds.put(UNLOCK_SOUND, new Sound(UNLOCK_SOUND, "/sounds/Unlock.wav", GamePanel.FPS / 4));
        sounds.put(FANFARE_SOUND, new Sound(FANFARE_SOUND, "/sounds/Fanfare.wav", GamePanel.FPS / 4));
        sounds.put(HIT_MONSTER_SOUND, new Sound(HIT_MONSTER_SOUND, "/sounds/hit_monster.wav", GamePanel.FPS / 4));
        sounds.put(RECEIVE_DAMAGE_SOUND, new Sound(RECEIVE_DAMAGE_SOUND, "/sounds/receive_damage.wav", GamePanel.FPS / 4));
        sounds.put(WEAPON_SWING_SOUND, new Sound(WEAPON_SWING_SOUND, "/sounds/swing_weapon.wav", GamePanel.FPS / 4));
        sounds.put(CURSOR_SOUND, new Sound(CURSOR_SOUND, "/sounds/cursor.wav", GamePanel.FPS / 8));

        // Initialize frame counters for each sound effect
        sounds.keySet().forEach(soundKey -> framesSinceLastPlay.put(soundKey, sounds.get(soundKey).soundFrameDelay));
    }

    public void playMusic(final String sound) {
        if (!sound.equals(NO_SOUND)) {
            Sound soundObj = sounds.get(sound);
            if (soundObj != null) {
                soundObj.loop();
            }
        }
    }

    public void playSound(final String sound) {
        if (!sound.equals(NO_SOUND)) {
            Sound soundObj = sounds.get(sound);
            Integer frames = framesSinceLastPlay.get(sound);
            if (soundObj != null && frames != null) {
                // Play sound if debounce delay has passed
                if (frames >= soundObj.soundFrameDelay) {
                    soundObj.play();
                    framesSinceLastPlay.put(sound, 0); // Reset this sound's frame counter after playing
                }
            }
        }
    }

    public void update() {
        framesSinceLastPlay.replaceAll((_, frames) -> frames + 1);
    }

}
