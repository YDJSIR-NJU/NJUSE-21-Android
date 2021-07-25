package com.example.douin.list
import java.io.Serializable

/**
 * 视频列表数据类
 *
 * @author 单金明
 */
data class News (val title:String ,val likes:String ,val imageUrl:String,val author:String,val headerUrl:String):Serializable
