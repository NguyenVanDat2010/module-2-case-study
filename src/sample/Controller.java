package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    MediaPlayer player;

    @FXML
    private Slider timeSlider;

    @FXML
    private Button btnShuffle;

    @FXML
    private Button btnRepeat;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnPrev;

    @FXML
    private Button btnSeekPrev;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnSeekNext;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnAudio;

    @FXML
    private Slider audioSlider;

    @FXML
    private MenuItem miOpen;

    @FXML
    private MenuItem miSave;

    @FXML
    private MenuItem miExit;

    @FXML
    private MenuItem miSpeed;

    @FXML
    private MenuItem miAbout;

    @FXML
    private MediaView mediaView;

    @FXML
    public void openFile(ActionEvent event) throws FileNotFoundException {
        if (player != null) {
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));
            player.pause();
            player.dispose();
        }
        try {
            System.out.println("open file");
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);

            Media media = new Media(file.toURI().toURL().toString());
            player = new MediaPlayer(media);
            mediaView.setMediaPlayer(player);

            //Set mặc định khi open file sẽ play mucsic
            player.play();
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.jpg"))));

            //time slider (set time hiển thị thời gian trên thanh slider)
            player.setOnReady(() -> {
                //when player gets ready
                timeSlider.setMin(0);
                timeSlider.setMax(player.getMedia().getDuration().toMinutes());
//                System.out.println(player.getMedia().getDuration().toSeconds());
                timeSlider.setValue(0);
                player.setMute(false);

            });

            //listener on player (set chạy thanh slider theo thời gian của bài nhạc)
            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                    Duration dur = player.getCurrentTime();

                    timeSlider.setValue(dur.toMinutes());
                }
            });

            //time slider (thay đổi hay tời thanh thời gian của bài nhạc)
            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (timeSlider.isPressed()) {
                        double value = timeSlider.getValue();
                        player.seek(new Duration(value *60* 1000));
                    }
                }
            });
            // slider volume
            audioSlider.setValue(player.getVolume()*100);
            audioSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    player.setVolume(audioSlider.getValue()/100);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sự kiện cho nút button play
    @FXML
    void playClick(ActionEvent event) {
        try {
            MediaPlayer.Status status = player.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                player.pause();
                btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));
//            btnPlay.setText("Play");
            } else {
                player.play();
                btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.jpg"))));
//                btnPlay.setText("Pause");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void prevClick(ActionEvent event){

    }

    @FXML
    void nextClick(ActionEvent event){

    }

    @FXML
    void seekNext(ActionEvent event) {
        double duration = player.getCurrentTime().toSeconds();
        duration += 10;
        player.seek(new Duration(duration * 1000));
    }

    @FXML
    void seekPrev(ActionEvent event) {
        double duration = player.getCurrentTime().toSeconds();
        duration -= 10;
        player.seek(new Duration(duration * 1000));
    }

    @FXML
    void audioClick(ActionEvent event) {
        try {
            if(player.isMute()){
                player.setMute(false);
                btnAudio.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/audio.png"))));
            }else {
                player.setMute(true);
                btnAudio.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/unaudio.png"))));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void repeatClick(ActionEvent event) {

    }

    @FXML
    void shuffleClick(ActionEvent event) {

    }

    //set action cho note stop button
    @FXML
    void stopClick(ActionEvent event) {
        try {
            player.seek(Duration.ZERO);
            player.pause();
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //set action cho note exit trong menu button
    @FXML
    void exitClick(ActionEvent event) {
        try {
            Alert alertExit = new Alert(Alert.AlertType.CONFIRMATION);
            alertExit.setTitle("Confirmation");
            alertExit.setHeaderText("Do you want to close ?");

            //Tạo 2 buttonType con lựa chon khi Alert
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

            alertExit.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alertExit.showAndWait();

            if (result.get() == buttonTypeYes) {
                System.exit(0);
            } else {
                player.stop();
                btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void aboutClick(ActionEvent event) {
        try {
            Stage window = new Stage();
            StackPane layout = new StackPane();

            //click about player sẽ dừng phát nhạc và chuyển icon về play
            player.pause();
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));

            File fileLogo = new File("src/icons/logo.png");
            File fileAboutMp3 = new File("src/icons/codegyminfo1.png");
            Image imageAboutMp3 = new Image(fileAboutMp3.toURI().toString());

            //set Background image cho about info StackPane
            layout.setBackground(new Background(new BackgroundImage(imageAboutMp3, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
            Scene scene = new Scene(layout, 450, 346);

            window.setTitle("About Mp3 Player");
            //set icon cho StackPane (layout)
            window.getIcons().add(new Image(fileLogo.toURI().toString()));
            window.setScene(scene);
            window.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void speedClick(ActionEvent event) {
    }

    //Gán icon cho các button
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));
            btnPrev.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pre.jpg"))));
            btnNext.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/next.jpg"))));
            btnShuffle.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/shuffle.png"))));
            btnRepeat.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/repeat.png"))));
            btnStop.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/stop.jpg"))));
            btnAudio.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/audio.png"))));
            btnSeekNext.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/last.png"))));
            btnSeekPrev.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/first.png"))));
            miOpen.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/openfile.png"))));
            miSave.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/save.png"))));
            miExit.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/exit.png"))));
            miAbout.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/about.png"))));

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
