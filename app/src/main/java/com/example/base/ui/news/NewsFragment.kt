package com.example.base.ui.news

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.base.R
import com.example.base.adapter.NewsAdapter
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
    private var newsAdapter: NewsAdapter? = null
    private var popupMenu: PopupMenu? = null
    private val loadingDialog by lazy {
        LoadingDialog(requireActivity()).apply {
            lifecycle.addObserver(this)
        }
    }
    override val viewModel: NewsViewModel by viewModel()
    override val layoutResource: Int
        get() = R.layout.news_fragment

    override fun initViewBinding(view: View): NewsFragmentBinding = NewsFragmentBinding.bind(view)

    override fun initViews() {
        initNews()
        popupMenu = PopupMenu(context, binding.ivMenu)
        popupMenu?.let {
            it.menuInflater.inflate(R.menu.menu_popup, it.menu)
            it.setOnMenuItemClickListener { item ->
                if (item?.itemId == R.id.item_all) {
                    viewModel.getNews()
                } else if (item.itemId == R.id.id_1_hour) {
                    viewModel.sortItem1Hour()
                } else if (item.itemId == R.id.item_1_day) {
                    viewModel.sortItemDays(day = 1)
                } else if (item.itemId == R.id.item_3_day) {
                    viewModel.sortItemDays(3)
                }
                false
            }
        }
        popupMenu?.setOnDismissListener {
            binding.ivMenu.setImageLevel(0)
        }
        binding.ivMenu.setOnClickListener {
            binding.ivMenu.setImageLevel(1)
            popupMenu?.show()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.isNotBlank() == true) {
                    viewModel.data.value = viewModel.data.value?.filter {
                        it.title.contains(p0.toString())
                    } as ArrayList<Item>?
                } else {
                    viewModel.data.value = viewModel.data.value
                }
            }
        })
    }

    private fun initNews() {
        viewModel.getNews()
        newsAdapter = NewsAdapter(object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(item: Item, key: String) {
                when (key) {
                    "title" -> {
                        val bundle = bundleOf(
                            "link" to item.link
                        )
                        findNavController().navigate(
                            R.id.action_translateFragment_to_webViewFragment,
                            bundle
                        )
                    }
                    "like" -> {
                        viewModel.insertNews(item)
                    }
                    "unlike" -> {
                        viewModel.deleteNews(item)
                    }
                }
            }
        })
        binding.vpNews.adapter = newsAdapter
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
            newsAdapter?.submitList(it)
        }
    }
}