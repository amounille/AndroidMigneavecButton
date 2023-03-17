package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuizzActivity extends AppCompatActivity implements View.OnClickListener{
    Button bValider;
    Button bRetour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        bValider = findViewById(R.id.Valider1);
        bRetour = findViewById(R.id.Retour);
        bValider.setOnClickListener(this);
        bRetour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bValider){
            Intent i = new Intent(this, reponseActivity.class);
            startActivity(i);
        }
        else if (v == bRetour){ finish(); }
    }
}