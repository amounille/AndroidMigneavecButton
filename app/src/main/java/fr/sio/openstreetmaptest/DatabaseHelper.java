package fr.sio.openstreetmaptest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quizz.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_QUESTION =
            "CREATE TABLE question (id INTEGER PRIMARY KEY, text TEXT)";

    private static final String CREATE_TABLE_ANSWER =
            "CREATE TABLE answer (id INTEGER PRIMARY KEY, question_id INTEGER, text TEXT, " +
                    "FOREIGN KEY(question_id) REFERENCES question(id))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_ANSWER);

        // Insert some sample data
        ContentValues values = new ContentValues();
        values.put("text", "What is the capital of France?");
        long questionId = db.insert("question", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "Paris");
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "London");
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "Berlin");
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "Rome");
        db.insert("answer", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade policy not implemented
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("question", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String text = cursor.getString(cursor.getColumnIndex("text"));
            Question question = new Question(id, text);
            questions.add(question);
        }
        cursor.close();
        db.close();
        return questions;
    }

    public List<Answer> getAnswersForQuestion(int questionId) {
        List<Answer> answers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = "question_id=?";
        String[] selectionArgs = {String.valueOf(questionId)};
        Cursor cursor = db.query("answer", null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String text = cursor.getString(cursor.getColumnIndex("text"));
            Answer answer = new Answer(id, questionId, text);
            answers.add(answer);
        }
        cursor.close();
        db.close();
        return answers;
    }
}

