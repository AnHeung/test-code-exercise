package com.example.daggerhilttest.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.daggerhilttest.data.local.ShoppingItem
import com.example.daggerhilttest.data.remote.response.ImageResponse
import com.example.daggerhilttest.other.Resource

// viewModel 이 적절히 이벤트에 응답하는지 실제 레파지토리의 시뮬레이팅 클래스
class FakeShoppingRepository : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shoudReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shoudReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float = shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeShoppingItems(): LiveData<List<ShoppingItem>> = observableShoppingItems

    override fun observeTotalPrice(): LiveData<Float> = observableTotalPrice

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> = if(shoudReturnNetworkError){
        Resource.error("Error" ,null)
    }else {
        Resource.success(ImageResponse(emptyList(), 0 , 0))
    }
}