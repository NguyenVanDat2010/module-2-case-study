package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {
    MediaPlayer player;

    @FXML
    private Slider timeSlider;

    @FXML
    private Label lbTimeSliderHours;

    @FXML
    private Label lbTimeSliderMinutes;

    @FXML
    private Label lbTimeSliderSeconds;

    @FXML
    private Label lbTimeSliderMaxHours;

    @FXML
    private Label lbTimeSliderMaxMinutes;

    @FXML
    private Label lbTimeSliderMaxSeconds;

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
    private Slider volumeSlider;

    @FXML
    private MenuItem miOpen;

    @FXML
    private MenuItem miSave;

    @FXML
    private MenuItem miExit;

    @FXML
    private RadioButton rbSlowSpeed;

    @FXML
    private RadioButton rbNormalSpeed;

    @FXML
    private RadioButton rbFastSpeed;

    @FXML
    private MenuItem miBackground;

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
            System.out.println("Open file");
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);

            Media media = new Media(file.toURI().toURL().toString());
            player = new MediaPlayer(media);
            mediaView.setMediaPlayer(player);

            //set setSelected cho radio button normal
            rbNormalSpeed.setSelected(true);

            //Set mặc định khi open file sẽ play music
            player.play();
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.jpg"))));

            /**time slider and volume slider (set time hiển thị thời gian trên thanh slider
             * và slider volume to nhỏ âm thanh)*/
            player.setOnReady(() -> {
                //when player gets ready (set value cho thanh time slider)
                timeSlider.setMin(0);
                timeSlider.setMax(player.getMedia().getDuration().toSeconds());
//                System.out.println(player.getMedia().getDuration().toSeconds());
                timeSlider.setValue(0);

                //audio slider (set volume hiển thị trên thanh slider)
                volumeSlider.setPrefWidth(100);
                volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
                volumeSlider.setMinWidth(30);
                volumeSlider.setValue(50);
                player.volumeProperty().bind(volumeSlider.valueProperty().divide(100));

            });

            /**listener on player (set chạy thanh slider theo thời gian của bài nhạc)*/
            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    Duration dur = player.getCurrentTime();
                    timeSlider.setValue(dur.toSeconds());

                    //Set thời gian chạy của nhạc
                    int value = (int) timeSlider.getValue();
                    int hours = value / 3600;
                    int minutes = (value - (hours * 3600)) / 60;
                    int seconds = value - (hours * 3600) - (minutes * 60);

//                    System.out.println(value);
                    lbTimeSliderSeconds.setText(numberPad(String.valueOf(seconds), 2));
                    lbTimeSliderMinutes.setText(numberPad(String.valueOf(minutes), 2));
                    lbTimeSliderHours.setText(numberPad(String.valueOf(hours), 2));

                    int maxValue = (int) timeSlider.getMax();
                    int maxHours = maxValue / 3600;
                    int maxMinutes = (maxValue - (maxHours * 3600)) / 60;
                    int maxSeconds = maxValue - (maxHours * 3600) - (maxMinutes * 60);

//                    System.out.println(maxValue);
                    lbTimeSliderMaxSeconds.setText(numberPad(String.valueOf(maxSeconds), 2));
                    lbTimeSliderMaxMinutes.setText(numberPad(String.valueOf(maxMinutes), 2));
                    lbTimeSliderMaxHours.setText(numberPad(String.valueOf(maxHours), 2));
                }
            });

            /**time slider (thay đổi hay tời thanh thời gian của bài nhạc)*/
            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (timeSlider.isPressed()) {
                        double value = timeSlider.getValue();
                        player.seek(new Duration(value * 1000));
                    }
                }
            });

            /**set âm thanh slider audio của bài nhạc*/
            volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (volumeSlider.isPressed()) {
                        double value = volumeSlider.getValue();
                        player.setVolume(value / 100);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Set length = 2 (vd: 01) cho tham số giây, phút và giờ nếu length=1*/
    public static String numberPad(String number, int length) {
        while(number.length() < length) {
            number = "0" + number;
        }
        return number;
    }

    /**
     * set sự kiện click cho nút button play
     */
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

    /**
     * set sự kiện click cho nút button previous lùi bài hát về trước
     */
    @FXML
    void prevClick(ActionEvent event) {

    }

    /**
     * set sự kiện click cho nút button next bài hát kế tiếp
     */
    @FXML
    void nextClick(ActionEvent event) {

    }

    /**
     * set sự kiện click cho nút button seek next tời bài hát về sau 10s
     */
    @FXML
    void seekNext(ActionEvent event) {
        double duration = player.getCurrentTime().toSeconds();
        duration += 10;
        player.seek(new Duration(duration * 1000));
    }

    /**
     * set sự kiện click cho nút button seek previous tời bài hát về trước 10s
     */
    @FXML
    void seekPrev(ActionEvent event) {
        double duration = player.getCurrentTime().toSeconds();
        duration -= 10;
        player.seek(new Duration(duration * 1000));
    }

    /**
     * Set button mute click tắt bật âm thanh
     */
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

    /**
     * set sự kiện click cho nút button repeat, lặp lại 1 bài hát đang playing
     */
    private boolean checkRepeat = true;
    @FXML
    void repeatClick(ActionEvent event) {
        try {
            if (checkRepeat){
                btnRepeat.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
                player.setCycleCount(MediaPlayer.INDEFINITE);
                checkRepeat = !checkRepeat;
                System.out.println("Repeat is on");
            }else
            {
                btnRepeat.setBackground(new Background(new BackgroundFill(Color.rgb(216,216,216), CornerRadii.EMPTY, Insets.EMPTY)));
//                btnRepeat.setBackground(null);
                player.setCycleCount(1);
                checkRepeat =!checkRepeat;
                System.out.println("Repeat is off");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * set sự kiện click cho nút button shuffle, chọn bài hát kế tiếp ngẫu nhiên
     */
    @FXML
    void shuffleClick(ActionEvent event) {

    }

    /**
     * set sự kiện click cho nút button stop, dừng trình phát nếu đang playing
     */
    @FXML
    void stopClick(ActionEvent event) {
        try {
            player.stop();
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set sự kiện click cho nút menuBar exit, đóng trình phát
     */
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

    /**
     * set sự kiện click cho nút menuBar about, hiển thị thông tin trình phát nhạc
     */
    @FXML
    void aboutClick(ActionEvent event) {
        try {
            Stage window = new Stage();
            StackPane layout = new StackPane();

            //click about player sẽ dừng phát nhạc và chuyển icon về play
            if (player != null) {
                player.pause();
                btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));
            }

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
    void menuSpeedClick(ActionEvent event) {
    }

    @FXML
    void slowSpeedClick(ActionEvent event) {
        if (rbSlowSpeed.isSelected()){
            rbNormalSpeed.setSelected(false);
            rbFastSpeed.setSelected(false);
            player.setRate(0.5);
        }
    }

    @FXML
    void normalSpeedClick(ActionEvent event) {
        if (rbNormalSpeed.isSelected()){
            rbSlowSpeed.setSelected(false);
            rbFastSpeed.setSelected(false);
            player.setRate(1);
        }
    }

    @FXML
    void fastSpeedClick(ActionEvent event) {
        if (rbFastSpeed.isSelected()){
            rbSlowSpeed.setSelected(false);
            rbNormalSpeed.setSelected(false);
            player.setRate(1.5);
        }
    }

    /**
     * Gán icon cho các button, menuBar,...
     */
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
