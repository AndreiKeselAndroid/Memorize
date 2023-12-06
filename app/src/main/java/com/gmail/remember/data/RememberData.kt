package com.gmail.remember.data

import com.gmail.remember.models.LevelModel
import com.gmail.remember.models.TimeModel

object RememberData {
    val listLevels: List<LevelModel>
        get() = listOf(
            LevelModel(
                id = 1,
                name = "Легкий",
                countSuccess = 1,
                check = false
            ),
            LevelModel(
                id = 3,
                name = "Средний",
                countSuccess = 3,
                check = false
            ),
            LevelModel(
                id = 5,
                name = "Сложный",
                countSuccess = 5,
                check = false
            )
        )

    val listTime: List<TimeModel>
        get() = listOf(
            TimeModel(
                id = 1,
                time = "01:00",
                isCheck = false
            ),
            TimeModel(
                id = 2,
                time = "02:00",
                isCheck = false
            ),
            TimeModel(
                id = 3,
                time = "03:00",
                isCheck = false
            ),
            TimeModel(
                id = 4,
                time = "04:00",
                isCheck = false
            ),
            TimeModel(
                id = 5,
                time = "05:00",
                isCheck = false
            ),
            TimeModel(
                id = 6,
                time = "06:00",
                isCheck = false
            ),
            TimeModel(
                id = 7,
                time = "07:00",
                isCheck = false
            ),
            TimeModel(
                id = 8,
                time = "08:00",
                isCheck = false
            ),
            TimeModel(
                id = 9,
                time = "09:00",
                isCheck = false
            ),
            TimeModel(
                id = 10,
                time = "10:00",
                isCheck = false
            ),
            TimeModel(
                id = 11,
                time = "11:00",
                isCheck = false
            ),
            TimeModel(
                id = 12,
                time = "12:00",
                isCheck = false
            ),
            TimeModel(
                id = 13,
                time = "13:00",
                isCheck = false
            ),
            TimeModel(
                id = 14,
                time = "14:00",
                isCheck = false
            ),
            TimeModel(
                id = 15,
                time = "15:00",
                isCheck = false
            ),
            TimeModel(
                id = 16,
                time = "16:00",
                isCheck = false
            ),
            TimeModel(
                id = 17,
                time = "17:00",
                isCheck = false
            ),
            TimeModel(
                id = 18,
                time = "18:00",
                isCheck = false
            ),
            TimeModel(
                id = 19,
                time = "19:00",
                isCheck = false
            ),
            TimeModel(
                id = 20,
                time = "20:00",
                isCheck = false
            ),
            TimeModel(
                id = 21,
                time = "21:00",
                isCheck = false
            ),
            TimeModel(
                id = 22,
                time = "22:00",
                isCheck = false
            ),
            TimeModel(
                id = 23,
                time = "23:00",
                isCheck = false
            ),
            TimeModel(
                id = 24,
                time = "24:00",
                isCheck = false
            ),
        )
}