package uz.alimov.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import uz.alimov.notesapp.presentation.navigation.NotesNavigationGraph
import uz.alimov.notesapp.presentation.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                val navController = rememberNavController()
                NotesNavigationGraph(navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotesAppTheme {
        NotesNavigationGraph(navController = rememberNavController())
    }
}