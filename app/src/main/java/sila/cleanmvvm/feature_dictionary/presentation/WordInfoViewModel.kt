package sila.cleanmvvm.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sila.cleanmvvm.core.util.DELAY_TIME_MS
import sila.cleanmvvm.core.util.Resource
import sila.cleanmvvm.core.util.UNKNOWN_ERROR
import sila.cleanmvvm.feature_dictionary.domain.usecase.GetWordInfo
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _wordInfoState = mutableStateOf( WordInfoState() )
    val wordInfoState: State<WordInfoState> = _wordInfoState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob : Job? = null

    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(DELAY_TIME_MS)
            getWordInfo(query)
                .onEach { result ->
                    when(result) {
                        is Resource.Success -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoList = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Loading -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoList = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                        is Resource.Error -> {
                            _wordInfoState.value = wordInfoState.value.copy(
                                wordInfoList = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackBar(
                                result.message ?: UNKNOWN_ERROR))
                        }
                    }
                }.launchIn(this)
        }

    }


    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
    }
}