/*
 * Copyright 2019 vmadalin.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demonstrationapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.demonstrationapp.extensions.toast
import com.example.demonstrationapp.utils.observeEvent

/**
 * Base fragment to standardize and simplify initialization for this component.
 *
 * @param layoutId Layout resource reference identifier.
 * @see Fragment
 */
abstract class BaseFragment<VB : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
) : Fragment() {

    private var _binding: VB? = null

    val binding get() = _binding!!

//    abstract val viewModel: BaseViewModel?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel?.apply {
//            isLoading.observe(viewLifecycleOwner) {
//
//            }
//
//            viewModel?.error?.observeEvent(viewLifecycleOwner) { exception ->
//                if (exception.message?.isNotEmpty() == true) {
//                    toast(exception.message.orEmpty())
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
