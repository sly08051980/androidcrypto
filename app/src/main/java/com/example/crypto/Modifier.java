package com.example.crypto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.crypto.DBHelper.*;

public class Modifier extends AppCompatActivity {
    private EditText editTextNom;
    private EditText editTextTrigramme;
    private EditText editTextStablecoin;
    private EditText editTextExchange;
    private RadioButton radioButtonBinance, radioButtonAutre;

    private Button supp,modif;


    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier);

        // Récupérer les vues
        editTextNom = findViewById(R.id.editTextNom);
        editTextTrigramme = findViewById(R.id.editTextTrigramme);
        editTextStablecoin = findViewById(R.id.editTextStablecoin);
        editTextExchange = findViewById(R.id.editTextExchange);
        radioButtonBinance = findViewById(R.id.radioButton_binance);
        radioButtonAutre=findViewById(R.id.radioButton_autre);
        supp=findViewById(R.id.supprimer);
        modif=findViewById(R.id.modifier);


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


       String tri = getIntent().getStringExtra("NOM");



        String[] projection = {DBHelper.COLUMN_NOM, DBHelper.COLUMN_TRIGRAMME, DBHelper.COLUMN_STABLECOIN, DBHelper.COLUMN_EXCHANGE};
        String selection = DBHelper.COLUMN_NOM + "=?";
        String[] selectionArgs = {tri};

        Cursor cursor = db.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);


        if (cursor.moveToFirst()) {
            editTextNom.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOM)));
            editTextTrigramme.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TRIGRAMME)));
            editTextStablecoin.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_STABLECOIN)));
            editTextExchange.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EXCHANGE)));

        }


        cursor.close();
        db.close();
String bi = editTextExchange.getText().toString();
        if (bi.equals("Binance")){
            radioButtonBinance.setChecked(true);
        }else if(bi.equals("Autre")){
            radioButtonAutre.setChecked(true);
        }

        modif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper dbHelper = new DBHelper(Modifier.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                String nom = editTextNom.getText().toString();
                String trigramme = editTextTrigramme.getText().toString();
                String stablecoin = editTextStablecoin.getText().toString();
                String exchange = "";
                if (radioButtonBinance.isChecked()) {
                    exchange = "Binance";
                } else if (radioButtonAutre.isChecked()) {
                    exchange = "Autre";
                }


                ContentValues values = new ContentValues();
                values.put(DBHelper.COLUMN_TRIGRAMME, trigramme);
                values.put(DBHelper.COLUMN_STABLECOIN, stablecoin);
                values.put(DBHelper.COLUMN_EXCHANGE, exchange);
                String selection = DBHelper.COLUMN_NOM + "=?";
                String[] selectionArgs = {nom};
                int count = db.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);


                db.close();


                Toast.makeText(Modifier.this, count + " enregistrement modifié", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(Modifier.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Modifier.this);
                builder.setMessage("Voulez-vous vraiment supprimer cet enregistrement ?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Ouvrir la base de données

                                DBHelper dbHelper = new DBHelper(Modifier.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                // Récupére le nom de l'enregistrement à supprimer
                                String nom = editTextNom.getText().toString();

                                // Supprime l'enregistrement dans la base de données
                                String selection = DBHelper.COLUMN_NOM + "=?";
                                String[] selectionArgs = {nom};
                                int count = db.delete(DBHelper.TABLE_NAME, selection, selectionArgs);


                                db.close();


                                Toast.makeText(Modifier.this, count + " enregistrement supprimé", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(Modifier.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });



    }


}
