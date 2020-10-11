package com.example.kovid.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
//, primaryKeys = ["date", "state"]
@Entity(tableName = "StateValues")
data class StateValue(
    val checkTimeEt: String,
    val commercialScore: Int,
    val dataQualityGrade: String,
    val date: Int,
    val dateChecked: String,
    val dateModified: String,
    val death: Int,
    val deathIncrease: Int,
    val fips: String,
    val grade: String,
    val hash: String,
    val hospitalizedCurrently: Int,
    val hospitalizedIncrease: Int,
    val inIcuCurrently: Int,
    val lastUpdateEt: String,
    val negative: Int,
    val negativeIncrease: Int,
    val negativeRegularScore: Int,
    val negativeScore: Int,
    val posNeg: Int,
    val positive: Int,
    val positiveCasesViral: Int,
    val positiveIncrease: Int,
    val positiveScore: Int,
    val score: Int,
    @PrimaryKey
    val state: String,
    val total: Int,
    val totalTestResults: Int,
    val totalTestResultsIncrease: Int,
    val totalTestResultsSource: String,
    val totalTestsViral: Int
)