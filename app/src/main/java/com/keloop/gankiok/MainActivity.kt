package com.keloop.gankiok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_blog.*

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

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    private fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    private fun AppCompatActivity.showFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { show(fragment) }
    }

    private fun AppCompatActivity.hideFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { hide(fragment) }
    }

}
