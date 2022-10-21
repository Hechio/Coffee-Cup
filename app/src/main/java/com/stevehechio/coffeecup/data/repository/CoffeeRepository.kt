package com.stevehechio.coffeecup.data.repository

import android.content.Context
import android.util.Log
import com.stevehechio.coffeecup.data.Resource
import com.stevehechio.coffeecup.data.local.db.AppDatabase
import com.stevehechio.coffeecup.data.local.entities.CoffeeEntity
import com.stevehechio.coffeecup.data.remote.api.CoffeeApiService
import com.stevehechio.coffeecup.data.remote.api.RandomCoffeeService
import com.stevehechio.coffeecup.utils.NetworkUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import javax.inject.Inject

/**
// Created by Steve Hechio on 14/10/2022.
 */
class CoffeeRepository @Inject constructor(
    private val context: Context,
    private val service: CoffeeApiService,
    private val appDatabase: AppDatabase
): BaseRepository {
    private val compositeDisposable = CompositeDisposable()

    fun getCoffeeDetails(type: String): Observable<Resource<List<CoffeeEntity>>> =
        if (NetworkUtil.isNetworkAvailable(context = context)){
            getRemoteCoffeeDetails(type = type)
        }else {
            getLocalCoffeeDetails(type = type)
        }

    private fun getRemoteCoffeeDetails(type: String): Observable<Resource<List<CoffeeEntity>>> =
        Observable.create {emitter ->
            emitter.onNext(Resource.Loading())
            val disposable = service
                .getCoffeeDetails(type = type)
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Completable.fromRunnable {
                        appDatabase.coffeeDao().deleteAll()
                        it.forEach { coffeeEntity ->
                            coffeeEntity.type = type
                        }
                        appDatabase.coffeeDao().insertAll(it)
                    }.subscribeOn(io()).subscribe()
                }
                .subscribe({
                    emitter.onNext( Resource.Success(it))
                    Log.d("coffee res", "Success Execution! $it")
                },{
                    emitter.onNext(Resource.Failure(it.localizedMessage))
                    Log.e("coffee res", "Error Execution! $it")
                })
            addDisposable(disposable = disposable)
        }

    private fun getLocalCoffeeDetails(type: String): Observable<Resource<List<CoffeeEntity>>> =
        Observable.create { emitter ->
            emitter.onNext(Resource.Loading())
            val disposable = appDatabase.coffeeDao().getCoffeeDetails(type)
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    emitter.onNext( Resource.Success(it))
                    Log.d("coffee res", "Success Execution! $it")
                },{
                    emitter.onNext(Resource.Failure(it.localizedMessage))
                    Log.e("coffee res", "Error Execution! $it")
                })
            addDisposable(disposable)
        }

//    private fun getRandomCoffeeImage(type: String): Observable<Resource<String>> =
//        Observable.create {emitter ->
//            emitter.onNext(Resource.Loading())
//            val disposable = randomService
//                .getRandomCoffee()
//                .subscribeOn(io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    emitter.onNext( Resource.Success(it.file))
//                    Log.d("coffee res", "Success Execution! $it")
//                },{
//                    emitter.onNext(Resource.Failure(it.localizedMessage))
//                    Log.e("coffee res", "Error Execution! $it")
//                })
//            addDisposable(disposable = disposable)
//        }

    override fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}