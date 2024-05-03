import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    fun isFavorite(itemId: Int): Boolean {
        return sharedPreferences.getBoolean(itemId.toString(), false)
    }

    fun setFavorite(itemId: Int, isFavorite: Boolean) {
        sharedPreferences.edit().putBoolean(itemId.toString(), isFavorite).apply()
    }
}