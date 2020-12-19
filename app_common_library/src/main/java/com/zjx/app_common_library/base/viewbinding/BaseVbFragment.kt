package com.zjx.app_common_library.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.zjx.app_common_library.utils.ext.getVmClazz

abstract class BaseVbFragment<VB : ViewBinding> : Fragment() {
    private var _mViewBinding: VB? = null
    val mViewBinding: VB
        get() = _mViewBinding!!
    private var isFirst: Boolean = true

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _mViewBinding = createViewBinding()
        return _mViewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        createObserver()
    }

    /**
     * 创建ViewBinding
     */
    private fun createViewBinding(): VB {
        val clazz = getVmClazz<Class<VB>>(this)
        val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    //首次加载
    abstract fun firstLazyLoadData()


    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED) {
            if (isFirst) {
                firstLazyLoadData()
            } else {
                lazyLoadData()
            }
            isFirst = false
        }
    }

    override fun onDestroyView() {
        _mViewBinding = null
        super.onDestroyView()
    }
}