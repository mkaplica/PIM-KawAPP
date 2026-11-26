package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.database.dao.BrewMethodDao
import pl.betoniarze.kawapp.core.database.model.asEntity
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.BrewMethod

/** [BrewMethodRepository] backed by Room. Built-in methods ([BrewMethod.BUILT_IN]) come first, then the user's own. */
class LocalBrewMethodRepository @Inject constructor(
    private val brewMethodDao: BrewMethodDao,
) : BrewMethodRepository {

    // LocalBrewMethodRepository
    override fun getBrewMethods(): Flow<List<BrewMethod>> =
        brewMethodDao.getBrewMethods().map { rows -> BrewMethod.BUILT_IN + rows.map { it.asExternalModel() } }

    override suspend fun addBrewMethod(brewMethod: BrewMethod) {
        brewMethodDao.insertBrewMethod(brewMethod.asEntity())
    }

    override suspend fun deleteBrewMethod(id: String) {
        brewMethodDao.deleteBrewMethod(id)
    }
}
