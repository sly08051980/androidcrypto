package com.example.crypto;

public class MyData {
    private String nom;
    private String valeur;
    private String pourcentage;
    private String total;
    private String priceChangePercent;
    private String priceChangeAmount;
    private String dollars;
    private String trigramme;
    private String stablecoin;
    private String exchange;

    public MyData(String nom, String valeur, String pourcentage, String total, String priceChangePercent, String priceChangeAmount, String dollars,String trigramme,
    String stablecoin,String exchange) {
        this.nom = nom;
        this.valeur = valeur;
        this.pourcentage = pourcentage;
        this.total = total;
        this.priceChangePercent = priceChangePercent;
        this.priceChangeAmount = priceChangeAmount;
        this.dollars= dollars;
        this.trigramme=trigramme;
        this.stablecoin=stablecoin;
        this.exchange=exchange;
    }

    public String getTrigramme() {
        return trigramme;
    }
    public String getStablecoin() {
        return stablecoin;
    }
    public String getExchange() {
        return exchange;
    }

    public String getNom() {
        return nom;
    }

    public String getValeur() {
        return valeur;
    }

    public String getPourcentage() {
        return pourcentage;
    }

    public String getTotal() {
        return total;
    }

    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    public String getPriceChangeAmount() {
        return priceChangeAmount;
    }
    public String getDollars() {
        return dollars;
    }


}

