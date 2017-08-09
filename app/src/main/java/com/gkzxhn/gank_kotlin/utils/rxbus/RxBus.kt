package com.gkzxhn.gank_kotlin.utils.rxbus

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subjects.Subject


/**
 * Created by 方 on 2017/8/9.
 */

class RxBus private constructor() {

    private  var bus: Subject<Any, Any> = SerializedSubject(PublishSubject.create<Any>())

    fun send(o: Any) {
        bus.onNext(o)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return bus.ofType(eventType)
    }

    companion object {
        var instance = RxBus()
        private set
    }
}