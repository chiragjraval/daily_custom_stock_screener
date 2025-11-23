package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Model representing technical history data for a company
 */
public class CompanyTechnicalHistory {

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("dma50")
    private Double dma50;

    @JsonProperty("dma100")
    private Double dma100;

    @JsonProperty("dma200")
    private Double dma200;

    @JsonProperty("volume")
    private Double volume;

    // Constructors
    public CompanyTechnicalHistory() {
    }

    public CompanyTechnicalHistory(LocalDate date, Double price, Double dma50, Double dma200, Double volume) {
        this.date = date;
        this.price = price;
        this.dma50 = dma50;
        this.dma200 = dma200;
        this.volume = volume;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDma50() {
        return dma50;
    }

    public void setDma50(Double dma50) {
        this.dma50 = dma50;
    }

    public Double getDma100() { return dma100; }

    public void setDma100(Double dma100) { this.dma100 = dma100; }

    public Double getDma200() {
        return dma200;
    }

    public void setDma200(Double dma200) {
        this.dma200 = dma200;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "CompanyTechnicalHistory{" +
                "date=" + date +
                ", price=" + price +
                ", dma50=" + dma50 +
                ", dma200=" + dma200 +
                ", volume=" + volume +
                '}';
    }
}

