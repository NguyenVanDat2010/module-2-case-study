package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import song.Mp3Parser;
import song.Mp3Player;
import song.Mp3Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {
<<<<<<< HEAD
    MediaPlayer player;
    private boolean checkRepeat = true;
=======
    Mp3Player mp3Player;
    Mp3Parser mp3Parser;
>>>>>>> master

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
    private TableView<Mp3Song> tableView;

    @FXML
    public void openFile(ActionEvent event) throws FileNotFoundException {
        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3", "*mp3"));
            File file = fc.showOpenDialog(new Stage());
            if (file != null) {
                mp3Player.getMp3Collection().clear();
                mp3Player.getMp3Collection().addSong(mp3Parser.createMp3Song(file));
                mp3Player.loadSong(0);
                configureTable();
                configureSlideBar();

            }
            //set setSelected cho radio button normal
            rbNormalSpeed.setSelected(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDir(ActionEvent event) throws FileNotFoundException {
        //open dialog with filter
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(new Stage());
        mp3Player.getMp3Collection().clear();
        mp3Player.getMp3Collection().addSongs(mp3Parser.createMp3Songs(dir));
        mp3Player.loadSong(0);
        configureSlideBar();
        configureTable();
        //select folder
        //save files to a new folder (async)
        //display on tableview
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
            MediaPlayer.Status status = mp3Player.getMediaPlayer().getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                mp3Player.getMediaPlayer().pause();
                btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));
//            btnPlay.setText("Play");
            } else {
                mp3Player.getMediaPlayer().play();
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
        double duration = mp3Player.getMediaPlayer().getCurrentTime().toSeconds();
        duration += 10;
        mp3Player.getMediaPlayer().seek(new Duration(duration * 1000));
    }

    /**
     * set sự kiện click cho nút button seek previous tời bài hát về trước 10s
     */
    @FXML
    void seekPrev(ActionEvent event) {
        double duration = mp3Player.getMediaPlayer().getCurrentTime().toSeconds();
        duration -= 10;
        mp3Player.getMediaPlayer().seek(new Duration(duration * 1000));
    }

    /**
     * Set button mute click tắt bật âm thanh
     */
    @FXML
    void audioClick(ActionEvent event) {
        try {
            if(mp3Player.getMediaPlayer().isMute()){
                mp3Player.getMediaPlayer().setMute(false);
                btnAudio.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/audio.png"))));
            }else {
                mp3Player.getMediaPlayer().setMute(true);
                btnAudio.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/unaudio.png"))));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * set sự kiện click cho nút button repeat, lặp lại 1 bài hát đang playing
     */
    @FXML
    void repeatClick(ActionEvent event) {
        try {
            if (checkRepeat){
                btnRepeat.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                mp3Player.getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);

                checkRepeat = !checkRepeat;
                System.out.println("Repeat is on");
            }else
            {

                btnRepeat.setBackground(new Background(new BackgroundFill(Color.rgb(216,216,216), CornerRadii.EMPTY, Insets.EMPTY)));
//                btnRepeat.setBackground(null);
                mp3Player.getMediaPlayer().setCycleCount(1);
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
            mp3Player.getMediaPlayer().stop();
            btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));

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
    void slowSpeedClick(ActionEvent event) {
        if (rbSlowSpeed.isSelected()){
            rbNormalSpeed.setSelected(false);
            rbFastSpeed.setSelected(false);
            mp3Player.getMediaPlayer().setRate(0.5);
        }
    }

    @FXML
    void normalSpeedClick(ActionEvent event) {
        if (rbNormalSpeed.isSelected()){
            rbSlowSpeed.setSelected(false);
            rbFastSpeed.setSelected(false);
            mp3Player.getMediaPlayer().setRate(1);
        }
    }

    @FXML
    void fastSpeedClick(ActionEvent event) {
        if (rbFastSpeed.isSelected()){
            rbSlowSpeed.setSelected(false);
            rbNormalSpeed.setSelected(false);
            mp3Player.getMediaPlayer().setRate(1.5);
        }
    }


    void configureSlideBar() {
        mp3Player.getMediaPlayer().setOnReady(() -> {
            //when player gets ready (set value cho thanh time slider)
            timeSlider.setMin(0);
            timeSlider.setMax(mp3Player.getMediaPlayer().getMedia().getDuration().toSeconds());
//                System.out.println(player.getMedia().getDuration().toSeconds());
            timeSlider.setValue(0);
//audio slider (set volume hiển thị trên thanh slider)
            volumeSlider.setPrefWidth(100);
            volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
            volumeSlider.setMinWidth(30);
            volumeSlider.setValue(50);
            mp3Player.getMediaPlayer().volumeProperty().bind(volumeSlider.valueProperty().divide(100));
        });

        /**listener on player (set chạy thanh slider theo thời gian của bài nhạc)*/
        mp3Player.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                Duration dur = mp3Player.getMediaPlayer().getCurrentTime();
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
                    mp3Player.getMediaPlayer().seek(new Duration(value * 1000));
                }
            }
        });
        /**set âm thanh slider audio của bài nhạc*/
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (volumeSlider.isPressed()) {
                double value = volumeSlider.getValue();
                mp3Player.getMediaPlayer().setVolume(value / 100);
            }
        });

    }
    void configureTable() {
        tableView.setItems(mp3Player.getMp3Collection().getSongList());
        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {
                        mp3Player.loadSong(tableView.getSelectionModel().getSelectedIndex());
                        configureSlideBar();
                        btnPlay.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.jpg"))));
                    } catch (FileNotFoundException exception) {
                        System.out.println("File not found");
                    }
                }

            }
        });
        tableView.getSelectionModel().select(0);
    }
    void drawTable() {
        TableColumn<Mp3Song, String> titleColumn = new TableColumn<Mp3Song, String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Mp3Song, String> authorColumn = new TableColumn<Mp3Song, String>("Artist");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Mp3Song, String> albumColumn = new TableColumn<Mp3Song, String>("Album");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

        tableView.getColumns().add(titleColumn);
        tableView.getColumns().add(authorColumn);
        tableView.getColumns().add(albumColumn);
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
            mp3Player = new Mp3Player();
            mp3Parser = new Mp3Parser();
            drawTable();

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
