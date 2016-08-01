import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;

/**
 * Created by User on 7/30/2016.
 */
public class AudioManager {

    File file;

    Media media;
    MediaPlayer mediaPlayer;

    boolean playing;

    int timePause = 0;
    int wordPause = 0;

    public AudioManager(){
        playing = false;
    }

    public void setFile(File file){
        this.file = file;

        String path = file.getAbsolutePath();
        path = path.replace("\\", "/");
        media = new Media(new File(path).toURI().toString());

        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.stop();
        mediaPlayer.setVolume(0.1);
        playing = false;
    }

    public boolean isPlaying(){
        return playing;
    }

    public void play(){
        try {

            playing = true;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (playing){
                        try {
                            mediaPlayer.play();

                            Thread.sleep(timePause * 1000);

                            mediaPlayer.pause();

                            Thread.sleep(wordPause * 1000);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

            thread.start();
        } catch (Exception e){

        }
    }

    public void pause(){
        try {
            mediaPlayer.pause();
            playing = false;
        } catch (Exception e){

        }
    }

    public void setVolume(double z){
        try {
            mediaPlayer.setVolume(z);
        } catch (Exception e){

        }
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public void setTimePause(int i){
        timePause = i;
    }

    public void setWordPause(int i){
        wordPause = i;
    }
}
