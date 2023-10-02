package media.uqab.goaltracker.utils

import java.time.LocalDateTime
import java.time.ZoneId

inline val LocalDateTime.timeInMillis: Long
    get() = this.atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

