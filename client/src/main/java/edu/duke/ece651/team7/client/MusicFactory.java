package edu.duke.ece651.team7.client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

/**
 * The MusicFactory class provides methods for creating MediaPlayer objects to play sound effects and background music
 * in the game.
 */
public class MusicFactory {

    /**
     * Creates a MediaPlayer object to play media from the specified path.
     * @param mediaPath the path to the media file
     * @return a MediaPlayer object that plays the media
     */
    public static MediaPlayer createMediaPlayer(String mediaPath) {
        Media media = new Media(Paths.get(mediaPath).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        return mediaPlayer;
    }

    /**
     * Creates a MediaPlayer object to play the "attack" sound effect.
     * @return a MediaPlayer object that plays the "attack" sound effect
     */
    public static MediaPlayer createAttackPlayer(){
        String mediaPath = "src/main/resources/music/attack.wav";
        MediaPlayer attackMediaPlayer = createMediaPlayer(mediaPath);
        attackMediaPlayer.setVolume(1);
        return attackMediaPlayer;
    }

    /**
     * Creates a MediaPlayer object to play the "move" sound effect.
     * @return a MediaPlayer object that plays the "move" sound effect
     */
    public static MediaPlayer createMovePlayer(){
        String mediaPath = "src/main/resources/music/move.wav";
        MediaPlayer moveMediaPlayer = createMediaPlayer(mediaPath);
        moveMediaPlayer.setVolume(1);
        return moveMediaPlayer;
    }

    /**
     * Creates a MediaPlayer object to play the "upgrade" sound effect.
     * @return a MediaPlayer object that plays the "upgrade" sound effect
     */
    public static MediaPlayer createUpgradePlayer(){
        String mediaPath = "src/main/resources/music/upgrade.wav";
        MediaPlayer upgradeMediaPlayer = createMediaPlayer(mediaPath);
        upgradeMediaPlayer.setVolume(1);
        return upgradeMediaPlayer;
    }

    /**
     * Creates a MediaPlayer object to play the background music.
     * @return a MediaPlayer object that plays the background music
     */
    public static MediaPlayer createBackgroundPlayer(){
        String mediaPath = "src/main/resources/music/background.mp3";
        MediaPlayer backgroundMediaPlayer = createMediaPlayer(mediaPath);
        backgroundMediaPlayer.setVolume(0.4);
        return backgroundMediaPlayer;
    }

    /**
     * Creates a MediaPlayer object to play the "action failed" sound effect.
     * @return a MediaPlayer object that plays the "action failed" sound effect
     */
    public static MediaPlayer createActionFailedPlayer(){
        String mediaPath = "src/main/resources/music/actionFailed.wav";
        MediaPlayer actionFailedPlayer = createMediaPlayer(mediaPath);
        actionFailedPlayer.setVolume(1);
        return actionFailedPlayer;
    }

    /**
     * Creates a MediaPlayer object to play the "ally" sound effect.
     * @return a MediaPlayer object that plays the "ally" sound effect
     */
    public static MediaPlayer createAllyPlayer(){
        String mediaPath = "src/main/resources/music/ally.wav";
        MediaPlayer allyPlayer = createMediaPlayer(mediaPath);
        allyPlayer.setVolume(1);
        return allyPlayer;
    }

    /**
     * Creates a MediaPlayer object to play the "manufacture" sound effect.
     * @return a MediaPlayer object that plays the "manufacture" sound effect
     */
    public static MediaPlayer createManufacturePlayer(){
        String mediaPath = "src/main/resources/music/manufacture.mp3";
        MediaPlayer manufacturePlayer = createMediaPlayer(mediaPath);
        manufacturePlayer.setVolume(1);
        return manufacturePlayer;
    }

    /**
     * Creates a MediaPlayer object to play the "commit" sound effect.
     * @return a MediaPlayer object that plays the "commit" sound effect
     */
    public static MediaPlayer createCommitPlayer(){
        String mediaPath = "src/main/resources/music/commit.mp3";
        MediaPlayer commitPlayer = createMediaPlayer(mediaPath);
        commitPlayer.setVolume(1);
        return commitPlayer;
    }
}
