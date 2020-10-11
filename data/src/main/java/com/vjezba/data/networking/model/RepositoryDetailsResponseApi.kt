package com.vjezba.data.networking.model

import com.google.gson.annotations.SerializedName


data class RepositoryDetailsResponseApi(
    val id: Long = 0,
    @SerializedName("owner")
    val ownerApi: RepositoryOwnerResponseApi = RepositoryOwnerResponseApi(""),
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("html_url")
    val html_url: String? = "",
    @SerializedName("language")
    val language: String? = "",
    @SerializedName("stargazers_count")
    val stars: Int = 0,
    @SerializedName("forks_count")
    val forks: Int = 0
)