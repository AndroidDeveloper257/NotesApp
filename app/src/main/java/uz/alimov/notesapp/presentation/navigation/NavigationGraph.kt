package uz.alimov.notesapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uz.alimov.notesapp.presentation.screen.category.CategoryScreen
import uz.alimov.notesapp.presentation.screen.home.HomeScreen
import uz.alimov.notesapp.presentation.screen.note.NoteScreen
import uz.alimov.notesapp.presentation.screen.settings.SettingsScreen
import uz.alimov.notesapp.presentation.ui.theme.NotesAppTheme

@Composable
fun NotesNavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    Surface {
        NavHost(
            modifier = Modifier
                .background(Color.White),
            navController = navController,
            startDestination = Home()
        ) {
            composable<Home>(
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                }
            ) {
                val categoryId = it.savedStateHandle.get<Long>("category_id")
                HomeScreen(
                    categoryId = categoryId,
                    onNavigateToNote = {
                        navController.navigate(
                            Note(
                                noteId = it?.id,
                                categoryId = it?.categoryId
                            )
                        )
                    },
                    onNavigateToCategory = {
                        navController.navigate(Category())
                    },
                    onNavigateToSettings = {
                        navController.navigate(Settings)
                    }
                )
            }
            composable<Note>(
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                }
            ) {
                val note = it.toRoute<Note>()
                NoteScreen(
                    note = note,
                    navigateUp = {
                        navController.navigateUp()
                    }
                )
            }
            composable<Category>(
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                }
            ) {
                CategoryScreen(
                    navigateBack = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("category_id", it)
                        navController.navigateUp()
                    }
                )
            }
            composable<Settings>(
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                }
            ) {
                SettingsScreen()
            }
        }

    }
}

@Preview
@Composable
private fun NotesNavGraphPreview() {
    NotesAppTheme {
        NotesNavigationGraph(
            navController = rememberNavController()
        )
    }
}