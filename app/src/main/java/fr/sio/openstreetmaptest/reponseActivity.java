package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class reponseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponse);
        Button bValider = findViewById(R.id.Valider2);
        bValider.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
