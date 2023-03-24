package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuizzActivity extends AppCompatActivity implements View.OnClickListener{
    Button bValider;
    Button bRetour;
    Button bAide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        bValider = findViewById(R.id.Valider1);
        bRetour = findViewById(R.id.Retour);
        bAide = findViewById(R.id.aide);
        bValider.setOnClickListener(this);
        bRetour.setOnClickListener(this);
        bAide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bValider){
            Intent i = new Intent(this, reponseActivity.class);
            startActivity(i);
        }
        else if (v == bAide){
            Context context = getApplicationContext();
            String mess = bAide.getText().toString();
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, mess, duration).show();
        }
        else if (v == bRetour){ finish(); }
    }
}
