package com.example.daggerhilttest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerhilttest.data.local.ShoppingItem
import com.example.daggerhilttest.data.remote.response.ImageResponse
import com.example.daggerhilttest.other.Event
import com.example.daggerhilttest.other.Resource
import com.example.daggerhilttest.repositories.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingViewModel @ViewModelInject constructor(
    private val repository: ShoppingRepository //하나의 파라미터로 DefaultRepository 나 FakeRepository 를 받을 수 있기 때문에 ShoppingRepository 인터페이스를 만든거임.
): ViewModel(){

    val shoppingItems = repository.observeShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images : LiveData<Event<Resource<ImageResponse>>> = _images //실제 뷰에 노출되는 livedata

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl : LiveData<String> = _currentImageUrl

    private val _insertShoppingItemsStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemsStatus : LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemsStatus

    fun setCurrentImageUrl(url : String){
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String , priceString: String){

    }

    fun searchForImage(imageQuery : String){

    }
}