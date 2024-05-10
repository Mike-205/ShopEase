package com.example.shopease

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopease.model.CategoryModel
import com.example.shopease.model.DbCartProductModel
import com.example.shopease.model.DbFavoriteModel
import com.example.shopease.model.ProfileModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "shopease.db"

        private const val TABLE_USERS = "users"
        private const val TABLE_CATEGORIES = "categories"
        private const val TABLE_CART_PRODUCT = "cart_product"
        private const val TABLE_FAVORITE = "favorite"

        private const val USER_COLUMN_ID = "id"
        private const val USER_COLUMN_USERNAME = "name"
        private const val USER_COLUMN_EMAIL = "email"
        private const val USER_COLUMN_PASSWORD = "password"

        private const val CATEGORIES_COLUMN_ID = "id"
        private const val CATEGORIES_COLUMN_NAME = "name"
        private const val CATEGORIES_COLUMN_IMAGERESID = "imageResId"

        private const val CART_PRODUCT_COLUMN_ID = "id"
        private const val CART_PRODUCT_COLUMN_IMAGERESID = "imageResId"
        private const val CART_PRODUCT_COLUMN_PRODUCTNAME = "productName"
        private const val CART_PRODUCT_COLUMN_SUBTOTAL = "subtotal"
        private const val CART_PRODUCT_COLUMN_TOTAL = "total"
        private const val CART_PRODUCT_COLUMN_QUANTITY = "quantity"
        private const val CART_PRODUCT_COLUMN_SIZE = "size"
        private const val CART_PRODUCT_COLUMN_EMAIL = "email"

        private const val FAVORITE_COLUMN_ID = "id"
        private const val FAVORITE_COLUMN_PRODUCTNAME = "productName"
        private const val FAVORITE_COLUMN_PRICE = "price"
        private const val FAVORITE_COLUMN_RATING = "rating"
        private const val FAVORITE_COLUMN_IMAGERESID = "imageResId"
        private const val FAVORITE_COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_USERS ($USER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_COLUMN_USERNAME TEXT, $USER_COLUMN_EMAIL TEXT UNIQUE, $USER_COLUMN_PASSWORD TEXT)"
        val createCategoriesTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIES ($CATEGORIES_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $CATEGORIES_COLUMN_NAME TEXT UNIQUE, $CATEGORIES_COLUMN_IMAGERESID INTEGER)"
        val createCartProductTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_CART_PRODUCT ($CART_PRODUCT_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $CART_PRODUCT_COLUMN_IMAGERESID INT, $CART_PRODUCT_COLUMN_PRODUCTNAME TEXT, $CART_PRODUCT_COLUMN_SUBTOTAL DOUBLE, $CART_PRODUCT_COLUMN_TOTAL DOUBLE, $CART_PRODUCT_COLUMN_QUANTITY INT, $CART_PRODUCT_COLUMN_SIZE TEXT, $CART_PRODUCT_COLUMN_EMAIL TEXT)"
        val createFavoriteTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_FAVORITE ($FAVORITE_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $FAVORITE_COLUMN_PRODUCTNAME TEXT, $FAVORITE_COLUMN_PRICE DOUBLE, $FAVORITE_COLUMN_RATING DOUBLE, $FAVORITE_COLUMN_IMAGERESID INT, $FAVORITE_COLUMN_EMAIL TEXT)"
        db.execSQL(createUserTableQuery)
        db.execSQL(createCategoriesTableQuery)
        db.execSQL(createCartProductTableQuery)
        db.execSQL(createFavoriteTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART_PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE")
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

    fun getCategoryByName(name: String): CategoryModel? {
        val db = this.readableDatabase
        val projection = arrayOf(CATEGORIES_COLUMN_ID, CATEGORIES_COLUMN_NAME, CATEGORIES_COLUMN_IMAGERESID)
        val selection = "$CATEGORIES_COLUMN_NAME = ?"
        val selectionArgs = arrayOf(name)

        val cursor = db.query(
            TABLE_CATEGORIES,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORIES_COLUMN_NAME))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORIES_COLUMN_IMAGERESID))
            cursor.close()
            CategoryModel(categoryName, imageResId)
        } else {
            cursor.close()
            null
        }
    }

    // Create a function to insert a category into the database

    fun insertCategory(name: String, imageUrl: String): Long {
        // First, check if the category item already exists
        val existingCategory = getCategoryByName(name)
        if (existingCategory != null) {
            // Category already exists, return -1 to indicate failure
            return -1
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(CATEGORIES_COLUMN_NAME, name)
            put(CATEGORIES_COLUMN_IMAGERESID, imageUrl)
        }
        return db.insert(TABLE_CATEGORIES, null, values)
    }

    fun insertCartProduct(imageResId: Int, productName: String, subTotal: Double, total: Double, quantity: Int, size: String, email: String): Long {
        // First, check if the user already exists
        val existingCartProduct = getCartProductByEmail(email)
        if (existingCartProduct != null) {
            // User already exists, return -1 to indicate failure
            return -1
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(CART_PRODUCT_COLUMN_IMAGERESID, imageResId)
            put(CART_PRODUCT_COLUMN_PRODUCTNAME, productName)
            put(CART_PRODUCT_COLUMN_SUBTOTAL, subTotal)
            put(CART_PRODUCT_COLUMN_TOTAL, total)
            put(CART_PRODUCT_COLUMN_QUANTITY, quantity)
            put(CART_PRODUCT_COLUMN_SIZE, size)
            put(CART_PRODUCT_COLUMN_EMAIL, email)
        }
        return db.insert(TABLE_CART_PRODUCT  , null, values)
    }

    fun getCartProductByEmail(email: String): DbCartProductModel? {
        val db = this.readableDatabase
        val projection = arrayOf(CART_PRODUCT_COLUMN_IMAGERESID, CART_PRODUCT_COLUMN_PRODUCTNAME, CART_PRODUCT_COLUMN_SUBTOTAL, CART_PRODUCT_COLUMN_TOTAL, CART_PRODUCT_COLUMN_QUANTITY, CART_PRODUCT_COLUMN_SIZE, CART_PRODUCT_COLUMN_EMAIL)
        val selection = "$USER_COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_CART_PRODUCT,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(CART_PRODUCT_COLUMN_IMAGERESID))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCT_COLUMN_PRODUCTNAME))
            val subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(CART_PRODUCT_COLUMN_SUBTOTAL))
            val total = cursor.getDouble(cursor.getColumnIndexOrThrow(CART_PRODUCT_COLUMN_TOTAL))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(CART_PRODUCT_COLUMN_QUANTITY))
            val size = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCT_COLUMN_SIZE))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCT_COLUMN_EMAIL))
            cursor.close()
            DbCartProductModel(imageResId, productName, subTotal, total, quantity, size, userEmail)
        } else {
            cursor.close()
            null
        }
    }

    fun updateCartProduct(imageResId: Int, productName: String, subTotal: Double, total: Double, quantity: Int, size: String, email: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(CART_PRODUCT_COLUMN_IMAGERESID, imageResId)
            put(CART_PRODUCT_COLUMN_PRODUCTNAME, productName)
            put(CART_PRODUCT_COLUMN_SUBTOTAL, subTotal)
            put(CART_PRODUCT_COLUMN_TOTAL, total)
            put(CART_PRODUCT_COLUMN_QUANTITY, quantity)
            put(CART_PRODUCT_COLUMN_SIZE, size)
            put(CART_PRODUCT_COLUMN_EMAIL, email)
        }
        val selection = "$CART_PRODUCT_COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        return db.update(TABLE_CART_PRODUCT, values, selection, selectionArgs)
    }

    fun insertFavorite(productName: String, price: Double, rating: Double, imageResId: Int, email: String): Long {
        // First, check if the user already exists
        val existingFavorite = getFavoriteByEmailAndProductName(email, productName)
        if (existingFavorite != null) {
            // User already exists, return -1 to indicate failure
            return -1
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FAVORITE_COLUMN_PRODUCTNAME, productName)
            put(FAVORITE_COLUMN_PRICE, price)
            put(FAVORITE_COLUMN_RATING, rating)
            put(FAVORITE_COLUMN_IMAGERESID, imageResId)
            put(FAVORITE_COLUMN_EMAIL, email)
        }
        return db.insert(TABLE_FAVORITE  , null, values)
    }

    fun getFavoriteByEmailAndProductName(email: String, productNameSearch: String): DbFavoriteModel? {
        val db = this.readableDatabase
        val projection = arrayOf(CART_PRODUCT_COLUMN_IMAGERESID, CART_PRODUCT_COLUMN_PRODUCTNAME, CART_PRODUCT_COLUMN_SUBTOTAL, CART_PRODUCT_COLUMN_TOTAL, CART_PRODUCT_COLUMN_QUANTITY, CART_PRODUCT_COLUMN_SIZE, CART_PRODUCT_COLUMN_EMAIL)
        val selection = "$USER_COLUMN_EMAIL = ? AND $CART_PRODUCT_COLUMN_PRODUCTNAME = ?"
        val selectionArgs = arrayOf(email, productNameSearch)

        val cursor = db.query(
            TABLE_FAVORITE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(FAVORITE_COLUMN_PRODUCTNAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(FAVORITE_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(FAVORITE_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(FAVORITE_COLUMN_IMAGERESID))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(FAVORITE_COLUMN_EMAIL))
            cursor.close()
            DbFavoriteModel(productName, price, rating, imageResId, userEmail)
        } else {
            cursor.close()
            null
        }
    }

    fun updateFavorite(productName: String, price: Double, rating: Double, imageResId: Int, email: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(CART_PRODUCT_COLUMN_PRODUCTNAME, productName)
            put(CART_PRODUCT_COLUMN_SUBTOTAL, price)
            put(CART_PRODUCT_COLUMN_TOTAL, rating)
            put(CART_PRODUCT_COLUMN_IMAGERESID, imageResId)
            put(CART_PRODUCT_COLUMN_EMAIL, email)
        }
        val selection = "$CART_PRODUCT_COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        return db.update(TABLE_CART_PRODUCT, values, selection, selectionArgs)
    }
}