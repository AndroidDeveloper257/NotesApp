package uz.alimov.notesapp.presentation.extension

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.smartPadding(
    all: Dp? = null,
    horizontal: Dp? = null,
    vertical: Dp? = null,
    start: Dp? = null,
    top: Dp? = null,
    end: Dp? = null,
    bottom: Dp? = null,
): Modifier {
    return this.then(
        Modifier.padding(
            start = start ?: horizontal ?: all ?: 0.dp,
            top = top ?: vertical ?: all ?: 0.dp,
            end = end ?: horizontal ?: all ?: 0.dp,
            bottom = bottom ?: vertical ?: all ?: 0.dp
        )
    )
}