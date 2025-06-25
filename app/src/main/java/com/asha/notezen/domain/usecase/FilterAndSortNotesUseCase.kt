package com.asha.notezen.domain.usecase

import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.util.SortOrder
import com.asha.notezen.domain.util.SortType

class FilterAndSortNotesUseCase {
    operator fun invoke(
        notes: List<Note>,
        query: String,
        sortType: SortType,
        sortOrder: SortOrder
    ): List<Note> {
        val filtered = if (query.isBlank()) {
            notes
        } else {
            notes.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }
        }
        return filtered.sortedWith(
            compareByDescending<Note> { it.isPinned }.thenComparator { a, b ->
                val result = when (sortType) {
                    SortType.TITLE -> a.title.lowercase().compareTo(b.title.lowercase())
                    SortType.DATE -> a.timestamp.compareTo(b.timestamp)
                    SortType.COLOR -> a.colorHex.compareTo(b.colorHex)
                }
                if (sortOrder == SortOrder.DESCENDING) -result else result
            }
        )
    }
}
