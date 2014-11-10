package com.example.eddie.panicbutton;

import java.io.Serializable;

/**
 * Created by eddie on 2014/11/06.
 */
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    public String number;
    public String name;

    public Item(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }
}
