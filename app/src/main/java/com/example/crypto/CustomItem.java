package com.example.crypto;

public class CustomItem {
    public String nom;
    public String valeur;
    public String pourcentage;
    public String total;

    public String dollars;


    public CustomItem(String nom, String valeur, String pourcentage, String total,String dollars ) {
        this.nom = nom;
        this.valeur = valeur;
        this.pourcentage = pourcentage;
        this.total = total;
        this.dollars=dollars;
    }
}
