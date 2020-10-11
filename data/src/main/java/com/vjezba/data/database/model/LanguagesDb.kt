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
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Calendar.DAY_OF_YEAR

@Entity(tableName = "languages")
data class LanguagesDb(
    @PrimaryKey @ColumnInfo(name = "id") val languageId: Int,
    val name: String,
    val description: String,
    val createdBy: String = "",
    val createdAt: Int = 7, // how often the plant should be watered, in days
    val typeLanguage: String = "",
    val imageUrl: String = ""
) {

    override fun toString() = name
}
