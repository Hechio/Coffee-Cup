package com.stevehechio.coffeecup.ui.activities.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stevehechio.coffeecup.data.Resource
import com.stevehechio.coffeecup.data.local.entities.CoffeeEntity
import com.stevehechio.coffeecup.data.repository.CoffeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import javax.inject.Inject

/**
// Created by Steve Hechio on 17/10/2022.
 */
@HiltViewModel
class CoffeeViewModel @Inject constructor(private val repository: CoffeeRepository) : ViewModel(){
    private val compositeDisposable = CompositeDisposable()
    private val coffeeEntityLiveData: MutableLiveData<Resource<List<CoffeeEntity>>> =
        MutableLiveData<Resource<List<CoffeeEntity>>>()

    val getCoffeeEntityLiveData:
            MutableLiveData<Resource<List<CoffeeEntity>>> = coffeeEntityLiveData

    fun fetchCoffeeDetails(type: String){
        addToDisposable(
            repository.getCoffeeDetails(type = type)
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                    coffeeEntityLiveData.value = it
                    },{
                        coffeeEntityLiveData.value = Resource.Failure(it.localizedMessage)
                    }
                )
        )
    }

    private fun addToDisposable(disposable: Disposable) {
        compositeDisposable.remove(disposable)
        compositeDisposable.add(disposable)
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}