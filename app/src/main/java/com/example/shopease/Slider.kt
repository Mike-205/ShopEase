import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.shopease.model.SliderModel

// Composable function Slider
// This function takes a list of SliderModel as input and displays them in a LazyRow
@Composable
fun Slider(sliderData: List<SliderModel>) {
    // Get the current context
    val context = LocalContext.current

    // Create a LazyRow
    LazyRow {
        // For each item in sliderData, create an Image
        items(sliderData) { slider ->
            Image(
                // Use the imageResId of the slider item to get the image resource
                painter = painterResource(id = slider.imageResId),
                // Content description for accessibility, set to null as it's decorative
                contentDescription = null
            )
        }
    }
}