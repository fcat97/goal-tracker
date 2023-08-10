package media.uqab.goaltracker.domain.model

data class UiEvent(val msg: String, val action: UiAction = null)

typealias UiAction = ( () -> Unit)?