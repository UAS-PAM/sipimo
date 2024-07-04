package com.example.sipimo

data class Schedule(
    val userId: String,
    val scheduleId: String,
    val diseaseName: String,
    val medicineName: String,
    val drugType: String,
    val amountOfDrug: Int,
    val used: Int,
    var during: String,
    var startAt: String
) {
    constructor(): this("","",  "", "", "", 1, 1, "","")
}
