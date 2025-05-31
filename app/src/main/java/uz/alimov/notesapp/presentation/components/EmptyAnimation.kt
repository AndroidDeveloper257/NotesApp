package uz.alimov.notesapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import uz.alimov.notesapp.R

@Composable
fun EmptyAnimation(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.empty)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = progress
    )
}