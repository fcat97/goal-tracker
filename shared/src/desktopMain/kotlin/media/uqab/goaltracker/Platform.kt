package media.uqab.goaltracker

class JvmPlatform: Platform {
    override val name: String
        get() = javaClass.simpleName
}

actual fun getPlatform(): Platform {
    return JvmPlatform()
}