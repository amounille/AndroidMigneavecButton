package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class QuizzActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout questionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        dbHelper = new DatabaseHelper(this);
        questionContainer = findViewById(R.id.question_container);

        List<Question> questions = dbHelper.getAllQuestions();

        for (Question question : questions) {
            // Créer la vue de question et de réponse
            View questionView = getLayoutInflater().inflate(R.layout.question_layout, null);
            TextView questionTextView = questionView.findViewById(R.id.question_textview);
            questionTextView.setText(question.getText());
            RadioGroup answersRadioGroup = questionView.findViewById(R.id.answers_radiogroup);

            // Ajouter les réponses à la vue de question
            List<Answer> answers = dbHelper.getAnswersForQuestion(question.getId());
            for (Answer answer : answers) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(answer.getText());
                radioButton.setTag(answer.getId());
                answersRadioGroup.addView(radioButton);
            }

            // Ajouter la vue de question et de réponse au conteneur
            questionContainer.addView(questionView);
        }

        Button validateButton = findViewById(R.id.validate_button);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedAnswerId = getSelectedAnswerId();
                if (selectedAnswerId == -1) {
                    Toast.makeText(QuizzActivity.this, "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isAnswerCorrect = dbHelper.isAnswerCorrect(selectedAnswerId);
                if (isAnswerCorrect) {
                    Toast.makeText(QuizzActivity.this, "Bonne réponse !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuizzActivity.this, "Mauvaise réponse...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private int getSelectedAnswerId() {
        for (int i = 0; i < questionContainer.getChildCount(); i++) {
            View questionView = questionContainer.getChildAt(i);
            RadioGroup answersRadioGroup = questionView.findViewById(R.id.answers_radiogroup);
            int selectedAnswerId = answersRadioGroup.getCheckedRadioButtonId();
            if (selectedAnswerId != -1) {
                return (int) answersRadioGroup.findViewById(selectedAnswerId).getTag();
            }
        }
        return -1;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
