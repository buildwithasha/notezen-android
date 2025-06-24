package com.asha.notezen.domain.usecase

import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.util.SortOrder
import com.asha.notezen.domain.util.SortType

class FilterAndSortNotes {
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

        val sorted = when (sortType) {
            SortType.TITLE -> filtered.sortedBy { it.title.lowercase() }
            SortType.DATE -> filtered.sortedBy { it.timestamp }
            SortType.COLOR -> filtered.sortedBy { it.colorHex }
        }

        return if (sortOrder == SortOrder.DESCENDING) sorted.reversed() else sorted
    }
}
