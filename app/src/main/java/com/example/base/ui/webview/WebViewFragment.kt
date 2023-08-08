package com.example.base.ui.webview

import android.view.View
import com.example.base.R
import com.example.base.base.BaseFragment
import com.example.base.custom.LoadingDialog
import com.example.base.databinding.WebViewFragmentBinding
import com.example.base.doOnProgressCompleted
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebViewFragment : BaseFragment<WebViewFragmentBinding>() {
    override val viewModel: WebViewViewModel by viewModel()
    override val layoutResource: Int
        get() = R.layout.web_view_fragment

    override fun initViewBinding(view: View): WebViewFragmentBinding {
        return WebViewFragmentBinding.bind(view)
    }

    private val loadingDialog by lazy {
        LoadingDialog(requireActivity()).apply {
            setCancelable(false)
            lifecycle.addObserver(this)
        }
    }

    override fun initViews() {
        val bundle = arguments
        val link = bundle?.getString("link")
        if (link != null) {
            loadingDialog.toggle(true)
            binding.webView.loadUrl(link)
            binding.webView.doOnProgressCompleted { loadingDialog.toggle(false) }
        }
    }
}