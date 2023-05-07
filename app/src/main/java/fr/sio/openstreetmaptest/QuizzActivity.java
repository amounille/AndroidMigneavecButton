package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
