package com.example.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DataBinding : ViewDataBinding> : Fragment() {
    private var _binding: DataBinding? = null
    protected val binding: DataBinding
        get() = requireNotNull(_binding)
    protected abstract val viewModel: BaseViewModel
    protected abstract val layoutResource: Int
    protected abstract fun initViews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil
        .inflate<DataBinding>(inflater, layoutResource, container, false)
        .apply { _binding = this }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    protected open fun observeViewModel() {
    }
}