package com.example.eddie.panicbutton;

/**
 * Created by eddie on 2014/09/30.
 */
public class ItemDetails {

    private String name ;
    private String number;
    private int imageNumber;
    private boolean checkbox;



    //Set name
    public void setName(String name) {
        this.name = name;
    }

    //Get name
    public String getName() {
        return name;
    }

    //Set number
    public void setNumber(String number) {
        this.number = number;
    }

    //Get number
    public String getNumber() {
        return number;
    }

    //Set image number
    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    //Get image number
    public int getImageNumber() {
        return imageNumber;
    }

    //Set checkbox value
    public void setCheckBox(boolean checkbox_value) {
        this.checkbox = checkbox_value;
    }

    //Get checkbox value
    public boolean getCheckBox() {
        return checkbox;
    }
}
