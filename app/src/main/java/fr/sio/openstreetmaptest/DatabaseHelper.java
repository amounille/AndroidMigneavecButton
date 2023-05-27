package fr.sio.openstreetmaptest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;


import java.util.ArrayList;
import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_QUESTION =
            "CREATE TABLE question (id INTEGER PRIMARY KEY, text TEXT)";

    private static final String CREATE_TABLE_ANSWER =
            "CREATE TABLE answer (id INTEGER PRIMARY KEY, question_id INTEGER, text TEXT, is_correct INTEGER, " +
                    "FOREIGN KEY(question_id) REFERENCES question(id))";

    private static final String CREATE_TABLE_COORDONEES  =
            "CREATE TABLE coordinates (id INTEGER PRIMARY KEY, latitude REAL, longitude REAL, nom TEXT, description TEXT)";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_ANSWER);
        db.execSQL(CREATE_TABLE_COORDONEES);

        // Insert some sample data
        ContentValues values = new ContentValues();
        values.put("text", "Qu'elle est la capital de la France?");
        long questionId = db.insert("question", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "Paris");
        values.put("is_correct", 1);
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "Londre");
        values.put("is_correct", 0);
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "Berlin");
        values.put("is_correct", 0);
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId);
        values.put("text", "Rome");
        values.put("is_correct", 0);
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("text", "Qu'elle est la capital de l'espagne ?");
        long questionId2 = db.insert("question", null, values);

        values = new ContentValues();
        values.put("question_id", questionId2);
        values.put("text", "Madrid");
        values.put("is_correct", 1);
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId2);
        values.put("text", "Londre");
        values.put("is_correct", 0);
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId2);
        values.put("text", "Berlin");
        values.put("is_correct", 0);
        db.insert("answer", null, values);

        values = new ContentValues();
        values.put("question_id", questionId2);
        values.put("text", "Rome");
        values.put("is_correct", 0);
        db.insert("answer", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Quiz DB

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

    // Fin Quiz DB
    // Coordonnees DB
    public void ajouterCoordonnees(double latitude, double longitude, String nom, String description) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("nom", nom);
        values.put("description", description);

        db.insert("coordinates", null, values);
        db.close();
    }

    public List<Coordonnee> getCoordinates() {
        List<Coordonnee> coordonnees = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {"latitude", "longitude", "nom", "description"};
        Cursor cursor = db.query("coordinates", projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow("nom"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            coordonnees.add(new Coordonnee(latitude, longitude, nom, description));
        }

        cursor.close();
        db.close();

        return coordonnees;
    }
    // verfie si les coordonée sont deja dans la bdd
    public boolean coordExist(double latitude, double longitude) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM coordinates WHERE latitude = ? AND longitude = ?";
        String[] selectionArgs = {String.valueOf(latitude), String.valueOf(longitude)};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exist = cursor.moveToFirst();
        cursor.close();

        return exist;
    }

    //Fin Coordonnees DB
}

