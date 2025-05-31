package uz.alimov.notesapp.presentation.components

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun NotesFloatingActionButton(
    icon: ImageVector,
    contentDescription: String,
    iconTint: Color = Color.White,
    containerColor: Color = Color.Blue,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier
            .imePadding(),
        onClick = onClick,
        containerColor = containerColor
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint
        )
    }
}