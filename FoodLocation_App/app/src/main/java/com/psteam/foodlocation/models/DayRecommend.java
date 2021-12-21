package com.psteam.foodlocation.models;

public class DayRecommend {
    private String text;
    private int value;

    public DayRecommend(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return text;
    }
}
