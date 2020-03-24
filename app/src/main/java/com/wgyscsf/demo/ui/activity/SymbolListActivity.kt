package com.wgyscsf.demo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wgyscsf.demo.R
import com.wgyscsf.demo.bean.ApiProduct
import com.wgyscsf.demo.net.ServiceWrapper
import com.wgyscsf.demo.net.base.SimpleTObserver
import com.wgyscsf.demo.ui.base.HandleExceptionActivity
import com.wgyscsf.demo.utils.RxUtils
import kotlinx.android.synthetic.main.activity_symbol_list.*
import kotlinx.android.synthetic.main.item_activity_symbol_list.view.*

class SymbolListActivity : HandleExceptionActivity() {

    companion object {
        fun goIntent(context: Context) {
            val intent = Intent(context, SymbolListActivity::class.java)
            context.startActivity(intent)
        }
    }

    val mDataList by lazy {
        ArrayList<ApiProduct>()
    }

    val mAdapter by lazy {
        object : BaseQuickAdapter<ApiProduct, BaseViewHolder>(
            R.layout.item_activity_symbol_list,
            mDataList
        ) {
            override fun convert(helper: BaseViewHolder, item: ApiProduct) {
                helper.itemView.run {
                    iasl_tv_cnName.text = item.cnName
                    iasl_tv_enName.text = item.enName
                    iasl_tv_currPrice.text = item.currentPriceFormat
                }

            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_symbol_list
    }

    override fun safeInitAll(savedInstanceState: Bundle?) {
        initAdapter()
        initListener()
        asl_srl_refresh.isRefreshing = true
        loadData()
    }

    private fun loadData() {
        val map = HashMap<String, Any>()
        map["limit"] = 50
        map["offset"] = 0
        map["category"] = 1
        ServiceWrapper.TRADE_SERVICE.getProductList(map)
            .compose(RxUtils.rxObSchedulerHelper())
            .subscribe(object : SimpleTObserver<List<ApiProduct>>(mActivity) {
                override fun onSuccess(it: List<ApiProduct>) {
                    asl_srl_refresh.isRefreshing = false
                    mDataList.clear()
                    mDataList.addAll(it)
                    mAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun initAdapter() {
        asl_rv_recycle.layoutManager = LinearLayoutManager(mContext)
        asl_rv_recycle.adapter = mAdapter
    }

    private fun initListener() {
        asl_srl_refresh.setOnRefreshListener {
            loadData()
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            val apiProduct = mDataList[position]
            ChartActivity.goIntent(mContext, apiProduct.productCode)
        }
    }
}
