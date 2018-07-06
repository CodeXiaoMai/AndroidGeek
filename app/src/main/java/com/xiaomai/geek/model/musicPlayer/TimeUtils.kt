package com.xiaomai.geek.model.musicPlayer

object TimeUtils {
    fun millToTime(time: Int): String {
        var timeString: String?
        var hour = 0
        var minute = 0
        var second = 0
        if (time <= 0) {
            return "00:00:00"
        }
        minute = (time / 1000 / 60)
        if (minute < 60) {
            second = (time / 1000 % 60)
            timeString = "00:" + unitFormat(minute) + ":" + unitFormat(second)
        } else {
            hour = minute / 60
            if (hour > 99) {
                return "99:59:59"
            }
            minute %= 60
            second = (time / 1000 - hour * 3600 - minute * 60)
            timeString = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
        }
        return timeString
    }

    private fun unitFormat(time: Int): String {
        return if (time in 0 until 10) {
            "0" + time
        } else {
            time.toString()
        }
    }
}