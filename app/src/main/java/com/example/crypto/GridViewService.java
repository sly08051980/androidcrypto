package com.example.crypto;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GridViewService extends RemoteViewsService {
   private DBHelper dbHelper;
   @Override
   public RemoteViewsFactory onGetViewFactory(Intent intent) {
      dbHelper = new DBHelper(this.getApplicationContext());
      List<MyData> dataList = dbHelper.getAllData();
      List<GridViewItem> items = new ArrayList<>();
      for (MyData data : dataList) {
         items.add(new GridViewItem(data.getNom(), data.getValeur(), data.getPourcentage(), data.getTotal()));
      }
      return new GridViewFactory(this.getApplicationContext(), items);
   }
}
class GridViewFactory implements RemoteViewsService.RemoteViewsFactory {
   private List<GridViewItem> items;
   private Context context;
   private DBHelper dbHelper;
   private Double priceChangePercent = null;
   private Double lastPrice=null;
   private String price;
   private String pourc;
   private String val1;
   public GridViewFactory(Context context, List<GridViewItem> items) {
      this.context = context;
      this.items = items;
   }
   @Override
   public void onCreate() {
      // Initialisez vos ressources ici si nécessaire
      dbHelper = new DBHelper(context);
   }
   @Override
   public void onDataSetChanged() {
      // Appelez cette méthode pour mettre à jour les données de la GridView
   }
   @Override
   public void onDestroy() {
      // Nettoyez vos ressources ici si nécessaire
   }
   @Override
   public int getCount() {
      // Retournez le nombre d'éléments dans votre liste
      return items.size();
   }
   @Override
   public RemoteViews getViewAt(int position) {
      // Créez et retournez la vue à afficher pour l'élément à la position donnée
      RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.grid);

      GridViewItem currentItem = items.get(position);
      row.setTextViewText(R.id.text1, currentItem.getText1());
      row.setTextViewText(R.id.text2, currentItem.getText2());
      row.setTextViewText(R.id.text3, currentItem.getText3());
      row.setTextViewText(R.id.text4, currentItem.getText4());
      String test = currentItem.getText4();
      String Val= currentItem.getText2();

      if(test.equals("Binance")) {
         String url = "https://api.binance.com/api/v3/ticker/24hr?symbol=" + Val + "USDT";
         RequestQueue queue = Volley.newRequestQueue(context);

         final CountDownLatch latch = new CountDownLatch(1); // créer un CountDownLatch initialisé à 1

         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                 (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try {
                          priceChangePercent = response.getDouble("priceChangePercent");
                          lastPrice = response.getDouble("lastPrice");
                          Log.d("GridViewFactory", "test = "+priceChangePercent+"%" );
                          row.setTextViewText(R.id.pourcentageTextview, String.valueOf(priceChangePercent) + "%");
row.setTextViewText(R.id.widgetmontant,String.valueOf(lastPrice)+"$");
                           if (priceChangePercent > 0) {
                               row.setInt(R.id.linearwidget, "setBackgroundResource", R.drawable.linearlayout_border);
                           }else if (priceChangePercent==0){
                               row.setInt(R.id.linearwidget, "setBackgroundResource", R.drawable.linearlayout_borderwhite);
                           }else if (priceChangePercent<0){
                               row.setInt(R.id.linearwidget, "setBackgroundResource", R.drawable.linearlayout_borderred);
                           }
                       } catch (JSONException e) {
                          e.printStackTrace();
                       } finally {
                          latch.countDown(); // décrémenter le CountDownLatch
                       }
                    }
                 }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                          Log.e("Error", "Bad request error 400");
                       } else {
                          Log.e("Error", error.getMessage(), error);
                       }
                       latch.countDown(); // décrémenter le CountDownLatch en cas d'erreur
                    }
                 });

         try {
            queue.add(jsonObjectRequest);
            latch.await(); // bloquer l'exécution jusqu'à ce que le CountDownLatch atteigne 0
         } catch (Exception e) {
            e.printStackTrace();
         }

         // définir un texte par défaut avant la réponse de la requête

         row.setTextViewText(R.id.pourcentageTextview, String.valueOf(priceChangePercent) + "%");
         row.setTextViewText(R.id.widgetmontant,String.valueOf(lastPrice)+"$");
         return row;

      }
       if (test.equals("Autre")) {
           RequestQueue queue = Volley.newRequestQueue(context);

           final CountDownLatch latch = new CountDownLatch(1);

           val1 = currentItem.getText1().toLowerCase();

           String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + val1;

           StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           try {
                               JSONArray jsonArray = new JSONArray(response);

                               JSONObject jsonObject = jsonArray.getJSONObject(0);
                               price = jsonObject.getString("current_price");
                               pourc = jsonObject.getString("price_change_percentage_24h");

                               Log.d("GridViewFactory", "test = "+pourc+"%" );
                               row.setTextViewText(R.id.pourcentageTextview, pourc + "%");
                               row.setTextViewText(R.id.widgetmontant,   price+ "$");


                               double pourcentage = Double.parseDouble(pourc);
                               if (pourcentage > 0) {
                                   row.setInt(R.id.linearwidget, "setBackgroundResource", R.drawable.linearlayout_border);
                               }else if (pourcentage==0){
                                   row.setInt(R.id.linearwidget, "setBackgroundResource", R.drawable.linearlayout_borderwhite);
                               }else if (pourcentage<0){
                                   row.setInt(R.id.linearwidget, "setBackgroundResource", R.drawable.linearlayout_borderred);
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                           } finally {
                               latch.countDown(); // décrémenter le CountDownLatch
                           }
                       }
                   }, new Response.ErrorListener() {

               @Override
               public void onErrorResponse(VolleyError error) {
                   if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                       Log.e("Error", "Bad request error 400");
                   } else {
                       Log.e("Error", error.getMessage(), error);
                   }
                   latch.countDown();
               }
           });

           try {
               queue.add(stringRequest);
               latch.await();
           } catch (Exception e) {
               e.printStackTrace();
           }
           row.setTextViewText(R.id.pourcentageTextview, pourc + "%");
           row.setTextViewText(R.id.widgetmontant, "$" + price);

           return row;
       }




       return row;
   }

   @Override
   public RemoteViews getLoadingView() {
      // Retournez une vue de chargement si nécessaire
      return null;
   }

   @Override
   public int getViewTypeCount() {
      // Retournez le nombre de types de vue différents dans la GridView
      return 1;
   }

   @Override
   public long getItemId(int position) {
      // Retournez l'ID de l'élément à la position donnée si vos éléments ont des ID uniques
      return position;
   }

   @Override
   public boolean hasStableIds() {
      // Retournez true si les IDs de vos éléments sont stables, c'est-à-dire s'ils ne changent pas même si les données changent
      return true;
   }
}
