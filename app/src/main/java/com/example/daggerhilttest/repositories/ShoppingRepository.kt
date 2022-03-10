package com.example.daggerhilttest.repositories

import androidx.lifecycle.LiveData
import com.example.daggerhilttest.data.local.ShoppingItem
import com.example.daggerhilttest.data.remote.response.ImageResponse
import com.example.daggerhilttest.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}