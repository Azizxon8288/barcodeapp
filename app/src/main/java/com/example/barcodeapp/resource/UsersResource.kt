package com.example.barcodeapp.resource

import com.example.barcodeapp.data.model.GithubUser
import com.example.barcodeapp.data.room.entities.CategoryEntity

sealed class UsersResource {
    object Loading : UsersResource()

    data class Error(val message: String) : UsersResource()

    data class Success(val list: List<GithubUser>?) : UsersResource()
}
