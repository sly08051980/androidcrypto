package com.example.crypto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AjoutActivity extends AppCompatActivity {

    private EditText editTextNom, editTextTrigramme, editTextStableCoin;
    private RadioGroup radioGroupExchanges;
    private RadioButton radioButtonBinance, radioButtonAutre;
    private Button buttonAjouter;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout);

        editTextNom = findViewById(R.id.editText_nom);
        editTextTrigramme = findViewById(R.id.editText_trigramme);
        editTextStableCoin = findViewById(R.id.editText_stableCoin);
        radioGroupExchanges = findViewById(R.id.radioGroup_exchanges);
        radioButtonBinance = findViewById(R.id.radioButton_binance);
        radioButtonAutre = findViewById(R.id.radioButton_autre);
        buttonAjouter = findViewById(R.id.button_ajouter);

        dbHelper = new DBHelper(this);

        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = editTextNom.getText().toString().trim(); // supprime les espaces en début et fin de chaîne
                String trigramme = editTextTrigramme.getText().toString().trim(); // supprime les espaces en début et fin de chaîne
                String stablecoin = editTextStableCoin.getText().toString();
                String exchange = radioButtonBinance.isChecked() ? "Binance" : "Autre";

                if ("Autre".equals(exchange)) {

                    nom = nom.replaceAll("\\s+", "-"); // remplace les espaces par des tirets
                }

                if ("Binance".equals(exchange)) {
                    trigramme = trigramme.trim(); // supprime les espaces en fin de chaîne
                }

                dbHelper.ajouterUtilisateur(nom, trigramme, stablecoin, exchange);

                Toast.makeText(AjoutActivity.this, "Utilisateur ajouté à la base de données", Toast.LENGTH_SHORT).show();

                editTextNom.setText("");
                editTextTrigramme.setText("");
                editTextStableCoin.setText("");
                radioButtonBinance.setChecked(true);
            }
        });
    }
}

