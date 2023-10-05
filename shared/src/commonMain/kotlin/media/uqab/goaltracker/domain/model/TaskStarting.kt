package media.uqab.goaltracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class DayOfWeek { Sat, Sun, Mon, Tue, Wed, Thu, Fri }
@Serializable
enum class MonthOfYear { Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec }

fun weekDays(): List<DayOfWeek> {
    return listOf(
        DayOfWeek.Sat,
        DayOfWeek.Sun,
        DayOfWeek.Mon,
        DayOfWeek.Tue,
        DayOfWeek.Wed,
        DayOfWeek.Thu,
        DayOfWeek.Fri
    )
}

fun months(): List<MonthOfYear> {
    return listOf(
        MonthOfYear.Jan,
        MonthOfYear.Feb,
        MonthOfYear.Mar,
        MonthOfYear.Apr,
        MonthOfYear.May,
        MonthOfYear.Jun,
        MonthOfYear.Jul,
        MonthOfYear.Aug,
        MonthOfYear.Sep,
        MonthOfYear.Oct,
        MonthOfYear.Nov,
        MonthOfYear.Dec
    )
}

@Serializable
sealed class TaskStarting

@Serializable
class WeekDay(var day: DayOfWeek): TaskStarting() {
    fun toDayOfWeek(): java.time.DayOfWeek {
        return when(day) {
            DayOfWeek.Sat -> java.time.DayOfWeek.SATURDAY
            DayOfWeek.Sun -> java.time.DayOfWeek.SUNDAY
            DayOfWeek.Mon -> java.time.DayOfWeek.MONDAY
            DayOfWeek.Tue -> java.time.DayOfWeek.TUESDAY
            DayOfWeek.Wed -> java.time.DayOfWeek.WEDNESDAY
            DayOfWeek.Thu -> java.time.DayOfWeek.THURSDAY
            DayOfWeek.Fri -> java.time.DayOfWeek.FRIDAY
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WeekDay) return false

        if (day != other.day) return false

        return true
    }

    override fun hashCode(): Int {
        return day.hashCode()
    }
}

@Serializable
class Month(var month: MonthOfYear): TaskStarting() {

    fun toMonth(): java.time.Month {
        return when(month) {
            MonthOfYear.Jan -> java.time.Month.JANUARY
            MonthOfYear.Feb -> java.time.Month.FEBRUARY
            MonthOfYear.Mar -> java.time.Month.MARCH
            MonthOfYear.Apr -> java.time.Month.APRIL
            MonthOfYear.May -> java.time.Month.MAY
            MonthOfYear.Jun -> java.time.Month.JUNE
            MonthOfYear.Jul -> java.time.Month.JULY
            MonthOfYear.Aug -> java.time.Month.AUGUST
            MonthOfYear.Sep -> java.time.Month.SEPTEMBER
            MonthOfYear.Oct -> java.time.Month.OCTOBER
            MonthOfYear.Nov -> java.time.Month.NOVEMBER
            MonthOfYear.Dec -> java.time.Month.DECEMBER
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is media.uqab.goaltracker.domain.model.Month) return false

        if (month != other.month) return false

        return true
    }

    override fun hashCode(): Int {
        return month.hashCode()
    }
}

@Serializable
class DateOfMonth(var dateOfMonth: Int): TaskStarting() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateOfMonth) return false

        if (dateOfMonth != other.dateOfMonth) return false

        return true
    }

    override fun hashCode(): Int {
        return dateOfMonth
    }
}

@Serializable
class DateOfYear(var month: Month, var dateOfMonth: DateOfMonth): TaskStarting() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateOfYear) return false

        if (month != other.month) return false
        if (dateOfMonth != other.dateOfMonth) return false

        return true
    }

    override fun hashCode(): Int {
        var result = month.hashCode()
        result = 31 * result + dateOfMonth.hashCode()
        return result
    }
}


fun test() {
    val wd = WeekDay(DayOfWeek.Fri)
    val mon = Month(MonthOfYear.Jan)
    val dom = DateOfMonth(20)
    val doy = DateOfYear(mon, dom)

    val wdS = encode(wd)
    val monS = encode(mon)
    val domS = encode(dom)
    val doyS = encode(doy)

    val wdR = decode<WeekDay>(wdS)
    val monR = decode<Month>(monS)
    val domR = decode<DateOfMonth>(domS)
    val doyR = decode<DateOfYear>(doyS)

    println("wd encode:$wdS decode:${wd == wdR}")
    println("mon encode:$monS decode:${mon == monR}")
    println("dom encode:$domS decode:${dom == domR}")
    println("doy encode:$doyS decode:${doy == doyR}")

    listOf(
        wd == wdR,
        mon == monR,
        dom == domR,
        doy == doyR
    ).forEach {
        if (! it) {
            println("❌ failed")
            return
        }
        else println("🚀 passed")
    }
}