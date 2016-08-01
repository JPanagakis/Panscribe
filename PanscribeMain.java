import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.MediaView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;

/**
 * Created by User on 7/30/2016.
 */
public class PanscribeMain implements ActionListener{

    public static void main(String[] args){
        PanscribeMain panscribeMain = new PanscribeMain();
        panscribeMain.run();
    }

    private JFrame frame;
    private JPanel panel;
    private JFXPanel fxPanel;

    private JLabel fileName, timePauseLabel, wordPauseLabel;
    private JSlider soundLocation, volume;
    private JButton findFile, prevWord, rewind, play, fastForward, nextWord, applyChanges;
    private JFormattedTextField timePause, wordPause;
    private JFileChooser fileChooser;
    private File file;

    private AudioManager audioManager;

    public PanscribeMain(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 250));

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 250));
        panel.setLayout(new GridBagLayout());

        setupComponents();

        fxPanel = new JFXPanel();


        frame.add(fxPanel);
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

        audioManager = new AudioManager();

        Platform.runLater(new Runnable() {
            @Override public void run() {
                initFX(fxPanel);
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on JavaFX thread
        Scene scene = null;
        fxPanel.setScene(scene);
    }

    public void setupComponents(){

        //temp
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);


        fileName = new JLabel();
        timePauseLabel = new JLabel();
        wordPauseLabel = new JLabel();
        soundLocation = new JSlider();
        volume = new JSlider();
        volume.setMaximum(100);
        volume.setMinimum(0);
        volume.setOrientation(JSlider.VERTICAL);
        volume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                doVolumeChange();
            }
        });
        findFile = new JButton();
        findFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doFindFile();
            }
        });
        prevWord = new JButton();
        prevWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doPrevWord();
            }
        });
        rewind = new JButton();
        rewind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doRewind();
            }
        });
        play = new JButton();
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doPlayPause();
            }
        });
        fastForward = new JButton();
        fastForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doFastForward();
            }
        });
        nextWord = new JButton();
        nextWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doNextWord();
            }
        });
        applyChanges = new JButton();
        applyChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doApplyChanges();
            }
        });

        timePause = new JFormattedTextField(formatter);
        wordPause = new JFormattedTextField(formatter);
        fileChooser = new JFileChooser();

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 0, 0);
        c.weightx = .5;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        fileName.setText("Please Select a File to Load");
        panel.add(fileName, c);
        c.gridwidth = 0;

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 4;
        c.gridy = 1;
        c.gridheight = 3;
        panel.add(volume, c);
        c.gridheight = 1;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        panel.add(soundLocation, c);
        c.gridwidth = 1;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        findFile.setText("File...");
        panel.add(findFile, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        timePauseLabel.setText("Play time:");
        panel.add(timePauseLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        wordPauseLabel.setText("Pause time:");
        panel.add(wordPauseLabel, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 2;
        timePause.setPreferredSize(new Dimension(30, 25));
        timePause.setMinimumSize(timePause.getPreferredSize());
        timePause.setText("0");
        panel.add(timePause, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 3;
        wordPause.setPreferredSize(new Dimension(30, 25));
        wordPause.setMinimumSize(wordPause.getPreferredSize());
        wordPause.setText("0");
        panel.add(wordPause, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 3;
        applyChanges.setText("Apply");
        panel.add(applyChanges, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        prevWord.setText("Previous Word");
        panel.add(prevWord, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        rewind.setText("<<");
        panel.add(rewind, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        play.setText("Play/Pause");
        panel.add(play, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 4;
        fastForward.setText(">>");
        panel.add(fastForward, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 4;
        nextWord.setText("Next Word");
        panel.add(nextWord, c);
    }

    public void doFindFile(){
            int returnVal = fileChooser.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                System.out.println("Selected File: " + file.getPath());
                fileName.setText(file.getName());
                audioManager.setFile(file);
            } else {

            }
    }

    public void doVolumeChange(){
        double z = volume.getValue() / 100.0;
        audioManager.setVolume(z);
    }

    public void doPrevWord(){

    }

    public void doRewind(){

    }

    public void doPlayPause(){
        if (audioManager.isPlaying()){
            audioManager.pause();
        } else {
            audioManager.play();
        }
    }

    public void doFastForward(){

    }

    public void doNextWord(){

    }

    public void doApplyChanges(){
        audioManager.setTimePause((int)timePause.getValue());
        audioManager.setWordPause((int)wordPause.getValue());
    }

    public void run(){

    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("this works");
    }
}
