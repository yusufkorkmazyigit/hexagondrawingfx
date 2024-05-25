package com.example.hexagondrawingfx.model;

import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.Map;

public class HexagonGameModel {
    private boolean isPlayer1Turn;  // Oyuncu icin bir boolean deger. Player1 = Kirmizi olacak
    private int turnCount;      // Oyunun Kaçıncı elde oldugunu belirten degisken.
    private int numHexagons;    //Altigen hardstand sutun sayısı.
    private int numOfRow;       //Altigen haritasında satir sayisi.

    private boolean swapUsed;   //Swap kurali kullanildi mi denetlemesi icin boolean degisken.
    private final Map<Polygon, Boolean> hexagonPaintedMap;
    private final Map<Polygon, String> hexagonOwnerMap;    //Altigen haritamizda hangi altigen hangi oyuncuya ait.

    public HexagonGameModel() {
        this.isPlayer1Turn = true;  //Oyun kirmizi oyuncudan basliyor.
        this.turnCount = 1;         //Oyun 1. elden basliyor.
        this.numHexagons = 5;       //Oyun haritasi sutun sayisi.
        this.numOfRow = 5;          //Oyun haritasi satir sayisi.
        this.swapUsed = false;      //Oyun swap kurali kullanilmadi olarak basliyor.
        this.hexagonPaintedMap = new HashMap<>();   //Isaretlenmis altigenlerin tutulacagi bir harita (nesne) olusturuyoruz.
        this.hexagonOwnerMap = new HashMap<>();     //Altigen haritamizda hangi altigen hangi oyuncuya ait bunu tutmak icin bir nesne olusturuyoruz.
    }

    public void switchTurn() {
        this.isPlayer1Turn = !this.isPlayer1Turn;
        this.turnCount++;
    }
    //SETTER & GETTER Metodlar
    public boolean isSwapUsed() {
        return swapUsed;
    }
    //swap kullanılıp kullanılmadıgını kontrol eder
    public void setSwapUsed(boolean swapUsed) {
        this.swapUsed = swapUsed;
    } //swap kullanimi

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    } //su an oynayan 1.oyuncu mu diye kontrol eder


    public int getTurnCount() {
        return turnCount;
    } //oyun sirasinin kacinci turda oldugunu dondurmek icin kullanilir

    public int getNumHexagons() {
        return numHexagons;
    } //oyundaki  altigen haritasının kolon sayisini dondurur

    public void setNumHexagons(int numHexagons) {this.numHexagons = numHexagons;}//oyundaki altigen haritasının kolon sayisini ayarlamak icin kullanilir

    public int getNumOfRow() {
        return numOfRow;
    } //oyun alanindaki satir sayisi dondurur

    public void setNumOfRow(int numOfRow) {
        this.numOfRow = numOfRow;
    }//oyun alanindaki satir sayisi ayarlanir

    public Map<Polygon, Boolean> getHexagonPaintedMap() {return hexagonPaintedMap;}//oyun alanindaki her altigenin boyanip boyanmadigi tutan haritayi dondurur

    public Map<Polygon, String> getHexagonOwnerMap() {return hexagonOwnerMap;}//oyun alanindaki her altigenin sahibini tutan haritayi dondurur


    /**
     *
     * Oyun sirasi (isPlayer1Turn)kirmizi oyuncuya alinir.
     * Oyun 1.(turnCount) ele ayarlanir.
     * swapKurali (swapUsed) kullanilmadiya ayarlanir.
     * Isaretlenmis altigen haritasi (hexagonPaintedMap) sifirlanir.
     * Altigenlerin hangi renk oldugunu tutan harita (hexagonOwnerMap) sifirlanir.
     */
    public void resetGame() {
        isPlayer1Turn = true;
        turnCount = 1;
        swapUsed = false;
        hexagonPaintedMap.clear();
        hexagonOwnerMap.clear();
    }
}
