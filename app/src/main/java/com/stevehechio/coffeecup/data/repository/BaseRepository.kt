package com.stevehechio.coffeecup.data.repository

import io.reactivex.rxjava3.disposables.Disposable

interface BaseRepository {
    fun addDisposable(disposable: Disposable)

    fun clear()
}