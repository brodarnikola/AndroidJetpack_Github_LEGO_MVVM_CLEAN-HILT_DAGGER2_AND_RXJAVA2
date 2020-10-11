/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.domain.user

import com.vjezba.domain.repository.UserManager
import com.vjezba.domain.storage.Storage
import javax.inject.Inject
import javax.inject.Singleton


private const val REGISTERED_USER = "registered_user"
private const val LOGGED_USER = "logged_user"
private const val PASSWORD_SUFFIX = "password"

@Singleton
class UserManagerImpl @Inject constructor(private val storage: Storage
                                ) : UserManager {

    val username: String
        get() = storage.getString(REGISTERED_USER)

    override fun isUserLoggedIn() = storage.getString(LOGGED_USER).isNotEmpty()

    override fun isUserRegistered() = storage.getString(REGISTERED_USER).isNotEmpty()

    override fun getUserName(): String {
        return username
    }

    override fun registerUser(username: String, password: String) {
        storage.setString(LOGGED_USER, username)
        storage.setString(REGISTERED_USER, username)
        storage.setString("$username$PASSWORD_SUFFIX", password)
    }

    override fun loginUser(username: String, password: String): Boolean {
        val registeredUser = this.username
        if (registeredUser != username) return false

        val registeredPassword = storage.getString("$username$PASSWORD_SUFFIX")
        if (registeredPassword != password) return false

        storage.setString(LOGGED_USER, username)
        return true
    }

    override fun logout() {
        storage.setString(LOGGED_USER, "")
    }

    override fun unregister() {
        val username = storage.getString(REGISTERED_USER)
        storage.setString(REGISTERED_USER, "")
        storage.setString("$username$PASSWORD_SUFFIX", "")
        storage.setString(LOGGED_USER, "")
    }
}
