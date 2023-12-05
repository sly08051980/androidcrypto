package com.example.crypto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

   private static final String DATABASE_NAME = "mydatabase.db";
   private static final int DATABASE_VERSION = 1;

   // Définir les noms de table et les colonnes
   public static final String TABLE_NAME = "users";
   private static final String COLUMN_ID = "_id";
   public static final String COLUMN_NOM = "nom";
   public static final String COLUMN_TRIGRAMME = "trigramme";
   public static final String COLUMN_STABLECOIN = "stablecoin";
   public static final String COLUMN_EXCHANGE = "exchange";

   // Définir la requête SQL pour créer la table
   private static final String SQL_CREATE_TABLE =
           "CREATE TABLE " + TABLE_NAME + " (" +
                   COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   COLUMN_NOM + " TEXT, " +
                   COLUMN_TRIGRAMME + " TEXT, " +
                   COLUMN_STABLECOIN + " TEXT, " +
                   COLUMN_EXCHANGE + " TEXT);";


   // Constructeur
   public DBHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   // Méthodes pour la création et la mise à jour de la base de données
   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // Code pour la mise à jour de la base de données (si nécessaire)
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      onCreate(db);
   }

   // Méthodes pour ajouter et récupérer des données
   public void ajouterUtilisateur(String nom, String trigramme, String stablecoin, String exchange) {
      SQLiteDatabase db = getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(COLUMN_NOM, nom);
      values.put(COLUMN_TRIGRAMME, trigramme);
      values.put(COLUMN_STABLECOIN, stablecoin);
      values.put(COLUMN_EXCHANGE, exchange);
      db.insert(TABLE_NAME, null, values);
      db.close();
   }

   public Cursor obtenirTousLesUtilisateurs() {
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
      return cursor;
   }


   public List<MyData> getAllData() {
      List<MyData> dataList = new ArrayList<>();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

      if (cursor.moveToFirst()) {
         do {
            MyData data = new MyData(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOM)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TRIGRAMME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_STABLECOIN)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EXCHANGE)),
                    "","","","","",""
            );
            dataList.add(data);
         } while (cursor.moveToNext());
      }

      cursor.close();
      db.close();
      return dataList;
   }
}