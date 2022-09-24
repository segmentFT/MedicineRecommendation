package com.medicinerecommendation.android

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MedicineDatabaseOpenHelper(context: Context, name: String, version: Int):
    SQLiteOpenHelper(context, name, null, version) {

    private val herbs = "create table herbs (" +
            "id integer primary key autoincrement," +
            "name text," +
            "alias text," +
            "ingredient text," +
            "process text," +
            "distinguish text," +
            "effect text," +
            "creation text," +
            "function text," +
            "taste text," +
            "usage text," +
            "compatibility text," +
            "storage text," +
            "side_effects text," +
            "category text," +
            "plant text," +
            "application text," +
            "position text," +
            "source text)"

    private val medicineClassifications = "create table classifications (" +
            "id integer primary key autoincrement," +
            "name text," +
            "classification text)"

    private val medicineSimilarity = "create table similarities (" +
            "id integer primary key autoincrement," +
            "name text," +
            "similar_medicine text," +
            "similarity real)"

    private val medicines = "create table medicines (" +
            "name text," +
            "ingredient text)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(herbs)
        db.execSQL(medicineClassifications)
        db.execSQL(medicineSimilarity)
        db.execSQL(medicines)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}