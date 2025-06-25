package com.asha.notezen.presentation.screens.notelist.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asha.notezen.domain.util.SortOrder
import com.asha.notezen.domain.util.SortType

@Composable
fun SortSection(
    sortType: SortType,
    sortOrder: SortOrder,
    onSortTypeChange: (SortType) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SortRadioButton("Title", sortType == SortType.TITLE) {
                onSortTypeChange(SortType.TITLE)
            }
            SortRadioButton("Date", sortType == SortType.DATE) {
                onSortTypeChange(SortType.DATE)
            }
            SortRadioButton("Color", sortType == SortType.COLOR) {
                onSortTypeChange(SortType.COLOR)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SortRadioButton("Ascending", sortOrder == SortOrder.ASCENDING) {
                onSortOrderChange(SortOrder.ASCENDING)
            }
            SortRadioButton("Descending", sortOrder == SortOrder.DESCENDING) {
                onSortOrderChange(SortOrder.DESCENDING)
            }
        }
    }
}
