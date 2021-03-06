/*
 * Copyright (c) 2020. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.zjx.app_common_library.utils.viewbinding

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * @author Dylan Cai
 */

inline fun <reified VB : ViewBinding> Activity.binding() = lazy {
  inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> Dialog.binding() = lazy {
  inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> Fragment.binding() =
  FragmentBindingDelegate(VB::class.java)

@JvmName("inflate")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> inflateBinding(clazz: Class<VB>, layoutInflater: LayoutInflater) =
  clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
  inflateBinding(VB::class.java, layoutInflater)

@JvmName("inflate")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> inflateBinding(
  clazz: Class<VB>, layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
  clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

inline fun <reified VB : ViewBinding> inflateBinding(
  layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean
) =
  inflateBinding(VB::class.java, layoutInflater, parent, attachToParent)

class FragmentBindingDelegate<VB : ViewBinding>(
  private val clazz: Class<VB>
) : ReadOnlyProperty<Fragment, VB> {

  private var isInitialized = false
  private var _binding: VB? = null
  private val binding: VB get() = _binding!!

  @Suppress("UNCHECKED_CAST")
  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (!isInitialized) {
      thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
          _binding = null
        }
      })
      _binding = clazz.getMethod("bind", View::class.java)
        .invoke(null, thisRef.requireView()) as VB
      isInitialized = true
    }
    return binding
  }
}