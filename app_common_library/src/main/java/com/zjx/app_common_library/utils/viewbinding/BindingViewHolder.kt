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

package com.dylanc.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author Dylan Cai
 */

inline fun <reified VB : ViewBinding> BaseViewBindingHolder(parent: ViewGroup) =
        BaseViewBindingHolder(inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false))

class BaseViewBindingHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root) {
    constructor(block: (LayoutInflater, ViewGroup, Boolean) -> VB, parent: ViewGroup) :
            this(block(LayoutInflater.from(parent.context), parent, false))
}

inline fun <reified VB : ViewBinding> BindingViewHolder(parent: ViewGroup) =
        BindingViewHolder(inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false))

class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
    constructor(block: (LayoutInflater, ViewGroup, Boolean) -> VB, parent: ViewGroup) :
            this(block(LayoutInflater.from(parent.context), parent, false))
}
