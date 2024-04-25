import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.shopease.model.SliderModel

@Composable
fun Slider(sliderData: List<SliderModel>) {
    val context = LocalContext.current
    LazyRow {
        items(sliderData) { slider ->
            Image(
                painter = painterResource(id = slider.imageResId),
                contentDescription = null
            )
        }
    }
}