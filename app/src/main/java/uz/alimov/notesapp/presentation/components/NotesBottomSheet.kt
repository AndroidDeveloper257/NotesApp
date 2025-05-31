package uz.alimov.notesapp.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        content()
    }
}