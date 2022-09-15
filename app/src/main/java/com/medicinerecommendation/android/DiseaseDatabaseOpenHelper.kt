package com.medicinerecommendation.android

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DiseaseDatabaseOpenHelper(context: Context, databaseName: String, version: Int):
    SQLiteOpenHelper(context, databaseName,null, version) {

    private val diseases = "create table diseases (" +
            "id integer primary key autoincrement," +
            "disease_name text," +
            "introduction text," +
            "insurance text," +
            "alias text," +
            "infection text," +
            "people text," +
            "symptom text," +
            "complication text," +
            "cost text," +
            "ratio text," +
            "duration text," +
            "method text," +
            "examination text," +
            "medicine text)"

    private val diseaseToPosition = "create table disease_to_position (" +
            "id integer primary key autoincrement," +
            "disease_name text," +
            "position text)"

    private val diseaseToDepartment = "create table disease_to_department (" +
            "id integer primary key autoincrement," +
            "disease_name text," +
            "department text)"

    private val diseaseToMedicine = "create table disease_to_medicine (" +
            "id integer primary key autoincrement," +
            "disease_name text," +
            "medicine_name text," +
            "grade real," +
            "comment_count integer," +
            "secondary_comment_count integer)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(diseases)
        db.execSQL(diseaseToPosition)
        db.execSQL(diseaseToDepartment)
        db.execSQL(diseaseToMedicine)
    }

   override fun onUpgrade(db: SQLiteDatabase, oldVerion: Int, newVersion: Int) {

   }

}