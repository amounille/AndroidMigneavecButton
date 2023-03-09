package fr.sio.openstreetmaptest;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button boutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout l = new LinearLayout(this);
        boutton = new Button(this);
        boutton.setText("Map");
        boutton.setOnClickListener(this);
        l.addView(boutton);
        setContentView(l);
    }

    @Override
    public void onClick(View v) {
        //permet de launch la 2eme actv
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent i = new Intent(this, MainActivity.class);
        // définir le request_code pour identifier le retour
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        //fin permet de launch la 2eme actv
    }
}
