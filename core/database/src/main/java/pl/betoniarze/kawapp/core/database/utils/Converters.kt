package pl.betoniarze.kawapp.core.database.utils

import androidx.room.TypeConverter
import org.json.JSONArray
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.Grinder
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromBrewMethod(method: BrewMethod): String = method.name

    @TypeConverter
    fun toBrewMethod(methodString: String): BrewMethod = BrewMethod.valueOf(methodString)

    @TypeConverter
    fun fromGrinder(grinder: Grinder): String = grinder.name

    @TypeConverter
    fun toGrinder(grinderString: String): Grinder = Grinder.valueOf(grinderString)

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return JSONArray(list).toString()
    }

    @TypeConverter
    fun toStringList(listString: String): List<String> {
        val jsonArray = JSONArray(listString)
        return List(jsonArray.length()) { jsonArray.getString(it) }
    }
}