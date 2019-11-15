package com.keloop.gankiok

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_blog.*


class BlogFragment : Fragment() {

    lateinit var mContext: Context
    val ARG_PARAM = "param_key"

    private var name: String? = ""

    private lateinit var adapter: BlogAdapter

    private lateinit var disposable: Disposable

    private var mPage: Int = 1

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    companion object

    fun newInstance(argParam: String): BlogFragment {
        val blogFragment = BlogFragment()
        val arg = Bundle()
        arg.putString(ARG_PARAM, argParam)
        blogFragment.arguments = arg
        return blogFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BlogAdapter()
        adapter.setOnItemClickListener(object : BlogAdapter.OnItemClickListener {
            override fun onClick(url: String) {
                val intent = Intent(mContext, WebActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            }
        })
        name = arguments?.getString(ARG_PARAM)
        swipeLayout.setOnRefreshListener {
            getBlogs(true)
        }
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                adapter.setLoadState(adapter.LOADING)
                getBlogs(false)
            }
        })
        getBlogs(true)
    }

    fun getBlogs(isRefresh: Boolean) {
        if (isRefresh) mPage = 1
        RetrofitWrap.service(Api::class.java)
            .getBlog(name!!, 10, mPage)
            .map {
                it.results
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Blog>> {
                override fun onSuccess(t: List<Blog>) {
                    swipeLayout.isRefreshing = false
                    if (isRefresh) {
                        adapter.setBlogs(t)
                    } else {
                        adapter.addBlogs(t)
                    }
                    adapter.setLoadState(adapter.LOADING_COMPLETE)
                    mPage++
                }

                override fun onSubscribe(d: Disposable) {
                    if (isRefresh) swipeLayout.isRefreshing = true
                    disposable = d
                }

                override fun onError(e: Throwable) {
                    swipeLayout.isRefreshing = false
                    adapter.setLoadState(adapter.LOADING_ERROR)
                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }

}