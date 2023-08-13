package sila.cleanmvvm.feature_dictionary.domain.repository

import kotlinx.coroutines.flow.Flow
import sila.cleanmvvm.core.util.Resource
import sila.cleanmvvm.feature_dictionary.domain.model.WordInfo

interface WordInfoRepository {

    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
}