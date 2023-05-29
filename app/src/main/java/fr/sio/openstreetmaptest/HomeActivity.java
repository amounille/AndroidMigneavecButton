package fr.sio.openstreetmaptest;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity  {
    private Button boutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textView = findViewById(R.id.textView8);
        TextView textView2 = findViewById(R.id.textView15);
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(Color.BLACK), 0, content.length(), 0);
        textView.setText(content);
        textView2.setText(content);
    }

    public void launchMainActivity(View view) {
        //permet de launch la 2eme actv
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent i = new Intent(this, MainActivity.class);
        // définir le request_code pour identifier le retour
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        //fin permet de launch la 2eme actv
    }


    public void launchQuizzActivity(View view) {
        //permet de launch la 2eme actv
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent i = new Intent(this, QuizzActivity.class);
        // définir le request_code pour identifier le retour
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        //fin permet de launch la 2eme actv
    }
}
