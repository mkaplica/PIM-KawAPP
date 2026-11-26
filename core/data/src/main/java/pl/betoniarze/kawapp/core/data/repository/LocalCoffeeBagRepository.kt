package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.database.dao.CoffeeBagDao
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.database.model.asSavedEntity
import pl.betoniarze.kawapp.core.model.CoffeeBag

/** [CoffeeBagRepository] backed by Room. No sample data: a fresh install starts with no coffees. */
class LocalCoffeeBagRepository @Inject constructor(
    private val coffeeBagDao: CoffeeBagDao,
) : CoffeeBagRepository {

    override fun getCoffeeBags(): Flow<List<CoffeeBag>> =
        coffeeBagDao.getCoffeeBags().map { list -> list.map { it.asExternalModel() } }

    override suspend fun addCoffeeBag(coffeeBag: CoffeeBag) {
        coffeeBagDao.insertCoffeeBag(coffeeBag.asSavedEntity())
    }

    override suspend fun deleteCoffeeBag(id: String) {
        coffeeBagDao.deleteCoffeeBag(id)
    }
}
