package com.xiaomai.geek.view

import com.xiaomai.mvp.lce.ILoadView

interface IReportView : ILoadView {
    fun reportResult(result: Boolean)
}
