package com.keloop.gankiok.constant

/**
 * 列表footer状态
 */
// 正在加载
const val LOADING = 1
// 加载完成
const val LOADING_COMPLETE = 2
// 加载到底
const val LOADING_END = 3
//加载失败
const val LOADING_ERROR = 4

/**
 * 列表类型
 */
const val FOOTER_VIEW_TYPE = 0
const val BLOG_CONTENT_VIEW_TYPE = 1

// 生成fragment是传递参数的key
const val ARG_PARAM = "param_key"