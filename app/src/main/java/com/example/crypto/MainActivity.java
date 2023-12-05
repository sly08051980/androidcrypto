package com.example.crypto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.common.api.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private FloatingActionButton fabajouter,fabmodifier;
    private DBHelper dbHelper;
    TextView totalDollarsTextView;
    private CastContext mCastContext;
private Toolbar toolbar;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            // Récupérer les données à partir de la base de données
            List<MyData> dataList = dbHelper.getAllData();
            // Mettre à jour la liste

            customAdapter.setDataList(dataList);
            // Programmer la prochaine exécution
            double totalDollars = customAdapter.getTotalDollars();
            String totalDollarsString = String.format("%.2f", totalDollars);
            totalDollarsTextView.setText("$" + totalDollarsString);
            handler.postDelayed(this, 10000);
customAdapter.resetTotalDollars();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
toolbar = findViewById(R.id.mytoolbar);
fabajouter=findViewById(R.id.fab_ajouter);
fabmodifier=findViewById(R.id.fab_modifier);
        dbHelper = new DBHelper(this);



        recyclerView = findViewById(R.id.recyclerView);


        // Set the RecyclerView layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Create and set the RecyclerView adapter



        customAdapter = new CustomAdapter(dbHelper.getAllData());

        recyclerView.setAdapter(customAdapter);
        totalDollarsTextView = findViewById(R.id.totalmontant1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<MyData> dataList = dbHelper.getAllData();
                // Mettre à jour la liste

                customAdapter.setDataList(dataList);
                // Programmer la prochaine exécution
                double totalDollars = customAdapter.getTotalDollars();
                String totalDollarsString = String.format("%.2f", totalDollars);
                totalDollarsTextView.setText("$" + totalDollarsString);
                customAdapter.resetTotalDollars();
            }
        }, 2000); // Délai d'une seconde en millisecondes



        fabajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something when the FloatingActionButton is clicked
                Intent intent = new Intent(MainActivity.this, AjoutActivity.class);
                startActivity(intent);
            }
        });


        fabmodifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenir le contexte CastContext

                }
        });





        // Programmation de la première exécution
        handler.postDelayed(runnable, 10000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Arrêter la répétition des tâches
        handler.removeCallbacks(runnable);
    }


}


