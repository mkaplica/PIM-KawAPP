package pl.betoniarze.kawapp.feature.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private val _message = MutableStateFlow("Hello Profile Screen")
    val message: StateFlow<String> = _message.asStateFlow()
}
