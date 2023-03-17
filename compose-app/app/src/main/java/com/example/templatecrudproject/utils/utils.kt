package com.example.templatecrudproject.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDateTime?.formatToReadableDateTime() : String {

    if (this == null || this == LocalDateTime.MIN)
        return ""

    return "${this.year}-${this.monthValue.toString().padStart(2, '0')}-${this.dayOfMonth.toString().padStart(2, '0')} ${this.hour.toString().padStart(2, '0')}:${this.minute.toString().padStart(2, '0')}"
}

fun String?.parseReadableDateTime(): LocalDateTime? {

    if (this == null || this == "")
        return null

    val year = this.substring(0, 4).toInt()

    val month = this.substring(5, 7).trimStart { it == '0' }.toInt()

    val day = this.substring(8, 10).trimStart { it == '0' }.toInt()

    val rawHour: String = this.substring(11, 13).trimStart { it == '0' }
    val hour: Int = if (rawHour == "") 0 else rawHour.toInt()

    val rawMinute = this.substring(14, 16).trimStart { it == '0' }
    val minute: Int = if (rawMinute == "") 0 else rawMinute.toInt()

    return LocalDateTime.of(year, month, day, hour, minute)
}

fun String?.checkValidDate(): Boolean {

    if (this == null || this == "")
        return false

    val year = this.substring(0, 4).toIntOrNull() ?: return false

    val month = this.substring(5, 7).trimStart { it == '0' }.toIntOrNull() ?: return false

    val day = this.substring(8, 10).trimStart { it == '0' }.toIntOrNull() ?: return false

    if (year < 1900 || year > 2100)
        return false

    if (month < 1 || month > 12)
        return false

    if (day < 1 || day > 31)
        return false

    return true
}

fun String.toUnixTimestamp(): Int {

    val year = this.substring(0, 4).toInt()

    val month = this.substring(5, 7).trimStart { it == '0' }.toInt()

    val day = this.substring(8, 10).trimStart { it == '0' }.toInt()

    val date = LocalDateTime.of(year, month, day, 0, 0, 0)

    return date.toEpochSecond(java.time.ZoneOffset.UTC).toInt()
}

@ProvidedTypeConverter
class Converters {

//    @TypeConverter
//    fun formatToReadableDateTime(dateTime: LocalDateTime?) : String {
//
//        if (dateTime == null || dateTime == LocalDateTime.MIN)
//            return ""
//
//        val ret = "${dateTime.year}-${dateTime.monthValue.toString().padStart(2, '0')}-${dateTime.dayOfMonth.toString().padStart(2, '0')}T${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}:00Z"
//        return ret
//    }
//
//    @TypeConverter
//    fun parseReadableDateTime(string: String?): LocalDateTime? {
//
//        if (string == null || string == "")
//            return null
//
//        val year = string.substring(0, 4).toInt()
//
//        val month = string.substring(5, 7).trimStart { it == '0' }.toInt()
//
//        val day = string.substring(8, 10).trimStart { it == '0' }.toInt()
//
//        val rawHour: String = string.substring(11, 13).trimStart { it == '0' }
//        val hour: Int = if (rawHour == "") 0 else rawHour.toInt()
//
//        val rawMinute = string.substring(14, 16).trimStart { it == '0' }
//        val minute: Int = if (rawMinute == "") 0 else rawMinute.toInt()
//
//        return LocalDateTime.of(year, month, day, hour, minute)
//    }
}
