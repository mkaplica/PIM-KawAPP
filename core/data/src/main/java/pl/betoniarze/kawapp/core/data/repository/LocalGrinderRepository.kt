package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.database.dao.GrinderDao
import pl.betoniarze.kawapp.core.database.model.asEntity
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.Grinder

/** [GrinderRepository] backed by Room. No sample data: a fresh install starts with no grinders. */
class LocalGrinderRepository @Inject constructor(
    private val grinderDao: GrinderDao,
) : GrinderRepository {

    override fun getGrinders(): Flow<List<Grinder>> =
        grinderDao.getGrinders().map { list -> list.map { it.asExternalModel() } }

    override suspend fun addGrinder(grinder: Grinder) {
        grinderDao.insertGrinder(grinder.asEntity())
    }

    override suspend fun deleteGrinder(id: String) {
        grinderDao.deleteGrinder(id)
    }
}
