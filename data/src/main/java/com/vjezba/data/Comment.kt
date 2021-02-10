package com.vjezba.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Comment {

    @Expose
    @SerializedName("postId")
    private var postId = 0

    @Expose
    @SerializedName("id")
    private var id = 0

    @Expose
    @SerializedName("name")
    private var name: String? = null

    @Expose
    @SerializedName("email")
    private var email: String? = null

    @Expose
    @SerializedName("body")
    private var body: String? = null

    fun Comment(
        postId: Int,
        id: Int,
        name: String?,
        email: String?,
        body: String?
    ) {
        this.postId = postId
        this.id = id
        this.name = name
        this.email = email
        this.body = body
    }


    fun getPostId(): Int {
        return postId
    }

    fun setPostId(postId: Int) {
        this.postId = postId
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getBody(): String? {
        return body
    }

    fun setBody(body: String?) {
        this.body = body
    }

}