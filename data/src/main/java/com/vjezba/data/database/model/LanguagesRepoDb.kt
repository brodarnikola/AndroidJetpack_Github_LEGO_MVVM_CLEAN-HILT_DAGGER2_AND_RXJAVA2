package com.vjezba.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "github_repositories")
data class LanguagesRepoDb(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = 0,
    val avatarUrl: String = "",
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String? = "",
    val description: String? = "",
    val html_url: String? = "",
    val language: String? = "",
    val stars: Int = 0,
    val forks: Int = 0
)