package com.example.crypto;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

   private List<MyData> dataList;
   public double totalDollars = 0;


   public CustomAdapter(List<MyData> dataList) {
      this.dataList = dataList;


   }

   @NonNull
   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      // create a new view
      View v = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.custom_item_layout, parent, false);

      MyViewHolder vh = new MyViewHolder(v);
      return vh;
   }

   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      MyData data = dataList.get(position);
      holder.textViewNom.setText(data.getNom());
      holder.textViewValeur.setText(data.getValeur());
      holder.textViewNombre.setText(data.getPourcentage());
      holder.textViewBinance.setText(data.getTotal());



      if (holder.textViewBinance.getText().toString().equals("Binance")) {
         // Volley Request
         String url = "https://api.binance.com/api/v3/ticker/24hr?symbol=" + data.getValeur() + "USDT";

         RequestQueue queue = Volley.newRequestQueue(holder.itemView.getContext());

         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                 (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       try {
                          double priceChangePercent = response.getDouble("priceChangePercent");
                          double lastPrice = response.getDouble("lastPrice");

                          holder.pourcentageTextView.setText("" + priceChangePercent + "%");
                          holder.montantTextView.setText("" + lastPrice);
                          double pourcentage = Double.parseDouble(data.getPourcentage());


                          double dollars = pourcentage * lastPrice;
                          String dollarsString = String.format("%.2f", dollars);
                          holder.dollarsTextView.setText(dollarsString);
                          totalDollars += dollars;


                          if (priceChangePercent < 0) {
                             holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                          } else {
                             holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                          }

                       } catch (JSONException e) {
                          e.printStackTrace();
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
                    }
                 });

         try {
            queue.add(jsonObjectRequest);
         } catch (Exception e) {
            e.printStackTrace();
         }
      } else {
         RequestQueue queue = Volley.newRequestQueue(holder.itemView.getContext());
         String valeur = data.getNom().toLowerCase();
         String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + valeur;

         StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                 new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                          JSONArray jsonArray = new JSONArray(response);
                          JSONObject jsonObject = jsonArray.getJSONObject(0);

                          String symbol = jsonObject.getString("symbol");
                          String price = jsonObject.getString("current_price");
                          String pourc = jsonObject.getString("price_change_percentage_24h");
                           holder.pourcentageTextView.setText(pourc);
                          // Vérifier si pourc et price ne sont pas null
                          if (pourc != "null" && price != "null"  ) {
                             pourc = String.format("%.3f", Double.parseDouble(pourc)) + "%";

                             holder.montantTextView.setText(price);
                             holder.pourcentageTextView.setText(pourc);

                             double pourcentage = Double.parseDouble(data.getPourcentage());
                             double price1 = Double.parseDouble(price);
                             double dollars = pourcentage * price1;
                             String dollarsString = String.format("%.2f", dollars);
                             holder.dollarsTextView.setText(dollarsString);
                             pourc = pourc.replaceAll("%", "");
                             pourc = pourc.replaceAll(",", ".");
                             totalDollars += dollars;
                             double pourc1 = Double.parseDouble(pourc);
                             if (pourc1 < 0) {
                                holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                             } else {
                                holder.linearLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                             }
                          }
                       } catch (JSONException e) {
                          e.printStackTrace();
                       }

                    }
                 }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
         });

         queue.add(stringRequest);





      }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // Récupérer l'ID de l'élément cliqué
               String nom = data.getNom();
               // Créer un intent pour démarrer l'activité de modification avec l'ID en extra
               Intent intent = new Intent(holder.itemView.getContext(), Modifier.class);
               intent.putExtra("NOM", nom);
               holder.itemView.getContext().startActivity(intent);
           }
       });
   }


   @Override
   public int getItemCount() {
      return dataList.size();
   }

   public static class MyViewHolder extends RecyclerView.ViewHolder {
      public TextView textViewNom, textViewValeur, textViewNombre, textViewBinance, pourcentageTextView, montantTextView,dollarsTextView,totalMontant;
      public LinearLayout linearLayout;


      public MyViewHolder(View itemView) {
         super(itemView);
         textViewNom = itemView.findViewById(R.id.nomTextView);
         textViewValeur = itemView.findViewById(R.id.valeurTextView);
         textViewNombre = itemView.findViewById(R.id.nombreTextView);
         textViewBinance = itemView.findViewById(R.id.binanceTextView);
         pourcentageTextView = itemView.findViewById(R.id.poucentageTextView);
         montantTextView = itemView.findViewById(R.id.montantTextView);
         dollarsTextView=itemView.findViewById(R.id.dollarTextView);
         linearLayout = itemView.findViewById(R.id.linearLayout);


      }
   }
   public void setDataList(List<MyData> dataList) {
      this.dataList = dataList;
      notifyDataSetChanged();
   }
   public interface OnItemClickListener {
      void onItemClick(MyData data);
   }

   public double getTotalDollars() {
      return totalDollars;
   }
   public void resetTotalDollars() {
      totalDollars = 0.0;
   }
}