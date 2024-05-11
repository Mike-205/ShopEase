package com.example.shopease

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopease.model.CategoryModel
import com.example.shopease.model.DbCartProductModel
import com.example.shopease.model.DbFavoriteModel
import com.example.shopease.model.DbOrderModel
import com.example.shopease.model.DbRecommendedModel
import com.example.shopease.model.ProfileModel
import com.example.shopease.model.RecommendedModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "shopease.db"

        private const val TABLE_USERS = "users"
        private const val TABLE_CATEGORIES = "categories"
        private const val TABLE_CART_PRODUCTS = "cart_products"
        private const val TABLE_FAVORITES = "favorites"
        private const val TABLE_ORDERS = "orders"
        private const val TABLE_PRODUCTS = "products"

        private const val USER_COLUMN_ID = "id"
        private const val USER_COLUMN_USERNAME = "name"
        private const val USER_COLUMN_EMAIL = "email"
        private const val USER_COLUMN_PASSWORD = "password"

        private const val CATEGORIES_COLUMN_ID = "id"
        private const val CATEGORIES_COLUMN_NAME = "name"
        private const val CATEGORIES_COLUMN_IMAGERESID = "imageResId"

        private const val CART_PRODUCTS_COLUMN_ID = "id"
        private const val CART_PRODUCTS_COLUMN_IMAGERESID = "imageResId"
        private const val CART_PRODUCTS_COLUMN_PRODUCTNAME = "productName"
        private const val CART_PRODUCTS_COLUMN_SUBTOTAL = "subtotal"
        private const val CART_PRODUCTS_COLUMN_TOTAL = "total"
        private const val CART_PRODUCTS_COLUMN_QUANTITY = "quantity"
        private const val CART_PRODUCTS_COLUMN_SIZE = "size"
        private const val CART_PRODUCTS_COLUMN_EMAIL = "email"

        private const val FAVORITES_COLUMN_ID = "id"
        private const val FAVORITES_COLUMN_PRODUCTNAME = "productName"
        private const val FAVORITES_COLUMN_PRICE = "price"
        private const val FAVORITES_COLUMN_RATING = "rating"
        private const val FAVORITES_COLUMN_IMAGERESID = "imageResId"
        private const val FAVORITES_COLUMN_EMAIL = "email"

        private const val ORDERS_COLUMN_ID = "id"
        private const val ORDERS_COLUMN_IMAGERESID = "imageResId"
        private const val ORDERS_COLUMN_PRODUCTNAME = "productName"
        private const val ORDERS_COLUMN_SUBTOTAL = "subtotal"
        private const val ORDERS_COLUMN_TOTAL = "total"
        private const val ORDERS_COLUMN_SIZE = "size"
        private const val ORDERS_COLUMN_QUANTITY = "quantity"
        private const val ORDERS_COLUMN_EMAIL = "email"

        private const val PRODUCTS_COLUMN_ID = "id"
        private const val PRODUCTS_COLUMN_PRODUCTNAME = "productName"
        private const val PRODUCTS_COLUMN_IMAGE = "image"
        private const val PRODUCTS_COLUMN_PRICE = "price"
        private const val PRODUCTS_COLUMN_RATING = "rating"
        private const val PRODUCTS_COLUMN_IMAGERESID = "imageResId"
        private const val PRODUCTS_COLUMN_CATEGORY = "category"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_USERS ($USER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_COLUMN_USERNAME TEXT, $USER_COLUMN_EMAIL TEXT UNIQUE, $USER_COLUMN_PASSWORD TEXT)"
        val createCategoriesTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIES ($CATEGORIES_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $CATEGORIES_COLUMN_NAME TEXT UNIQUE, $CATEGORIES_COLUMN_IMAGERESID INTEGER)"
        val createCartProductsTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_CART_PRODUCTS ($CART_PRODUCTS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $CART_PRODUCTS_COLUMN_IMAGERESID INT, $CART_PRODUCTS_COLUMN_PRODUCTNAME TEXT, $CART_PRODUCTS_COLUMN_SUBTOTAL DOUBLE, $CART_PRODUCTS_COLUMN_TOTAL DOUBLE, $CART_PRODUCTS_COLUMN_QUANTITY INT, $CART_PRODUCTS_COLUMN_SIZE TEXT, $CART_PRODUCTS_COLUMN_EMAIL TEXT)"
        val createFavoritesTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_FAVORITES ($FAVORITES_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $FAVORITES_COLUMN_PRODUCTNAME TEXT, $FAVORITES_COLUMN_PRICE DOUBLE, $FAVORITES_COLUMN_RATING DOUBLE, $FAVORITES_COLUMN_IMAGERESID INT, $FAVORITES_COLUMN_EMAIL TEXT)"
        val createOrdersTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_ORDERS ($ORDERS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $ORDERS_COLUMN_IMAGERESID INT, $ORDERS_COLUMN_PRODUCTNAME TEXT, $ORDERS_COLUMN_SUBTOTAL DOUBLE, $ORDERS_COLUMN_TOTAL DOUBLE, $ORDERS_COLUMN_SIZE TEXT, $ORDERS_COLUMN_QUANTITY INT, $ORDERS_COLUMN_EMAIL TEXT)"
        val createProductsTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_PRODUCTS ($PRODUCTS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $PRODUCTS_COLUMN_PRODUCTNAME TEXT, $PRODUCTS_COLUMN_IMAGE TEXT, $PRODUCTS_COLUMN_PRICE DOUBLE, $PRODUCTS_COLUMN_RATING DOUBLE, $PRODUCTS_COLUMN_IMAGERESID INT, $PRODUCTS_COLUMN_CATEGORY TEXT)"
        db.execSQL(createUserTableQuery)
        db.execSQL(createCategoriesTableQuery)
        db.execSQL(createCartProductsTableQuery)
        db.execSQL(createFavoritesTableQuery)
        db.execSQL(createOrdersTableQuery)
        db.execSQL(createProductsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
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

    fun getCategories() : MutableList<CategoryModel> {
        val db = this.readableDatabase
        val projection = arrayOf(CATEGORIES_COLUMN_NAME, CATEGORIES_COLUMN_IMAGERESID)

        val cursor = db.query(
            TABLE_CATEGORIES,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val categories = mutableListOf<CategoryModel>()
        while (cursor.moveToNext()) {
            val categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORIES_COLUMN_NAME))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORIES_COLUMN_IMAGERESID))
            categories.add(CategoryModel(categoryName, imageResId))
        }
        cursor.close()
        return categories
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
            put(CART_PRODUCTS_COLUMN_IMAGERESID, imageResId)
            put(CART_PRODUCTS_COLUMN_PRODUCTNAME, productName)
            put(CART_PRODUCTS_COLUMN_SUBTOTAL, subTotal)
            put(CART_PRODUCTS_COLUMN_TOTAL, total)
            put(CART_PRODUCTS_COLUMN_QUANTITY, quantity)
            put(CART_PRODUCTS_COLUMN_SIZE, size)
            put(CART_PRODUCTS_COLUMN_EMAIL, email)
        }
        return db.insert(TABLE_CART_PRODUCTS  , null, values)
    }

    fun getCartProductByEmail(email: String): DbCartProductModel? {
        val db = this.readableDatabase
        val projection = arrayOf(CART_PRODUCTS_COLUMN_IMAGERESID, CART_PRODUCTS_COLUMN_PRODUCTNAME, CART_PRODUCTS_COLUMN_SUBTOTAL, CART_PRODUCTS_COLUMN_TOTAL, CART_PRODUCTS_COLUMN_QUANTITY, CART_PRODUCTS_COLUMN_SIZE, CART_PRODUCTS_COLUMN_EMAIL)
        val selection = "$USER_COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_CART_PRODUCTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_IMAGERESID))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_PRODUCTNAME))
            val subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_SUBTOTAL))
            val total = cursor.getDouble(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_TOTAL))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_QUANTITY))
            val size = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_SIZE))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_EMAIL))
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
            put(CART_PRODUCTS_COLUMN_IMAGERESID, imageResId)
            put(CART_PRODUCTS_COLUMN_PRODUCTNAME, productName)
            put(CART_PRODUCTS_COLUMN_SUBTOTAL, subTotal)
            put(CART_PRODUCTS_COLUMN_TOTAL, total)
            put(CART_PRODUCTS_COLUMN_QUANTITY, quantity)
            put(CART_PRODUCTS_COLUMN_SIZE, size)
            put(CART_PRODUCTS_COLUMN_EMAIL, email)
        }
        val selection = "$CART_PRODUCTS_COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        return db.update(TABLE_CART_PRODUCTS, values, selection, selectionArgs)
    }

    fun getCartProducts() : MutableList<DbCartProductModel> {
        val db = this.readableDatabase
        val projection = arrayOf(CART_PRODUCTS_COLUMN_IMAGERESID, CART_PRODUCTS_COLUMN_PRODUCTNAME, CART_PRODUCTS_COLUMN_SUBTOTAL, CART_PRODUCTS_COLUMN_TOTAL, CART_PRODUCTS_COLUMN_QUANTITY, CART_PRODUCTS_COLUMN_SIZE, CART_PRODUCTS_COLUMN_EMAIL)

        val cursor = db.query(
            TABLE_CART_PRODUCTS,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val cartProducts = mutableListOf<DbCartProductModel>()
        while (cursor.moveToNext()) {
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_IMAGERESID))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_PRODUCTNAME))
            val subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_SUBTOTAL))
            val total = cursor.getDouble(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_TOTAL))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_QUANTITY))
            val size = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_SIZE))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(CART_PRODUCTS_COLUMN_EMAIL))
            cartProducts.add(DbCartProductModel(imageResId, productName, subTotal, total, quantity, size, userEmail))
        }
        cursor.close()
        return cartProducts
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
            put(FAVORITES_COLUMN_PRODUCTNAME, productName)
            put(FAVORITES_COLUMN_PRICE, price)
            put(FAVORITES_COLUMN_RATING, rating)
            put(FAVORITES_COLUMN_IMAGERESID, imageResId)
            put(FAVORITES_COLUMN_EMAIL, email)
        }
        return db.insert(TABLE_FAVORITES  , null, values)
    }

    fun getFavoriteByEmailAndProductName(email: String, productNameSearch: String): DbFavoriteModel? {
        val db = this.readableDatabase
        val projection = arrayOf(CART_PRODUCTS_COLUMN_IMAGERESID, CART_PRODUCTS_COLUMN_PRODUCTNAME, CART_PRODUCTS_COLUMN_SUBTOTAL, CART_PRODUCTS_COLUMN_TOTAL, CART_PRODUCTS_COLUMN_QUANTITY, CART_PRODUCTS_COLUMN_SIZE, CART_PRODUCTS_COLUMN_EMAIL)
        val selection = "$USER_COLUMN_EMAIL = ? AND $CART_PRODUCTS_COLUMN_PRODUCTNAME = ?"
        val selectionArgs = arrayOf(email, productNameSearch)

        val cursor = db.query(
            TABLE_FAVORITES,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_PRODUCTNAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_IMAGERESID))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_EMAIL))
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
            put(FAVORITES_COLUMN_PRODUCTNAME, productName)
            put(FAVORITES_COLUMN_PRICE, price)
            put(FAVORITES_COLUMN_RATING, rating)
            put(FAVORITES_COLUMN_IMAGERESID, imageResId)
            put(FAVORITES_COLUMN_EMAIL, email)
        }
        val selection = "$FAVORITES_COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        return db.update(TABLE_FAVORITES, values, selection, selectionArgs)
    }

    fun getFavorites() : MutableList<DbFavoriteModel> {
        val db = this.readableDatabase
        val projection = arrayOf(FAVORITES_COLUMN_PRODUCTNAME, FAVORITES_COLUMN_PRICE, FAVORITES_COLUMN_RATING, FAVORITES_COLUMN_IMAGERESID, FAVORITES_COLUMN_EMAIL)

        val cursor = db.query(
            TABLE_FAVORITES,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val favorites = mutableListOf<DbFavoriteModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_PRODUCTNAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_IMAGERESID))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(FAVORITES_COLUMN_EMAIL))
            favorites.add(DbFavoriteModel(productName, price, rating, imageResId, userEmail))
        }
        cursor.close()
        return favorites
    }

    fun insertOrder(imageResId: Int, productName: String, subTotal: Double, total: Double, quantity: Int, size: String, email: String): Long {
        // First, check if the user already exists
        val existingCartProduct = getOrderByEmailAndProductName(email, productName)
        if (existingCartProduct != null) {
            // User already exists, return -1 to indicate failure
            return -1
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(CART_PRODUCTS_COLUMN_IMAGERESID, imageResId)
            put(CART_PRODUCTS_COLUMN_PRODUCTNAME, productName)
            put(CART_PRODUCTS_COLUMN_SUBTOTAL, subTotal)
            put(CART_PRODUCTS_COLUMN_TOTAL, total)
            put(CART_PRODUCTS_COLUMN_QUANTITY, quantity)
            put(CART_PRODUCTS_COLUMN_SIZE, size)
            put(CART_PRODUCTS_COLUMN_EMAIL, email)
        }
        return db.insert(TABLE_CART_PRODUCTS  , null, values)
    }

    fun getOrderByEmailAndProductName(email: String, productNameSearch: String): DbOrderModel? {
        val db = this.readableDatabase
        val projection = arrayOf(ORDERS_COLUMN_IMAGERESID, ORDERS_COLUMN_PRODUCTNAME, ORDERS_COLUMN_SUBTOTAL, ORDERS_COLUMN_TOTAL, ORDERS_COLUMN_QUANTITY, ORDERS_COLUMN_SIZE, ORDERS_COLUMN_EMAIL)
        val selection = "$ORDERS_COLUMN_EMAIL = ? AND $ORDERS_COLUMN_PRODUCTNAME = ?"
        val selectionArgs = arrayOf(email, productNameSearch)

        val cursor = db.query(
            TABLE_ORDERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_IMAGERESID))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_PRODUCTNAME))
            val subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_SUBTOTAL))
            val total = cursor.getDouble(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_TOTAL))
            val size = cursor.getString(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_SIZE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_QUANTITY))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_EMAIL))
            cursor.close()
            DbOrderModel(imageResId, productName, subTotal, total, size, quantity, userEmail)
        } else {
            cursor.close()
            null
        }
    }

    fun updateOrder(imageResId: Int, productName: String, subTotal: Double, total: Double, size: String, quantity: Int, email: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ORDERS_COLUMN_IMAGERESID, imageResId)
            put(ORDERS_COLUMN_PRODUCTNAME, productName)
            put(ORDERS_COLUMN_SUBTOTAL, subTotal)
            put(ORDERS_COLUMN_TOTAL, total)
            put(ORDERS_COLUMN_SIZE, size)
            put(ORDERS_COLUMN_QUANTITY, quantity)
            put(ORDERS_COLUMN_EMAIL, email)
        }
        val selection = "$ORDERS_COLUMN_EMAIL = ? AND $ORDERS_COLUMN_PRODUCTNAME = ?"
        val selectionArgs = arrayOf(email, productName)
        return db.update(TABLE_ORDERS, values, selection, selectionArgs)
    }

    fun getOrders(): MutableList<DbOrderModel> {
        val db = this.readableDatabase
        val projection = arrayOf(ORDERS_COLUMN_IMAGERESID, ORDERS_COLUMN_PRODUCTNAME, ORDERS_COLUMN_SUBTOTAL, ORDERS_COLUMN_TOTAL, ORDERS_COLUMN_QUANTITY, ORDERS_COLUMN_SIZE, ORDERS_COLUMN_EMAIL)

        val cursor = db.query(
            TABLE_ORDERS,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val orders = mutableListOf<DbOrderModel>()
        while (cursor.moveToNext()) {
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_IMAGERESID))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_PRODUCTNAME))
            val subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_SUBTOTAL))
            val total = cursor.getDouble(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_TOTAL))
            val size = cursor.getString(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_SIZE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_QUANTITY))
            val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(ORDERS_COLUMN_EMAIL))
            orders.add(DbOrderModel(imageResId, productName, subTotal, total, size, quantity, userEmail))
        }
        cursor.close()
        return orders
    }


    fun insertProduct(productName: String, image: String, price: Double, rating: Double, imageResId: Int, category: String): Long {
        // First, check if the user already exists
        val existingProducts = getProduct(productName, image, price, rating, imageResId, category)
        if (existingProducts != null) {
            // User already exists, return -1 to indicate failure
            return -1
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(PRODUCTS_COLUMN_PRODUCTNAME, productName)
            put(PRODUCTS_COLUMN_IMAGE, image)
            put(PRODUCTS_COLUMN_PRICE, price)
            put(PRODUCTS_COLUMN_RATING, rating)
            put(PRODUCTS_COLUMN_IMAGERESID, imageResId)
            put(PRODUCTS_COLUMN_CATEGORY, category)
        }
        return db.insert(TABLE_PRODUCTS  , null, values)
    }

    private fun getProduct(productNameSearch: String, imageUrl: String, priceSearch: Double, ratingSearch: Double, imageResIdSearch: Int, categorySearch: String): DbRecommendedModel? {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)
        val selection = "$$PRODUCTS_COLUMN_PRODUCTNAME = ? AND $PRODUCTS_COLUMN_IMAGE = ? AND $PRODUCTS_COLUMN_PRICE = ? AND $PRODUCTS_COLUMN_RATING = ? AND $PRODUCTS_COLUMN_IMAGERESID = ? AND $PRODUCTS_COLUMN_CATEGORY = ?"
        val selectionArgs = arrayOf(productNameSearch, imageUrl, priceSearch.toString(), ratingSearch.toString(), imageResIdSearch.toString(), categorySearch)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_CATEGORY))
            cursor.close()
            DbRecommendedModel(productName, image, price, rating, imageResId, category)
        } else {
            cursor.close()
            null
        }
    }

    // function to get product category  by name, image, price, rating and imageResId
    fun getProductCategory(name: String, image: String, price: Double, rating: Double, imageResId: Int): String {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_CATEGORY)
        val selection = "$PRODUCTS_COLUMN_PRODUCTNAME = ? AND $PRODUCTS_COLUMN_IMAGE = ? AND $PRODUCTS_COLUMN_PRICE = ? AND $PRODUCTS_COLUMN_RATING = ? AND $PRODUCTS_COLUMN_IMAGERESID = ?"
        val selectionArgs = arrayOf(name, image, price.toString(), rating.toString(), imageResId.toString())

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val category = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_CATEGORY))
            cursor.close()
            category
        } else {
            cursor.close()
            ""
        }
    }

    fun updateProduct(productName: String, image: String, price: Double, rating: Double, imageResId: Int, category: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(PRODUCTS_COLUMN_PRODUCTNAME, productName)
            put(PRODUCTS_COLUMN_IMAGE, image)
            put(PRODUCTS_COLUMN_PRICE, price)
            put(PRODUCTS_COLUMN_RATING, rating)
            put(PRODUCTS_COLUMN_IMAGERESID, imageResId)
            put(PRODUCTS_COLUMN_CATEGORY, category)
        }
        val selection = "$PRODUCTS_COLUMN_PRODUCTNAME = ? AND $PRODUCTS_COLUMN_IMAGE = ? AND $PRODUCTS_COLUMN_PRICE = ? AND $PRODUCTS_COLUMN_RATING = ? AND $PRODUCTS_COLUMN_IMAGERESID = ? AND $PRODUCTS_COLUMN_CATEGORY = ?"
        val selectionArgs = arrayOf(productName, image, price.toString(), rating.toString(), imageResId.toString(), category)
        return db.update(TABLE_PRODUCTS, values, selection, selectionArgs)
    }

    fun getProducts(): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()
        return products
    }

    // get the first ten products in the database
    fun getRecommendedProducts(): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            null,
            null,
            null,
            null,
            null,
            "10"
        )

        val products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()
        return products
    }

    // get products in a specific category
    fun getProductsByCategory(category: String): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)
        val selection = "$PRODUCTS_COLUMN_CATEGORY = ?"
        val selectionArgs = arrayOf(category)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()
        return products
    }

    // get products in a specific category and sort them by price
    fun getProductsByCategoryAndSortByPrice(category: String): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)
        val selection = "$PRODUCTS_COLUMN_CATEGORY = ?"
        val selectionArgs = arrayOf(category)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            "$PRODUCTS_COLUMN_PRICE ASC"
        )

        val products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()
        return products
    }

    // get products in a specific category and sort them by price
    fun getProductsByCategoryAndSortByRating(category: String): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)
        val selection = "$PRODUCTS_COLUMN_CATEGORY = ?"
        val selectionArgs = arrayOf(category)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            "$PRODUCTS_COLUMN_RATING DESC"
        )

        val products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()
        return products
    }

    // get products in a specific category and sort them by price
    fun getProductsByCategoryAndSortByName(category: String): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)
        val selection = "$PRODUCTS_COLUMN_CATEGORY = ?"
        val selectionArgs = arrayOf(category)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            "$PRODUCTS_COLUMN_PRODUCTNAME DESC"
        )

        val products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()
        return products
    }

    // get recommended products and sort them by price
    fun getRecommendedProductsAndSortByPrice(): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            null,
            null,
            null,
            null,
            null,
            "10"
        )

        var products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()

        // Sort the products by price
        products = products.sortedBy { it.price }.toMutableList()
        return products
    }

    // get recommended products and sort them by name
    fun getRecommendedProductsAndSortByName(): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            null,
            null,
            null,
            null,
            null,
            "10"
        )

        val products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(name, image, price, rating, imageResId))
        }
        cursor.close()
        products.sortBy {
            it.name
        }
        return products
    }

    // get recommended products and sort them by price
    fun getRecommendedProductsAndSortByRating(): MutableList<RecommendedModel> {
        val db = this.readableDatabase
        val projection = arrayOf(PRODUCTS_COLUMN_PRODUCTNAME, PRODUCTS_COLUMN_IMAGE, PRODUCTS_COLUMN_PRICE, PRODUCTS_COLUMN_RATING, PRODUCTS_COLUMN_IMAGERESID, PRODUCTS_COLUMN_CATEGORY)

        val cursor = db.query(
            TABLE_PRODUCTS,
            projection,
            null,
            null,
            null,
            null,
            null,
            "10"
        )

        var products = mutableListOf<RecommendedModel>()
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRODUCTNAME))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGE))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_PRICE))
            val rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_RATING))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COLUMN_IMAGERESID))
            products.add(RecommendedModel(productName, image, price, rating, imageResId))
        }
        cursor.close()

        // Sort the products by price
        products = products.sortedBy { it.rating }.toMutableList()
        return products
    }

}