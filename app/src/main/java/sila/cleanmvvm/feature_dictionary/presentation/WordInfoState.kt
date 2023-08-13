package sila.cleanmvvm.feature_dictionary.presentation

import sila.cleanmvvm.feature_dictionary.domain.model.WordInfo

data class WordInfoState(
    val wordInfoList: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false
)