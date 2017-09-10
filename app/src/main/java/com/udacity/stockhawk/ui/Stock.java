package com.udacity.stockhawk.ui;

import java.io.Serializable;

/**
 * Created by PH-Data™ 01221240053 on 02/04/2017.
 */
public class Stock implements Serializable{
   private double price;
    private String history;
    private String sympol;

    public void setHistory(String history) {
        this.history = history;
    }

    public String getHistory() {
        return history;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setSympol(String sympol) {
        this.sympol = sympol;
    }

    public String getSympol() {
        return sympol;
    }
}
