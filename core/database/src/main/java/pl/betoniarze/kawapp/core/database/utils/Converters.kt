package pl.betoniarze.kawapp.core.database.utils

import androidx.room.TypeConverter
import org.json.JSONArray
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.Grinder
import java.time.LocalDate

/** Room type converters for types SQLite cannot store directly. */
class Converters {
    /** Stores a date as ISO-8601 text, e.g. "2026-11-21". */
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    /** Parses an ISO-8601 date stored by [fromLocalDate]. */
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it) }

    /** Stores the enum by its name. */
    @TypeConverter
    fun fromBrewMethod(method: BrewMethod): String = method.name

    /** Reads an enum stored by [fromBrewMethod]. */
    @TypeConverter
    fun toBrewMethod(methodString: String): BrewMethod = BrewMethod.valueOf(methodString)

    /** Stores the enum by its name. */
    @TypeConverter
    fun fromGrinder(grinder: Grinder): String = grinder.name

    /** Reads an enum stored by [fromGrinder]. */
    @TypeConverter
    fun toGrinder(grinderString: String): Grinder = Grinder.valueOf(grinderString)

    /** Stores a list of strings as a JSON array. */
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return JSONArray(list).toString()
    }

    /** Reads a JSON array stored by [fromStringList]. */
    @TypeConverter
    fun toStringList(listString: String): List<String> {
        val jsonArray = JSONArray(listString)
        return List(jsonArray.length()) { jsonArray.getString(it) }
    }
}