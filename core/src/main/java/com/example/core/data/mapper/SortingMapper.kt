package com.example.core.data.mapper

import com.example.core.data.StorageConstants
import com.example.core.domain.model.SortingType
import javax.inject.Inject

class SortingMapper @Inject constructor() {

    fun mapToPair(sorting: String): Pair<String, String> {
        val pairNameAscending = SortingType.ORDER_BY_NAME.value to SortingType.ORDER_ASCENDING.value
        val pairNameDescending = SortingType.ORDER_BY_NAME.value to SortingType.ORDER_DESCENDING.value
        val pairModifiedAscending = SortingType.ORDER_BY_MODIFIED.value to SortingType.ORDER_ASCENDING.value
        val pairModifiedDescending = SortingType.ORDER_BY_MODIFIED.value to SortingType.ORDER_DESCENDING.value

        return when (sorting) {
            StorageConstants.ORDER_BY_NAME_ASCENDING -> pairNameAscending
            StorageConstants.ORDER_BY_NAME_DESCENDING -> pairNameDescending
            StorageConstants.ORDER_BY_MODIFIED_ASCENDING -> pairModifiedAscending
            StorageConstants.ORDER_BY_MODIFIED_DESCENDING -> pairModifiedDescending
            else -> pairNameAscending
        }
    }

    fun mapFromPair(sortingPair: Pair<String, String>): String {
        val orderBy: String = sortingPair.first
        val order: String = sortingPair.second

        return when (orderBy) {
            SortingType.ORDER_BY_NAME.value ->
                when (order) {
                    SortingType.ORDER_ASCENDING.value -> StorageConstants.ORDER_BY_NAME_ASCENDING
                    SortingType.ORDER_DESCENDING.value -> StorageConstants.ORDER_BY_NAME_DESCENDING
                    else -> StorageConstants.ORDER_BY_NAME_ASCENDING
                }

            SortingType.ORDER_BY_MODIFIED.value ->
                when (order) {
                    SortingType.ORDER_ASCENDING.value -> StorageConstants.ORDER_BY_MODIFIED_ASCENDING
                    SortingType.ORDER_DESCENDING.value -> StorageConstants.ORDER_BY_MODIFIED_DESCENDING
                    else -> StorageConstants.ORDER_BY_MODIFIED_ASCENDING
                }

            else -> StorageConstants.ORDER_BY_NAME_ASCENDING
        }
    }
}
