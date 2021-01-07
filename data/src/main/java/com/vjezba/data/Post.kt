package com.vjezba.data

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Post  {

    @SerializedName("userId")
    @Expose
    val userId = 0

    @SerializedName("id")
    @Expose
    val id = 0

    @SerializedName("title")
    @Expose
    val title: String? = null

    @SerializedName("body")
    @Expose
    val body: String? = null

    var comments: List<Comment>? = null

    override fun toString(): String {
        return "Post{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}'
    }

}