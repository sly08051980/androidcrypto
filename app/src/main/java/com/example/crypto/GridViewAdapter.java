package com.example.crypto;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
   private List<GridViewItem> items;
   private LayoutInflater inflater;

   public GridViewAdapter(Context context, List<GridViewItem> items) {
      this.items = items;
      this.inflater = LayoutInflater.from(context);
   }

   @Override
   public int getCount() {
      return items.size();
   }

   @Override
   public Object getItem(int position) {
      return items.get(position);
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      if (view == null) {
         view = inflater.inflate(R.layout.grid, parent, false);
      }

      // Récupérez les champs texte de la vue
      TextView text1 = view.findViewById(R.id.text1);
      TextView text2 = view.findViewById(R.id.text2);
      TextView text3 = view.findViewById(R.id.text3);
      TextView text4 = view.findViewById(R.id.text4);
      TextView pourcentagetextview = view.findViewById(R.id.pourcentageTextView);

      // Récupérez l'élément GridView correspondant à cette position
      GridViewItem item = (GridViewItem) getItem(position);

      // Définissez les champs texte avec les valeurs correspondantes de l'élément GridView
      text1.setText(item.getText1());
      text2.setText(item.getText2());
      text3.setText(item.getText3());
      text4.setText(item.getText4());


      return view;
   }
}

