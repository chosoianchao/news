package com.example.base.ui.splash

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

    override fun initViews() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SplashFragment.viewModel
        }
    }

    override fun observeViewModel() {
        viewModel.repoSelectedLiveData.observe(this, SingleEventObserver {
            if (!it) {
                findNavController().navigate(R.id.action_splashFragment_to_translateFragment)
            }
        })
    }
}