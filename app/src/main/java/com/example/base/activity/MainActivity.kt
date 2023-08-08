package com.example.base.activity

import android.view.View
import com.example.base.R
import com.example.base.base.BaseActivity
import com.example.base.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initViews() {
    }

    override fun initViewBinding(view: View): ActivityMainBinding {
        return ActivityMainBinding.bind(view)
    }
}