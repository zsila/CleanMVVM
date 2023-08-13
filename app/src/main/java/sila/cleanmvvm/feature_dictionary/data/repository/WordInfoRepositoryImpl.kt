package sila.cleanmvvm.feature_dictionary.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import sila.cleanmvvm.core.util.Resource
import sila.cleanmvvm.feature_dictionary.data.local.DictionaryDao
import sila.cleanmvvm.feature_dictionary.data.remote.DictionaryAPI
import sila.cleanmvvm.feature_dictionary.domain.model.WordInfo
import sila.cleanmvvm.feature_dictionary.domain.repository.WordInfoRepository
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryAPI,
    private val dao: DictionaryDao
) : WordInfoRepository {

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(data = wordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Something went wrong!",
                    data = wordInfos
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Check your internet connection!",
                    data = wordInfos
                )
            )
        }

        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(data = newWordInfos))

    }
}