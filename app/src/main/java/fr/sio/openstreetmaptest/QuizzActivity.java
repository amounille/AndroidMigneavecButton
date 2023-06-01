package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class QuizzActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private Cursor cursor;
    private int currentQuestionIndex = 0;
    private TextView questionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        dbHelper = new DatabaseHelper(this);
        questionTextView = findViewById(R.id.question_text); // Assurez-vous que vous avez un TextView avec l'ID "question_textview" dans votre layout

        startQuiz(); // Ajoutez cet appel pour démarrer le quiz
    }


    // Déclarez ces variables en tant que membres de votre classe


    // Méthode pour initialiser la base de données et le curseur
    private void initDatabase() {
        // Initialisez votre base de données SQLite et obtenez une référence à la base de données
        database = dbHelper.getReadableDatabase();

        // Récupérez les questions de la base de données dans le curseur
        String query = "SELECT * FROM question";
        cursor = database.rawQuery(query, null);
    }

    // Méthode pour afficher la question actuelle
    private void displayCurrentQuestion() {
        if (cursor.moveToPosition(currentQuestionIndex)) {
            @SuppressLint("Range") String questionText = cursor.getString(cursor.getColumnIndexOrThrow("text"));
            questionTextView.setText(questionText);

            @SuppressLint("Range") int questionId = cursor.getInt(cursor.getColumnIndex("id"));
            List<Answer> answers = dbHelper.getAnswersForQuestion(questionId);


            LinearLayout questionContainer = findViewById(R.id.question_container);
            questionContainer.removeAllViews();

            for (Answer answer : answers) {
                RadioButton answerRadioButton = new RadioButton(this);
                answerRadioButton.setText(answer.getText());
                questionContainer.addView(answerRadioButton);
            }
        }
    }
    // Méthode pour vérifier la réponse de l'utilisateur + aide
    public void submitAnswer(View view) {
        if (cursor.moveToPosition(currentQuestionIndex)) {
            @SuppressLint("Range") long questionId = cursor.getLong(cursor.getColumnIndex("id"));

            LinearLayout questionContainer = findViewById(R.id.question_container);
            RadioButton selectedRadioButton = null;
            for (int i = 0; i < questionContainer.getChildCount(); i++) {
                View childView = questionContainer.getChildAt(i);
                if (childView instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) childView;
                    if (radioButton.isChecked()) {
                        selectedRadioButton = radioButton;
                        break;
                    }
                }
            }

            if (selectedRadioButton != null) {
                String selectedAnswer = selectedRadioButton.getText().toString();
                boolean isCorrect = checkAnswer(questionId, selectedAnswer);

                // Affiche un toast indiquant si la réponse est correcte ou incorrecte
                if (isCorrect) {
                    showToast("Bonne réponse!");
                } else {
                    showToast("Mauvaise réponse!");
                }

                // Passe à la question suivante
                moveToNextQuestion();
            } else {
                // Aucune réponse sélectionnée, affichez un message d'erreur
                showToast("Veuillez sélectionner une réponse");
            }
        }
    }

    // Méthode pour vérifier si la réponse sélectionnée est correcte
    private boolean checkAnswer(long questionId, String selectedAnswer) {
        // Ouvrez la base de données
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        try {
            String query = "SELECT is_correct FROM answer WHERE id = ? AND text = ?";
            Cursor answerCursor = database.rawQuery(query, new String[]{String.valueOf(questionId), selectedAnswer});

            boolean isCorrect = false;
            if (answerCursor.moveToFirst()) {
                @SuppressLint("Range") int isCorrectValue = answerCursor.getInt(answerCursor.getColumnIndex("is_correct"));
                isCorrect = (isCorrectValue == 1);
            }

            answerCursor.close();
            return isCorrect;
        } finally {
            // Fermez la base de données
            database.close();
        }
    }

    // Méthode pour passer à la question suivante
    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < cursor.getCount()) {
            displayCurrentQuestion();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    // aide
    public void aide(View view) {
        if (cursor.moveToPosition(currentQuestionIndex)) {
            @SuppressLint("Range") String aideText = cursor.getString(cursor.getColumnIndexOrThrow("aide"));
            showToast(aideText);
        }
    }


    // Méthode pour afficher un toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Appelez cette méthode lorsque vous êtes prêt à commencer le quiz
    private void startQuiz() {
        initDatabase();
        displayCurrentQuestion();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (database != null) {
            database.close();
        }
    }


}