package com.example.base.ui.splash

import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.base.R
import com.example.base.SingleEventObserver
import com.example.base.base.BaseFragment
import com.example.base.databinding.SplashFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashFragmentBinding>() {
    override val viewModel: SplashViewModel by viewModel()
    override val layoutResource: Int
        get() = R.layout.splash_fragment

    override fun initViewBinding(view: View): SplashFragmentBinding =
        SplashFragmentBinding.bind(view)

    override fun initViews() {
    }

    override fun observeViewModel() {
        viewModel.repoSelectedLiveData.observe(this, SingleEventObserver {
            if (!it) {
                findNavController().navigate(R.id.action_splashFragment_to_translateFragment)
            }
        })
    }
}