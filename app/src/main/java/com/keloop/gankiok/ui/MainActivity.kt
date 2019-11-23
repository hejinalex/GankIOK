package com.keloop.gankiok.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.keloop.gankiok.R
import com.keloop.gankiok.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragments: List<BlogFragment>

    private var menuItem: MenuItem? = null

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavi.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_android -> {
                    viewPager.currentItem = 0
                }
                R.id.menu_ios -> {
                    viewPager.currentItem = 1
                }
                R.id.menu_web -> {
                    viewPager.currentItem = 2
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    menuItem?.isChecked = false
                } else {
                    bottomNavi.menu.getItem(0).isChecked = false
                }
                menuItem = bottomNavi.menu.getItem(position)
                menuItem?.isChecked = true
            }

        })

        val androidFragment = BlogFragment().newInstance("Android")
        val iOSFragment = BlogFragment().newInstance("iOS")
        val webFragment = BlogFragment().newInstance("前端")

        fragments = listOf(androidFragment, iOSFragment, webFragment)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, fragments)
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2
    }

}
