package com.example.hexagondrawingfx.controller;

import com.example.hexagondrawingfx.model.HexagonGameModel;
import com.example.hexagondrawingfx.view.HexagonGameView;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class HexagonGameController
{
    private final HexagonGameModel model;
    private final HexagonGameView view;

    /**
     * model ve view nesneleri referans alınarak sınıflar arası bağımlılık sağlanır
     * @param model
     * @param view
     */
    public HexagonGameController(HexagonGameModel model, HexagonGameView view)
    {
        this.model = model;
        this.view = view;
    }


    /**
     *  Yanlıs zamanda swap rule butonuna basıldıgı zamanda calisir ve uyarı verip kullanicinin kapamasını bekler
     */
    private void showSwapAlert()
    {
        Alert swapAlert = new Alert(Alert.AlertType.INFORMATION);
        swapAlert.setTitle("Swap Rule Alert!");
        swapAlert.setHeaderText(null);
        swapAlert.setContentText("YOU CAN ONLY USE SWAP RULE FOR SECOND TURN");
        swapAlert.showAndWait();
    }
    private void handleSwap()
    {
            if (model.getTurnCount() == 2 && !model.isSwapUsed())
            {
                // İkinci oyuncunun hamle sırası geldiğinde ve swap hakkı henüz kullanılmamışsa
                for (Polygon hex : model.getHexagonOwnerMap().keySet())
                {
                    if (model.getHexagonOwnerMap().get(hex).equals("Player1"))
                    {
                        hex.setFill(Color.web("#4793AF")); // Tüm kırmızı tasları maviye cevir.
                        model.getHexagonOwnerMap().put(hex, "Player2"); // Haritada altigenin sahibini günceller
                    }
                }

                model.setSwapUsed(true); // Swap hakkının kullanıldığını işaretle
                view.getSwapButton().setDisable(true); // Swap butonunu kullanıldıktan sonra devre dışı bırak

                model.switchTurn(); // Sıranın diğer oyuncuya geçmesini sağla
                view.updateTurnLabel(model.getTurnCount(), model.isPlayer1Turn()); // El bilgilerini veren ızgarayı güncelle
            } else
            {
                // Hatalı durum: Swap hakkı zaten kullanıldı veya sıra henüz ikinci oyuncuda değil
                System.out.println("Swap özelliğini kullanamazsınız!");
                showSwapAlert();
                view.getSwapButton().setDisable(true);
            }
    }

    public void initialize(Stage primaryStage)
    {
        view.createStartButton();
        view.getStartButton().setOnAction(event -> startGame());    // Start butonuna tıklanınca startGame() fonksiyonunu çağır
        view.getHelpButton().setOnAction(event ->showHelp());       // How To PLay? butonuna  tıklanınca showHelp() fonksiyonunu çağır
        view.getSwapButton().setOnAction(event -> handleSwap());     // Swap butonuna tıklanınca handleSwap() fonksiyonunu çağır

        Scene scene = new Scene(view.getRoot(), 900, 700);
        primaryStage.setTitle("Hex Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGame()
    {
        Pane gamePane = new Pane();  //oyun alanini olustur
        view.setGamePane(gamePane);
        drawHexagons();         //altigenler cizdirilir.

        view.setBottomBox();

        view.getRb5x5().setOnAction(event -> {
            model.setNumHexagons(5);    // 5x5 olacak şekilde 25 adet altigen cizdirilir. (rhombus)
            model.setNumOfRow(5);
            resetGame();
        });

        view.getRb11x11().setOnAction(event -> {
            model.setNumHexagons(11);   //11x11 olacak sekilde bir altigen haritasi olusturulur (rhombus)
            model.setNumOfRow(11);
            resetGame();
        });

        view.getRb17x17().setOnAction(event -> {
            model.setNumHexagons(17);       //17x17 olacak sekilde bir altigen haritasi olustulur (rhombus)
            model.setNumOfRow(17);
            resetGame();
        });
    }

    private void handleHexagonClick(MouseEvent event, Polygon hexagon)
    {
        if (!model.getHexagonPaintedMap().get(hexagon)) //Eger altigen boyanmamissa
        {
            if (model.isPlayer1Turn())      //Sıra kırmızı oyuncuda ise
            {
                hexagon.setFill(Color.web("#DD5746"));      //Altigen kirmizi renge boyanir
                model.getHexagonOwnerMap().put(hexagon, "Player1");     //Altigenin sahibi kirmizi oyuncu olarak atanir.
            } else      //Kırmızı oyuncuda degilse mavi oyuncudadır ve
            {
                hexagon.setFill(Color.web("#4793AF"));      //Altigen mavi renge boyanir
                model.getHexagonOwnerMap().put(hexagon, "Player2");    //Altigenin sahibi mavi oyuncu olarak atanir.
            }
            model.getHexagonPaintedMap().put(hexagon, true);    //Altigen her halükarda isaretlenmis olur.

            //ANIMATION : Altigenin boyanması ekranında beyazdan tam kendi rengine yarım saniyede geçer..
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), hexagon);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            if (model.getTurnCount() == 1 && !model.isSwapUsed())
            {
                view.getSwapButton().setDisable(false); // İlk hamleden sonra swap butonunu etkinleştir
            }

            if (checkWin()) //Oyun bitti mi kontrol et eger bitti ise;
            {
                String winner = model.isPlayer1Turn() ? "Red Player" : "Blue Player";       //eger play1Turn ise winner = Red Player degilde winner = Blue Player.
                showEndGameScreen(winner);      //showEndGameScreen metodunu winner (string) degiskeni ile cagir
                return;
            }

            model.switchTurn();     //Sirayi degistir
            view.updateTurnLabel(model.getTurnCount(), model.isPlayer1Turn());  //Sıra ızgarasını güncelle
        }
    }

    private boolean checkWin() //Bu fonksiyon Kazanma durumunu kontrol eder
    {
        if (model.isPlayer1Turn())  // Eğer sıra Oyuncu 1'e aitse
        {
            for (Polygon hex : model.getHexagonOwnerMap().keySet())   // Tüm altıgenlerin sahiplerini kontrol eder
            {
                if (model.getHexagonOwnerMap().get(hex).equals("Player1"))   // Eğer altıgenin sahibi Oyuncu 1 ise
                {
                    int x = (int) ((double) hex.getProperties().get("gridX"));  // Altıgenin gridX özelliğini alır
                    if (x == 1 && bfs(hex, "Player1", x, true))  // Eğer altıgen ilk sıradaysa ve Oyuncu 1'in kazanması mümkünse
                    {
                        return true;    //true donderilir yani kırmızı kazanmıstır
                    }
                }
            }
        } else  // Eğer sıra Oyuncu 2'ye aitse
        {
            for (Polygon hex : model.getHexagonOwnerMap().keySet()) // Tüm altıgenlerin sahiplerini kontrol eder
            {
                if (model.getHexagonOwnerMap().get(hex).equals("Player2"))  // Eğer altıgenin sahibi Oyuncu 2 ise
                {
                    int y = (int) ((double) hex.getProperties().get("gridY"));   // Altıgenin gridY özelliğini alır
                    if (y == 1 && bfs(hex, "Player2", y, false)) // Eğer altıgen ilk sıradaysa ve Oyuncu 2'nin kazanması mümkünse
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean bfs(Polygon startHex, String player, int start, boolean isHorizontal)  // Genişlik öncelikli arama (BFS) algoritması
    {
        Queue<Polygon> queue = new LinkedList<>();   // BFS için kuyruk oluşturur
        Set<Polygon> visited = new HashSet<>();    // Ziyaret edilmiş altıgenler kümesi oluşturur
        queue.add(startHex);    // Başlangıç altıgenini kuyruğa ekler
        visited.add(startHex);  // Başlangıç altıgenini ziyaret edilmiş olarak işaretler

        while (!queue.isEmpty())   //kuyruk boş olmadığı sürece döngü çalışır
        {
            Polygon current = queue.poll();   // Kuyruğun başındaki altıgeni al ve kuyruktan çıkarır
            int x = (int) ((double) current.getProperties().get("gridX"));   // Altıgenin gridX özelliğini alır
            int y = (int) ((double) current.getProperties().get("gridY"));    // Altıgenin gridY özelliğini alır

            if (isHorizontal && x == model.getNumHexagons()) return true;   // Eğer yatay yönde arama yapılıyorsa ve altıgen son sütunda ise kazanma durumu gerçekleşir
            if (!isHorizontal && y == model.getNumOfRow()) return true;     // Eğer dikey yönde arama yapılıyorsa ve altıgen son satırda ise kazanma durumu gerçekleşir

            for (Polygon neighbor : getNeighbors(x, y)) // Altıgenin komşularını almak için yardımcı fonksiyonu kullanır
            {
                if (model.getHexagonOwnerMap().get(neighbor).equals(player) && !visited.contains(neighbor))   // Eğer komşu altıgen bu oyuncuya aitse ve daha önce ziyaret edilmemişse
                {
                    queue.add(neighbor);   // Komşu altıgeni kuyruğa ekler
                    visited.add(neighbor);  // Komşu altıgeni ziyaret edilmiş olarak işaretler
                }
            }
        }
        return false;
    }

    private List<Polygon> getNeighbors(int x, int y)   // Belirli bir altıgenin komşularını döndüren fonksiyon
    {
        List<Polygon> neighbors = new ArrayList<>();    // Komşuları tutacak liste oluşturur
        int[][] deltas =    // Altıgenin altı komşusunun x ve y koordinatlarını belirleyen delta değerleri
        {
                {-1, 0}, {-1, 1}, {0, -1},
                {0, 1}, {1, -1}, {1, 0}
        };

        for (int[] delta : deltas)
        {
            int nx = x + delta[0];      // Yeni x koordinatını hesaplar
            int ny = y + delta[1];      // Yeni y koordinatını hesaplar
            if (nx > 0 && nx <= model.getNumHexagons() && ny > 0 && ny <= model.getNumOfRow())    // Eğer yeni koordinatlar oyun alanının içindeyse tüm altıgenlerin üzerinde döner
            {
                for (Polygon hex : model.getHexagonOwnerMap().keySet())
                {
                    if ((int) ((double) hex.getProperties().get("gridX")) == nx &&      // Eğer altıgenin koordinatları yeni koordinatlara eşitse
                            (int) ((double) hex.getProperties().get("gridY")) == ny)
                    {
                        neighbors.add(hex);      // Altıgeni komşu olarak ekler
                    }
                }
            }
        }
        return neighbors;       // Komşu altıgenleri içeren listeyi döndürur
    }

    private void drawHexagons()     // Altıgenleri çizen fonksiyon
    {
        Pane pane = view.getGamePane();
        pane.getChildren().clear();   // oyun alanında onceden eklenmis ogeler kaldirilir
        model.getHexagonPaintedMap().clear();  //boyanmis altigenlerin bilgisini sifirlar
        model.getHexagonOwnerMap().clear(); // altigenlerin sahiplik bilgisini sifirlar
        double hexagonRadius = 300/ model.getNumOfRow();  //altigenlerin yaricapini temsil eden bir degisken olusturur
        double hexagonWidth = Math.sqrt(3) * hexagonRadius;     //altigenlerin genisligini temsil eden bir degisken olusturur
        //altigenlerin cizilmeye baslanacagi noktalari belirler
        double startX = 100;
        double startY = 100;

        for (int row = 1; row <= model.getNumOfRow(); row++)        //Her bir satır icin bu donguye girer
        {
            for (int col = 1; col <= model.getNumHexagons(); col++)         //Her bir sutun icin bu donguye girer
            {
                double x = startX + (col - 1) * hexagonWidth + (row - 1) * hexagonWidth / 2;
                double y = startY + (row - 1) * 1.5 * hexagonRadius;        //Her bir iterasyonda kayma degerine göre yeni altıgenlerin baslangıc noktaları icin kullanılacak degerler
                Polygon hexagon = view.createHexagon(x, y, hexagonRadius, col, row);    //Bu degerlere göre Polygon sinifindan hexagon isminde nesneler olusturulur.(createHexagon metodu ile)

                if (row == 1 || row == model.getNumOfRow())         //En ust ve son sıradaki altıgenlerin rengini mavi olarak ayarlar.
                {
                    hexagon.setStroke(Color.web("#4793AF"));
                } else if (col == 1 || col == model.getNumHexagons())       //En sag ve en sol sutundaki altıgenlerin rengini ise kırmızı olarak ayarlar
                {
                    hexagon.setStroke(Color.web("#DD5746"));
                }

                model.getHexagonPaintedMap().put(hexagon, false);           //Boyanmis altigen haritasını bos olarak baslatır
                model.getHexagonOwnerMap().put(hexagon, "");                //Altıgen sahiplik haritasını bos olarak baslatir
                hexagon.setOnMouseClicked(event -> handleHexagonClick(event, hexagon));     //Altigenlere tiklandıgında handleHexagonClick butonuna cagri yapar
                pane.getChildren().add(hexagon);        //Altigen pane'a eklenir
            }
        }
    }


    /**
     * Oyun bitti ekranını gösterir.
     *
     * @param winner
     */
    private void showEndGameScreen(String winner) {

        String endGameColor; //Oyun bitti ekranının rengini kazanan oyuncuya göre ayarlamak için.
        if (winner == "Red Player")
            endGameColor = "-fx-background-color: #DD5746; ";
        else
            endGameColor = "-fx-background-color: #4793AF; ";

        Stage endGameStage = new Stage();
        endGameStage.initModality(Modality.APPLICATION_MODAL); // Pencereyi modal yapar
        endGameStage.setTitle("Game Over");

        VBox endGameBox = new VBox(20);
        endGameBox.setAlignment(Pos.CENTER);
        endGameBox.setPadding(new Insets(10));

        Text endGameText = new Text(winner + " wins!");
        endGameText.setStyle(
                "-fx-font-size: 25; " +
                        "-fx-font-family: 'Times New Roman'; " +
                        "-fx-background-color: #FFC470; " +
                        "-fx-background-radius: 2; " +
                        "-fx-border-radius: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); " +
                        "-fx-text-fill: #8B322C; " + // Yazı rengini beyaz yapmak için
                        "-fx-padding: 10 20 10 20;" // Buton içi boşlukları ayarlamak için
        );
        endGameText.setFocusTraversable(false); // Yazının odaklanmasını engeller
        endGameBox.setStyle(endGameColor);

        Button restartButton = new Button("Restart");
        restartButton.setStyle(
                "-fx-font-size: 25; " +
                        "-fx-font-family: 'Times New Roman'; " +
                        "-fx-background-color: #FFC470; " +
                        "-fx-background-radius: 2; " +
                        "-fx-border-radius: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0); " +
                        "-fx-text-fill: #8B322C; " + // Yazı rengini beyaz yapmak için
                        "-fx-padding: 10 20 10 20;" // Buton içi boşlukları ayarlamak için
        );
        restartButton.setOnAction(event -> { //"restartButton" adindaki dugmeye tiklama islemi eklenir
            endGameStage.close();    //oyun sona erince "endGameStage" adindaki pencerenin kapatilmasini saglar
            resetGame();
        });

        // Sahnedeki diğer tüm odaklanabilir öğeleri devre dışı bırakır
        endGameBox.getChildren().addAll(endGameText, restartButton);
        endGameBox.getChildren().forEach(node -> {
            if (node instanceof Control) {
                ((Control) node).setFocusTraversable(false);
            }
        });
        restartButton.setFocusTraversable(true); // Restart butonunun odaklanabilir olmasını sağlar

        Scene endGameScene = new Scene(endGameBox, 300, 200);
        endGameStage.setScene(endGameScene);
        endGameStage.showAndWait(); // endGameStage ekranda gosterilir diger islemler icin kullanıcıdan etkilesim beklenir.
    }

    private void resetGame() {
        model.resetGame();
        view.updateTurnLabel(model.getTurnCount(), model.isPlayer1Turn());
        drawHexagons();
        view.getSwapButton().setDisable(false);
    }

    //How To Play? Butonu icin ekran
    private void showHelp() {
        Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);   //kullaniciya bilgi sunmak icin kullanilir
        helpAlert.setTitle("How to Play");
        helpAlert.setHeaderText(null);
        helpAlert.setContentText("Hex Game is a two-player strategy game.\n\n"
                + "The goal of the game is to connect opposite sides of the board with a continuous path of your own tiles.\n\n"
                + "Red player tries to connect the top and bottom edges, while Blue player tries to connect the left and right edges.\n\n"
                + "Players take turns to place their tiles on the board. The first player to complete their path wins the game.\n\n"
                +"SWAP RULE: If properly chosen, the first move can give a big advantage to the first player."
                +" The Swap Rule equalizes chances of players. When using this rule, the second player has the option to either move normally or swap their stone with the one placed by the first player."
                +" This encourages the first player to only choose a moderately strong first move and so reduces any advantage of going first.\n\n"
                +"Have Fun :)");
        helpAlert.showAndWait();
    }

}
