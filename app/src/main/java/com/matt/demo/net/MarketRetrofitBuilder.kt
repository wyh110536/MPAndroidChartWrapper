package com.bitconch.lib_wrapper.net

import com.matt.demo.config.GlobalConfig
import com.matt.demo.net.base.AbstractRetrofitBuilder
import retrofit2.Retrofit


/**
 * ============================================================
 * 作 者 :    freer-2
 * 更新时间 ：2019/11/13 13:41
 * 描 述 ：一个Retrofit实例
 * ============================================================
 */
class MarketRetrofitBuilder : AbstractRetrofitBuilder() {

    override fun apiBaseUrl(): String {
        return GlobalConfig.getMarketUrl()
    }

    companion object {
        val mRetrofit: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MarketRetrofitBuilder().retrofit
        }
    }


}