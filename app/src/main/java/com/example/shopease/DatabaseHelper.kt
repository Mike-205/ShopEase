package com.example.shopease

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopease.model.ProfileModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "shopease.db"
        private const val TABLE_USERS = "users"
        private const val TABLE_CATEGORIES = "categories"
        private const val USER_COLUMN_ID = "id"
        private const val USER_COLUMN_USERNAME = "name"
        private const val USER_COLUMN_EMAIL = "email"
        private const val USER_COLUMN_PASSWORD = "password"
        private const val CATEGORIES_COLUMN_ID = "id"
        private const val CATEGORIES_COLUMN_NAME = "name"
        private const val CATEGORIES_COLUMN_IMAGEURL = "imageUrl"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_USERS ($USER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_COLUMN_USERNAME TEXT, $USER_COLUMN_EMAIL TEXT UNIQUE, $USER_COLUMN_PASSWORD TEXT)"
        val createCategoriesTableQuery = "CREATE TABLE $TABLE_CATEGORIES ($CATEGORIES_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_COLUMN_USERNAME TEXT, $USER_COLUMN_EMAIL TEXT UNIQUE, $USER_COLUMN_PASSWORD TEXT)"
        db.execSQL(createTableQuery)
        db.execSQL(createCategoriesTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun insertUser(username: String, email: String, password: String): Long {
        // First, check if the user already exists
        val existingUser = getUserByEmail(email)
        if (existingUser != null) {
            // User already exists, return -1 to indicate failure
            return -1
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(USER_COLUMN_USERNAME, username)
            put(USER_COLUMN_EMAIL, email)
            put(USER_COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun getUserByEmail(email: String): ProfileModel? {
        val db = this.readableDatabase
        val projection = arrayOf(USER_COLUMN_ID, USER_COLUMN_USERNAME, USER_COLUMN_EMAIL, USER_COLUMN_PASSWORD)
        val selection = "$USER_COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_USERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(USER_COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_USERNAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_PASSWORD))
            cursor.close()
            ProfileModel(id, name, email, password)
        } else {
            cursor.close()
            null
        }
    }

    fun getUserByEmailAndPassword(email: String, password: String): ProfileModel? {
        val db = this.readableDatabase
        val projection = arrayOf(USER_COLUMN_ID, USER_COLUMN_USERNAME, USER_COLUMN_EMAIL, USER_COLUMN_PASSWORD)
        val selection = "$USER_COLUMN_EMAIL = ? AND $USER_COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(
            TABLE_USERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(USER_COLUMN_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_USERNAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_PASSWORD))
            cursor.close()
            ProfileModel(id, username, email, password)
        } else {
            cursor.close()
            null
        }
    }

    fun updateUser(id: Long, name: String, email: String, password: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(USER_COLUMN_USERNAME, name)
            put(USER_COLUMN_EMAIL, email)
            put(USER_COLUMN_PASSWORD, password)
        }
        val selection = "$USER_COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.update(TABLE_USERS, values, selection, selectionArgs)
    }
}