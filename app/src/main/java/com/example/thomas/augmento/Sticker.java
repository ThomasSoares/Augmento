package com.example.thomas.augmento;

public class Sticker {

    private int gifUrl;

    public Sticker()
    {

    }

    public Sticker(int gifUrl) {
        this.gifUrl = gifUrl;
    }


    public int getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(int gifUrl) {
        this.gifUrl = gifUrl;
    }
}
