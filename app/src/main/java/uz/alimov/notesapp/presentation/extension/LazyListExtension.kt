package uz.alimov.notesapp.presentation.extension

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState

suspend fun LazyListState.scrollToRevealItem(
    itemIndex: Int,
    durationMillis: Int = 300
) {
    val layoutInfo = this.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    val viewportStart = layoutInfo.viewportStartOffset
    val viewportEnd = layoutInfo.viewportEndOffset
    val totalItemsCount = layoutInfo.totalItemsCount

    val itemInfo = visibleItems.find { it.index == itemIndex }

    if (itemInfo != null) {
        val itemStart = itemInfo.offset
        val itemEnd = itemStart + itemInfo.size

        val isFirstItem = itemIndex == 0
        val isLastItem = itemIndex == totalItemsCount - 1

        when {
            itemStart < viewportStart -> {
                val offset = viewportStart - itemStart
                val extraScroll = if (isFirstItem) 50 else 0
                this.animateScrollBy(
                    -(offset + extraScroll).toFloat(),
                    animationSpec = tween(durationMillis)
                )
            }

            itemEnd > viewportEnd -> {
                val offset = itemEnd - viewportEnd
                val extraScroll = if (isLastItem) 50 else 0
                this.animateScrollBy(
                    (offset + extraScroll).toFloat(),
                    animationSpec = tween(durationMillis)
                )
            }
        }
    } else {
        this.animateScrollBy(
            ((itemIndex - firstVisibleItemIndex) * 100).toFloat(),
            animationSpec = tween(durationMillis)
        )
    }
}