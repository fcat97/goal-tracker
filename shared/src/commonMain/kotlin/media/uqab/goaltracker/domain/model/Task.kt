package media.uqab.goaltracker.domain.model

interface Task {
    val id: Long

    var title: String

    var target: Long

    val progress: Long

    val progressPercentage: Float
}