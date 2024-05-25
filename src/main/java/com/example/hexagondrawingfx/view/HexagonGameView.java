package com.example.hexagondrawingfx.view;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.media.Media;

import java.net.URL;
import java.util.Stack;

public class HexagonGameView
{
    private final BorderPane root;
    private final Button startButton;
    private final Button swapButton;
    private final Button soundButton;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private final Button helpButton;
    private final RadioButton rb5x5;
    private final RadioButton rb11x11;
    private final RadioButton rb17x17;
    private final Text turnLabel;
    private Pane gamePane;

    public HexagonGameView()
    {
        root = new BorderPane();

        // Kullandigimiz renk paleti : https://colorhunt.co/palette/4793afffc470dd57468b322c
        //Ses acip kapama butonu ve efektleri.
        soundButton = new Button("🔊");
        soundButton.setStyle(
                "-fx-font-size: 25; " +
                        "-fx-background-color: #DD5746; " +
                        "-fx-font-family: 'Times New Roman'; " +
                        "-fx-background-radius: 2; " +
                        "-fx-border-radius: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); " +
                        "-fx-text-fill: #FFC470; " + // Yazı rengini  yapmak için
                        "-fx-padding: 10 10 10 10;" // Buton içi boşlukları ayarlamak için
        );
        soundButton.setOnAction(event ->
        {
            if (isPlaying)
            {
                mediaPlayer.pause();
                soundButton.setText("🔇");
            } else
            {
                mediaPlayer.play();
                soundButton.setText("🔊");
            }
            isPlaying = !isPlaying;
        });
        // StartButton ve efektleri
        startButton = new Button("Start Game");
        startButton.setStyle(
                "-fx-font-size: 50; " +
                        "-fx-background-color: #DD5746; " +
                        "-fx-font-family: 'Times New Roman'; " +
                        "-fx-background-radius: 2; " +
                        "-fx-border-radius: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); " +
                        "-fx-text-fill: #FFC470; " + // Yazı rengini  yapmak için
                        "-fx-padding: 10 20 10 20;" // Buton içi boşlukları ayarlamak için
        );
        // Hover animasyonu
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), startButton);
        scaleUp.setToX(1.3);
        scaleUp.setToY(1.3);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), startButton);
        scaleDown.setToX(1);
        scaleDown.setToY(1);

        startButton.setOnMouseEntered(e -> scaleUp.playFromStart());
        startButton.setOnMouseExited(e -> scaleDown.playFromStart());

        // How To Play? butonu ve efektleri
        helpButton = new Button("How To Play?");
        helpButton.setStyle(
                "-fx-font-size: 25; " +
                        "-fx-font-family: 'Times New Roman'; " +
                        "-fx-background-color: #DD5746; " +
                        "-fx-background-radius: 2; " +
                        "-fx-border-radius: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); " +
                        "-fx-text-fill: #FFC470; " + // Yazı rengini beyaz yapmak için
                        "-fx-padding: 10 20 10 20;" // Buton içi boşlukları ayarlamak için
        );
        ScaleTransition scaleUpforHelp = new ScaleTransition(Duration.millis(200), helpButton);
        scaleUpforHelp.setToX(1.4);
        scaleUpforHelp.setToY(1.4);

        ScaleTransition scaleDownforHelp = new ScaleTransition(Duration.millis(200), helpButton);
        scaleDownforHelp.setToX(1);
        scaleDownforHelp.setToY(1);

        helpButton.setOnMouseEntered(e -> scaleUpforHelp.playFromStart());
        helpButton.setOnMouseExited(e -> scaleDownforHelp.playFromStart());

        //Swap butonu ve efektleri
        swapButton = new Button("Swap"); // Swap butonu oluşturuldu
        swapButton.setFont(new Font(20));
        swapButton.setStyle(
                "-fx-font-size: 25; " +
                        "-fx-font-family: 'Times New Roman'; " +
                        "-fx-background-color: #8B322C; " +
                        "-fx-background-radius: 2; " +
                        "-fx-border-radius: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); " +
                        "-fx-text-fill: #FFC470; " + //
                        "-fx-padding: 10 20 10 20;"
        );
        swapButton.setDisable(true); // Baslangıcta swap Butonunu pasif hale getiriyoruz.

        createStartButton();
        playBackgroundMusic(); // Muzik calma fonksiyonunu cagiriyoruz

        //Harita ayarlaması icin radio button ' lar ve efektleri
        ToggleGroup group = new ToggleGroup();
        rb5x5 = new RadioButton("5x5");
        String radioButtonEffect = "-fx-font-size: 14; " +
                "-fx-font-family: 'Times New Roman'; " +
                "-fx-background-color: #8B322C; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); " +
                "-fx-text-fill: #FFFAE6; " +
                "-fx-padding: 5 5 5 5;";
        rb5x5.setStyle(radioButtonEffect);
        rb5x5.setToggleGroup(group);
        rb5x5.setSelected(true);

        rb11x11 = new RadioButton("11x11");
        rb11x11.setStyle(radioButtonEffect);
        rb11x11.setToggleGroup(group);

        rb17x17 = new RadioButton("17x17");
        rb17x17.setStyle(radioButtonEffect);
        rb17x17.setToggleGroup(group);

        turnLabel = new Text();
        turnLabel.setFont(new Font(20));
    }

    public Polygon createHexagon(double centerX, double centerY, double radius, int gridX, int gridY)
    {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++)
        {
            double angle = Math.toRadians(30 + 60 * i);
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            hexagon.getPoints().addAll(x, y);
        }
        hexagon.getProperties().put("gridX", (double) gridX);
        hexagon.getProperties().put("gridY", (double) gridY);
        hexagon.setStroke(Color.BLACK);
        hexagon.setStrokeWidth(1);
        hexagon.setFill(Color.TRANSPARENT);
        return hexagon;
    }

    public void createStartButton()
    {
        // Hex Game yazisi icin Text
        Text hexText = new Text(" ⬡ HEX (board game) ⬡ ");
        hexText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 60));
        hexText.setFill(Color.web("#8B322C"));
        hexText.setStroke(Color.web("#DD5746"));
        hexText.setStrokeWidth(2);

        //Ic golge efekti icin
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.BLACK);
        innerShadow.setRadius(2);
        innerShadow.setBlurType(BlurType.GAUSSIAN);

        // Hex yazisinin yerinin belirlenmesi ve gölge efekti uygulanmasi.
        StackPane hexTextPane = new StackPane(hexText);
        StackPane.setMargin(hexText, new Insets(40, 0, 0, 0)); // Üstten 40 birimlik boşluk bırakıyoruz
        hexTextPane.setEffect(innerShadow); // InnerShadow efekti

        // Butonları içeren VBox (Start ve How To Play? butonlari)
        VBox buttonBox = new VBox(10); // Dikey sirada siralama;
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(startButton, helpButton); // Butonlar buttonBox'a ekleniyor
        VBox.setMargin(helpButton, new Insets(10, 0, 0, 0));



        StackPane mainPane = new StackPane(buttonBox);
        mainPane.setPadding(new Insets(10));
        root.setCenter(mainPane);
        root.setTop(hexTextPane); // Hex text'i ekranın üst kısmında göstermek için root'un üstüne ekliyoruz

        root.setStyle("-fx-background-color: #FFC470;"
                + "-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.60), 30, 0.5, 0, 0), "
                + "glow(0.5);");
    }

    public void updateTurnLabel(int turnCount, boolean isPlayer1Turn) {
        String currentPlayer = isPlayer1Turn ? "Red Player" : "Blue Player";
        turnLabel.setText("Turn: " + turnCount + " - Current: " + currentPlayer);
        turnLabel.setStyle(
                "-fx-font-size: 25; " +
                        "-fx-font-family: 'Times New Roman'; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); "
        );
    }
    private void playBackgroundMusic()
    {
        URL resource = getClass().getResource("/background.mp3");
        Media media = new Media(resource.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // muzik sonsuz dongude
        mediaPlayer.play();
    }
    //GETTER VE SETTER METODLAR
    public void setGamePane(Pane gamePane)
    {
        this.gamePane = gamePane;
        root.setCenter(gamePane);
    }

    public void setBottomBox()
    {
        //Radio  butonlar icin yatay eksende bir izgara
        HBox radioBox = new HBox(10, rb5x5, rb11x11, rb17x17);
        radioBox.setAlignment(Pos.CENTER);
        radioBox.setPadding(new Insets(10));

        //Ekranin tabaninda gozukecek bir izgara;
        HBox bottomBox = new HBox(20, turnLabel, radioBox,swapButton,soundButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);
    }
    public BorderPane getRoot() {return root;}
    public Button getSwapButton() {return swapButton;}
    public Button getStartButton() {return startButton;}
    public Button getHelpButton() { return helpButton; }
    public RadioButton getRb5x5() {return rb5x5;}
    public RadioButton getRb11x11() {return rb11x11;}
    public RadioButton getRb17x17() {return rb17x17;}
    public Text getTurnLabel() {return turnLabel;}
    public Pane getGamePane() {return gamePane;}

}
