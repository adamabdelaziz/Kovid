package com.example.kovid.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USValue")
data class USValue(
    @PrimaryKey
    val date: Int,
    val dateChecked: String,
    val death: Int,
    val deathIncrease: Int,
    val hash: String,
    val hospitalized: Int,
    val hospitalizedCumulative: Int,
    val hospitalizedCurrently: Int,
    val hospitalizedIncrease: Int,
    val inIcuCumulative: Int,
    val inIcuCurrently: Int,
    val lastModified: String,
    val negative: Int,
    val negativeIncrease: Int,
    val onVentilatorCumulative: Int,
    val onVentilatorCurrently: Int,
    val pending: Int,
    val posNeg: Int,
    val positive: Int,
    val positiveIncrease: Int,
    val recovered: Int,
    val states: Int,
    val total: Int,
    val totalTestResults: Int,
    val totalTestResultsIncrease: Int
)