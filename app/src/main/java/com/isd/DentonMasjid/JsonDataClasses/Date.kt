package com.isd.DentonMasjid.JsonDataClasses

data class Date(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)