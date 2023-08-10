package media.uqab.goaltracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform