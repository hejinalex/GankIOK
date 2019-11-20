package com.keloop.gankiok.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keloop.gankiok.R
import com.keloop.gankiok.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var androidFragment: BlogFragment
    private lateinit var iOSFragment: BlogFragment
    private lateinit var webFragment: BlogFragment

    private lateinit var fragments: Array<BlogFragment>

    private var lastShowFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavi.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_android -> {
                    if (lastShowFragment != 0) {
                        switchFragment(0)
                        lastShowFragment = 0
                    }
                }
                R.id.menu_ios -> {
                    if (lastShowFragment != 1) {
                        switchFragment(1)
                        lastShowFragment = 1
                    }
                }
                R.id.menu_web -> {
                    if (lastShowFragment != 2) {
                        switchFragment(2)
                        lastShowFragment = 2
                    }
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        androidFragment = BlogFragment().newInstance("Android")
        iOSFragment = BlogFragment().newInstance("iOS")
        webFragment = BlogFragment().newInstance("前端")

        fragments = arrayOf(androidFragment, iOSFragment, webFragment)

        switchFragment(0)
    }

    private fun switchFragment(index: Int) {
        hideFragment(fragments[lastShowFragment])
        if (!fragments[index].isAdded) {
            addFragment(fragments[index], R.id.flContent)
        }
        showFragment(fragments[index])
    }

}
