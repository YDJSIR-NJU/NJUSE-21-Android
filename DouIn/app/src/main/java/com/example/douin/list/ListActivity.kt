package com.example.douin.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.douin.R
import com.example.douin.play.ViewPagerLayoutManagerActivity
import com.example.douin.user.Info
import com.example.douin.util.network.FeedParser
import com.example.douin.util.network.VideoItem
import java.util.*

/**
 * 视频列表页面
 *
 * @author 单金明
 */
class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
//        if (Build.VERSION.SDK_INT > 9) {
//            val policy = ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)
//        }
        var videoItems: ArrayList<VideoItem>? = null

        val v_RecyclerView = findViewById<RecyclerView>(R.id.v_RecyclerView) as RecyclerView
        val mRunnable = Runnable {
            val feedParser = FeedParser()
            videoItems = feedParser.getVideoList()
            runOnUiThread {
                Info.getinstance().videoItemArrayList = videoItems
                Info.getinstance().isused = true
                v_RecyclerView.layoutManager = LinearLayoutManager(this)
                val myAdapter = videoItems?.let { MyAdapter(it) }
                if (myAdapter != null) {
                    myAdapter.registerAdapterDataObserver(object :
                        RecyclerView.AdapterDataObserver() {
                    })
                }
                v_RecyclerView.adapter = myAdapter
            }
        }
        Thread(mRunnable).start()
        val mRunnable2 = Runnable {
            Thread.sleep(10000);
            runOnUiThread {
                val progressBar = findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.GONE;
            }
        }
        Thread(mRunnable2).start()
    }

    inner class MyAdapter(val newsList: List<VideoItem>) : RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.v_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val news = newsList[position]
            holder.description.text = news.description
            holder.nickname.text = news.nickname
            holder.likecount.text = news.likecount.toString()
//            holder.glide. = news.glide

            Glide.with(this@ListActivity).load(news.thumbnailURI).into(holder.thumbnailURI)
            Glide.with(this@ListActivity).load(news.avatarURI).into(holder.avatarURI)

            //添加按钮、跳转
            holder.itemView.setOnClickListener {
                val intent = Intent(this@ListActivity, ViewPagerLayoutManagerActivity::class.java)
                Info.getinstance().position = holder.position
                startActivity(intent)
//                finish()
            }
        }

        override fun getItemCount(): Int {
            return newsList.size
        }
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val description: TextView = itemView.findViewById(R.id.title)
    val thumbnailURI: ImageView = itemView.findViewById(R.id.glide)
    val nickname: TextView = itemView.findViewById(R.id.author)
    val likecount: TextView = itemView.findViewById(R.id.likes)
    val avatarURI: com.makeramen.roundedimageview.RoundedImageView =
        itemView.findViewById(R.id.header)

    //这里需要在item中添加id,然后在这里添加其他的元素，
}