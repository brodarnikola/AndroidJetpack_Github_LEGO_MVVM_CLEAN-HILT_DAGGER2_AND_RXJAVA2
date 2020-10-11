/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * [SavedLanguagesDb] represents when a user adds a [Plant] to their garden, with useful metadata.
 * Properties such as [lastVisitedLanguageDate] are used for notifications (such as when to water the
 * plant).
 *
 * Declaring the column info allows for the renaming of variables without implementing a
 * database migration, as the column name would not change.
 */
@Entity(
    tableName = "saved_languages",
    foreignKeys = [
        ForeignKey(entity = LanguagesDb::class, parentColumns = ["id"], childColumns = ["language_id"])
    ],
    indices = [Index("language_id")]
)
data class SavedLanguagesDb(
    @ColumnInfo(name = "language_id") val languagesId: Int,

    /**
     * Indicates when the [Plant] was planted. Used for showing notification when it's time
     * to harvest the plant.
     */
    @ColumnInfo(name = "saved_lanaguage_date") val savedLanguageDate: Calendar = Calendar.getInstance(),


    /**
     * Indicates when the [Plant] was last watered. Used for showing notification when it's
     * time to water the plant.
     */
    @ColumnInfo(name = "last_visited_language_date")
    val lastVisitedLanguageDate: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var savedLanguageId: Long = 0



}
