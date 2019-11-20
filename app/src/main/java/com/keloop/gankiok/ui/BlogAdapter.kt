package com.keloop.gankiok.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keloop.gankiok.*
import com.keloop.gankiok.constant.*
import com.keloop.gankiok.model.Blog

class BlogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var blog: MutableList<Blog> = mutableListOf()

    // 当前加载状态，默认为加载完成
    private var loadState = LOADING_COMPLETE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        return if (viewType == FOOTER_VIEW_TYPE) {
            itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
            FooterViewHolder(itemView)
        } else {
            itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_blog, parent, false)
            BlogViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int) =
        if (position == blog.size) FOOTER_VIEW_TYPE else BLOG_CONTENT_VIEW_TYPE

    override fun getItemCount(): Int {
        return if (blog.isEmpty()) 0 else blog.size + 1
    }

    fun setBlog(blog: List<Blog>) {
        this.blog.clear()
        this.blog.addAll(blog)
    }

    fun addBlog(blog: List<Blog>) {
        this.blog.addAll(blog)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BlogViewHolder) {
            holder.bindData(blog[position])
            holder.itemView.setOnClickListener {
                listener.onClick(blog[position].url)
            }
        } else if (holder is FooterViewHolder) {
            when (this.loadState) {
                LOADING // 正在加载
                -> {
                    holder.pbLoading.visibility = View.VISIBLE
                    holder.tvLoading.visibility = View.VISIBLE
                    holder.llEnd.visibility = View.GONE
                }
                LOADING_COMPLETE // 加载完成
                -> {
                    holder.pbLoading.visibility = View.INVISIBLE
                    holder.tvLoading.visibility = View.INVISIBLE
                    holder.llEnd.visibility = View.GONE
                }

                LOADING_END // 加载到底
                -> {
                    holder.pbLoading.visibility = View.GONE
                    holder.tvLoading.visibility = View.GONE
                    holder.llEnd.visibility = View.VISIBLE
                }
                LOADING_ERROR // 加载失败
                -> {
                    holder.pbLoading.visibility = View.GONE
                    holder.tvLoading.visibility = View.GONE
                    holder.llEnd.visibility = View.GONE
                }
            }
        }
    }

    class BlogViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tv_author)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time)

        fun bindData(blog: Blog) {
            tvTitle.text = blog.desc
            tvAuthor.text = blog.who
            tvTime.text = blog.publishedAt.substring(0, 10)
        }
    }

    fun setLoadState(loadState: Int) {
        this.loadState = loadState
        notifyDataSetChanged()
    }

    class FooterViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pbLoading: ProgressBar = itemView.findViewById(R.id.pb_loading)
        val tvLoading: TextView = itemView.findViewById(R.id.tv_loading)
        val llEnd: LinearLayout = itemView.findViewById(R.id.ll_end)
    }

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(url: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}