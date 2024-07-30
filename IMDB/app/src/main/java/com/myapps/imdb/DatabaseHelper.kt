package com.myapps.imdb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SearchHistory.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_TITLE_RESULTS = "title_results"
        private const val COLUMN_ID = "id"
        private const val COLUMN_RESULT_ID = "_id"
        private const val COLUMN_MY_QUERY = "myQuery"
        private const val COLUMN_IMAGE_TYPE = "imageType"
        private const val COLUMN_TITLE_NAME_TEXT = "titleNameText"
        private const val COLUMN_URL = "url"
        private const val COLUMN_TITLE_RELEASE_TEXT = "titleReleaseText"
        private const val COLUMN_TITLE_TYPE_TEXT = "titleTypeText"
        private const val COLUMN_TOP_CREDITS = "topCredits"
        const val createTitleResultsTable = ("CREATE TABLE IF NOT EXISTS $TABLE_TITLE_RESULTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_RESULT_ID TEXT, "
                + "$COLUMN_MY_QUERY TEXT, "
                + "$COLUMN_IMAGE_TYPE TEXT, "
                + "$COLUMN_TITLE_NAME_TEXT TEXT, "
                + "$COLUMN_URL TEXT, "
                + "$COLUMN_TITLE_RELEASE_TEXT TEXT, "
                + "$COLUMN_TITLE_TYPE_TEXT TEXT, "
                + "$COLUMN_TOP_CREDITS TEXT)")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTitleResultsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_TITLE_RESULTS ADD COLUMN $COLUMN_MY_QUERY TEXT")
        }
    }

    fun insertTitleResult(myQuery: String, result: FindResponse.TitleResults.Result): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_RESULT_ID, result.id)
            put(COLUMN_MY_QUERY, myQuery)
            put(COLUMN_IMAGE_TYPE, result.imageType)
            put(COLUMN_TITLE_NAME_TEXT, result.titleNameText)
            put(COLUMN_URL, result.titlePosterImageModel?.url)
            put(COLUMN_TITLE_RELEASE_TEXT, result.titleReleaseText)
            put(COLUMN_TITLE_TYPE_TEXT, result.titleTypeText)
            val topCreditsJson = Gson().toJson(result.topCredits)
            put(COLUMN_TOP_CREDITS, topCreditsJson)
        }
        val result =db.insert(TABLE_TITLE_RESULTS, null, contentValues)
        db.close()
        return result
    }

    fun getTitleResultsByQuery(myQuery: String): List<FindResponse.TitleResults.Result> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_TITLE_RESULTS, null, "$COLUMN_MY_QUERY = ?", arrayOf(myQuery), null, null, null)
        val results = mutableListOf<FindResponse.TitleResults.Result>()
        val type = object : TypeToken<List<String>>() {}.type
        val gson = Gson()

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(COLUMN_RESULT_ID))
                val imageType = getString(getColumnIndexOrThrow(COLUMN_IMAGE_TYPE))
                val titleNameText = getString(getColumnIndexOrThrow(COLUMN_TITLE_NAME_TEXT))
                val url = getString(getColumnIndexOrThrow(COLUMN_URL))
                val titlePosterImageModel =
                    FindResponse.TitleResults.Result.TitlePosterImageModel(null, 0, 0, url)
                val titleReleaseText = getString(getColumnIndexOrThrow(COLUMN_TITLE_RELEASE_TEXT))
                val titleTypeText = getString(getColumnIndexOrThrow(COLUMN_TITLE_TYPE_TEXT))
                val topCreditsJson = getString(getColumnIndexOrThrow(COLUMN_TOP_CREDITS))
                val topCredits = gson.fromJson<List<String>>(topCreditsJson, type)
                results.add(FindResponse.TitleResults.Result(id, imageType, titleNameText, titlePosterImageModel, titleReleaseText, titleTypeText, topCredits))
            }
            close()
        }
        db.close()
        return results
    }
    fun getAllTitleNameTexts(): ArrayList<String> {
        val titleNameTexts = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT $COLUMN_MY_QUERY FROM $TABLE_TITLE_RESULTS", null)

        with(cursor) {
            while (moveToNext()) {
                val titleNameText = getString(getColumnIndexOrThrow(COLUMN_MY_QUERY))
                titleNameTexts.add(titleNameText)
            }
            close()
        }
        db.close()
        return titleNameTexts
    }


    fun clearTitleResults() {
        val db = this.writableDatabase
        db.delete(TABLE_TITLE_RESULTS, null, null)
        db.close()
    }
}
