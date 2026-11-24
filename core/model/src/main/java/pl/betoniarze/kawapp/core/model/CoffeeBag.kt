package pl.betoniarze.kawapp.core.model

import java.time.LocalDate

/** A bag of coffee beans used for a brew. */
data class CoffeeBag(
    val roasterName: String,
    val beanName: String,
    val origin: String,
    val roastDate: LocalDate
)
