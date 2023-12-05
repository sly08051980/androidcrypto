package com.example.crypto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nom") && intent.hasExtra("valeur") && intent.hasExtra("pourcentage") && intent.hasExtra("total")) {

            String nom = intent.getStringExtra("nom");
            String valeur = intent.getStringExtra("valeur");
            String pourcentage = intent.getStringExtra("pourcentage");
            String total = intent.getStringExtra("total");


            TextView nomTextView = findViewById(R.id.nomTextView);
            nomTextView.setText(nom);

            TextView valeurTextView = findViewById(R.id.valeurTextView);
            valeurTextView.setText(valeur);

            TextView pourcentageTextView = findViewById(R.id.pourcentageTextView);
            pourcentageTextView.setText(pourcentage);

            TextView totalTextView = findViewById(R.id.totalTextView);
            totalTextView.setText(total);
        }
    }
}
