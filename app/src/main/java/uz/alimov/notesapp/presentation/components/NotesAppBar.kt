package uz.alimov.notesapp.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.alimov.notesapp.R
import uz.alimov.notesapp.presentation.ui.theme.NotesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesAppBar(
    modifier: Modifier = Modifier,
    title: Int,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    navigateUp: (() -> Unit)? = null,
    actions: @Composable () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(title)
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = containerColor
        ),
        navigationIcon = {
            if (navigateUp != null) {
                IconButton(
                    onClick = navigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            actions()
        }
    )
}

@Composable
fun NotesActionIcon(
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    bitmap: ImageBitmap? = null,
    contentDescription: String = "",
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        when {
            imageVector != null -> {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }

            painter != null -> {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painter,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }

            bitmap != null -> {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    bitmap = bitmap,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun NotesAppBarPreview() {
    NotesAppTheme {
        NotesAppBar(
            title = R.string.app_name,
            navigateUp = {},
            actions = {}
        )
    }
}