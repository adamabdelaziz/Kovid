package com.example.kovid.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StateMetadata")
data class StateMetadata(
//    val covid19Site: String,
//    val covid19SiteOld: String,
//    val covid19SiteQuaternary: String,
//    val covid19SiteQuinary: String,
//    val covid19SiteSecondary: String,
//    val covid19SiteTertiary: String,
//    val covidTrackingProjectPreferredTotalTestField: String,
//    val covidTrackingProjectPreferredTotalTestUnits: String,
//    val fips: String,
  val name: String,
//    val notes: String,
//    val pui: String,
//    val pum: Boolean,
    @PrimaryKey
    val state: String,
//    val totalTestResultsField: String,
//    val twitter: String
)