package com.matt.sample_zm.net.base

import com.matt.libwrapper.exception.ApiSuccessNullException
import com.matt.libwrapper.exception.ExceptionManager
import com.matt.libwrapper.exception.ExceptionType
import com.matt.libwrapper.ui.base.loading.IDisposable
import com.matt.libwrapper.widget.ObserverWrapper
import com.matt.sample_zm.bean.ApiData

/**
 * ============================================================
 * 作 者 :    matt
 * 更新时间 ：2020/03/11 10:16
 * 描 述 ：
 * ============================================================
 */
abstract class SimpleTObserver<T>(iDisposable: IDisposable) :
    ObserverWrapper<ApiData<T>>(iDisposable) {

    override fun onCatchNext(t: ApiData<T>) {
        super.onCatchNext(t)
        if (t.isSuccess) {
            onInnerSuccess(t.data)
        } else {
            onFinalFail(t.code, null)
        }
    }

    override fun onCatchError(e: Throwable) {
        super.onCatchError(e)
        onFinalFail(null, e)
    }

    override fun onHandlerException(e: Throwable) {
        //super.onHandlerException(e)
        onFinalFail(null, e)
    }

    open fun onInnerSuccess(data: T?) {
        if (data == null) throw ApiSuccessNullException("data不允许为null")
        onFinalSuccess(data)
    }

    open fun onFinalFail(code: Int?, throwable: Throwable?) {
        ExceptionManager.handlerException(throwable, null, ExceptionType.NET)
    }

    abstract fun onFinalSuccess(data: T)

}