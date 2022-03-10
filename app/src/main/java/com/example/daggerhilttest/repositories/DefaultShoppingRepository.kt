package com.example.daggerhilttest.repositories

import androidx.lifecycle.LiveData
import com.example.daggerhilttest.data.local.ShoppingDao
import com.example.daggerhilttest.data.local.ShoppingItem
import com.example.daggerhilttest.data.remote.PixabayAPI
import com.example.daggerhilttest.data.remote.response.ImageResponse
import com.example.daggerhilttest.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeShoppingItems(): LiveData<List<ShoppingItem>> =
        shoppingDao.observeAllShoppingItems()

    override fun observeTotalPrice(): LiveData<Float> = shoppingDao.observeTotalPrice()

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response =  pixabayAPI.searchForImage(imageQuery)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("알수없는 에러 발생", null)
            }else{
                Resource.error("알수없는 에러 발생", null)
            }
        } catch (e: Exception) {
            Resource.error("서버에 연결되지 않았습니다. 인터넷 상태를 체크하세요", null)
        }
    }
}