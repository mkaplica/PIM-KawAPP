package pl.betoniarze.kawapp.core.model

import java.time.LocalDate

data class CoffeeBag(
    val roasterName: String,
    val beanName: String,
    val origin: String,
    val roastDate: LocalDate
)
