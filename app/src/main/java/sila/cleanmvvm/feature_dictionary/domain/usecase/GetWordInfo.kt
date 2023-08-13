package sila.cleanmvvm.feature_dictionary.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sila.cleanmvvm.core.util.Resource
import sila.cleanmvvm.feature_dictionary.domain.model.WordInfo
import sila.cleanmvvm.feature_dictionary.domain.repository.WordInfoRepository

class GetWordInfo(
    private val repository: WordInfoRepository
) {
    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {
        if(word.isBlank()){
            return flow {}
        }
        return repository.getWordInfo(word)
    }
}