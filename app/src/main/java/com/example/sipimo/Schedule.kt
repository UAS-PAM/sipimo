package com.example.sipimo

data class Schedule(
    val userId: String,
    val diseaseName: String,
    val medicineName: String,
    val drugType: String,
    val amountOfDrug: Int,
    val used: Int,
    val during: String,
    val startAt: String
) {
    constructor(): this("", "", "", "", 1, 1, "","")
}
