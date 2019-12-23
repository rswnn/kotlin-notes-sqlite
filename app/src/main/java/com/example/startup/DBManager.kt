package com.example.startup

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager {
    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion = 1

    // buat table

    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS "+dbTable+" ("+colID+" INTEGER PRIMARY KEY," + colTitle+" TEXT, "+colDes+" TEXT);"

    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context) {
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes:SQLiteOpenHelper {
        var context:Context?=null
        constructor(context: Context):super(context, dbName, null, dbVersion) {
            this.context = context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "database berhasil dibuat !", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS "+dbTable)
        }

    }


    fun Insert (values: ContentValues):Long {
        val ID = sqlDB!!.insert(dbTable,"",values)
        return ID
    }

    fun Delete ( selection: String, selectionArgs: Array<String>):Int {
        val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun Update (values: ContentValues, selection: String, selectionArgs: Array<String>):Int {
        var count =  sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }

    fun Query (projection:Array<String>, selection:String, selectionArgs: Array<String>, soOreder:String):Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        var cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, soOreder)
        return cursor
    }
}