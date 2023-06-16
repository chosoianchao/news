package com.example.base.ui.translate


import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.base.R
import com.example.base.base.BaseFragment
import com.example.base.custom.LoadingDialog
import com.example.base.custom.ZoomOutPageTransformer
import com.example.base.databinding.NewsFragmentBinding

import com.example.base.getErrorMessage
import com.example.base.model.Item
import com.example.base.network.Response
import com.example.base.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException

class NewsFragment : BaseFragment<NewsFragmentBinding>() {
    private var viewPager: NewsAdapter? = null
    private val loadingDialog by lazy {
        LoadingDialog(requireActivity()).apply {
            lifecycle.addObserver(this)
        }
    }
    override val viewModel: NewsViewModel by viewModel()
    override val layoutResource: Int
        get() = R.layout.news_fragment

    override fun initViews() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@NewsFragment.viewModel
        }
        viewModel.getNews()
        viewPager = NewsAdapter(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {
                val bundle = bundleOf(
                    "link" to item.link
                )
                findNavController().navigate(
                    R.id.action_translateFragment_to_webViewFragment,
                    bundle
                )
            }
        })
        binding.vpNews.adapter = viewPager
        binding.vpNews.clipToPadding = false
        binding.vpNews.clipChildren = false
        binding.vpNews.offscreenPageLimit = 10
        binding.vpNews.layoutDirection = View.LAYOUT_DIRECTION_LTR
        binding.vpNews.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        binding.vpNews.setPageTransformer(ZoomOutPageTransformer())
    }

    override fun observeViewModel() {
        viewModel.responseLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> loadingDialog.toggle(response.status)
                is Response.Error<*> -> {
                    if ((response.throwable as? HttpException)?.code() == 404) {
                        getString(R.string.network_error).toast(requireContext())
                    } else {
                        response.throwable?.getErrorMessage(requireContext())
                            ?.toast(requireContext())
                    }
                    loadingDialog.toggle(false)
                }
                is Response.Success -> {
                    loadingDialog.toggle(false)
                    viewModel.convertStreamToString(response.data.byteStream())
                }
                else -> {}
            }
        }
        viewModel.result.observe(viewLifecycleOwner) {
            viewModel.parseXmlData(it)
        }
        viewModel.data.observe(viewLifecycleOwner) {
            viewPager?.setListNews(it)
        }
    }
}