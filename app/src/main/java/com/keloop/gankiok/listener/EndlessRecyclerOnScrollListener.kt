package com.keloop.gankiok.listener


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * RecyclerView滑动监听
 * Created by yangle on 2017/10/12.
 */

abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {

    // 用来标记是否正在向上滑动
    private var isSlidingUpward = false

    private var lastPositions: IntArray? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val manager = recyclerView.layoutManager
        // 当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 获取最后一个完全显示的itemPosition
            var lastItemPosition = 0
            if (manager is LinearLayoutManager) {
                lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
            }
            if (manager is StaggeredGridLayoutManager) {
                val staggeredGridLayoutManager = manager as StaggeredGridLayoutManager?
                staggeredGridLayoutManager!!.invalidateSpanAssignments()
                if (lastPositions == null) {
                    lastPositions = IntArray(staggeredGridLayoutManager.spanCount)
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions)
                lastItemPosition = findMax(lastPositions!!)
            }

            val itemCount = manager!!.itemCount

            // 判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == itemCount - 1 && isSlidingUpward) {
                // 加载更多
                onLoadMore()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUpward = dy > 0
    }

    /**
     * 加载更多回调
     */
    abstract fun onLoadMore()

    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }
}
