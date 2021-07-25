package com.example.douin.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.douin.R;
import com.example.douin.list.ListActivity;
import com.example.douin.play.OnViewPagerListener;
import com.example.douin.play.ViewPagerLayoutManager;
import com.example.douin.play.ViewPagerLayoutManagerActivity;
import com.example.douin.user.Info;
import com.example.douin.video.db.VDBHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * 用于查看自己的视频的ViewPager管理器活动
 *
 * @author 方昊
 */
public class MyvideosActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerActivity";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myvideo);
        context = this;

        initView();

        initListener();

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler1);

        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        TextView play = findViewById(R.id.play1);
        play.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        /**
         * 查看列表页面
         */
        TextView recommend = findViewById(R.id.recommend1);

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件
                Toast.makeText(getApplicationContext(), "精彩马上就来", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(i);
                finish(); // 当前播放页面没必要保留
            }
        });

        findViewById(R.id.play1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件
                Intent i = new Intent(getApplicationContext(), ViewPagerLayoutManagerActivity.class);
                startActivity(i);
                finish(); // 当前播放页面没必要保留
            }
        });
        /**
         * 翻页相关
         */
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onInitComplete() {
                Log.e(TAG, "onInitComplete");
                playVideo(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
                playVideo(0);
            }


        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.tv_hv);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play1);
//        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                mp.setLooping(true);
//                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });

    }

    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.tv_hv);
//        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play1);
        videoView.stopPlayback();
//        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        //        private int[] imgs = {R.mipmap.img_video_1,R.mipmap.img_video_2};
        ArrayList<String> videos = new ArrayList<String>();// 存的都是URI
//        private ArrayList<String> imgs = new ArrayList<String>();

//        private int[] likes ={50,60};

        public MyAdapter() {
            VDBHelper V = new VDBHelper(context);
            ArrayList<String> videoList = V.getUri(Info.getinstance().user_email);

            Log.d("12314", videoList.size() + "");
            for (int i = 0; i < videoList.size(); i++) {
                videos.add(videoList.get(i));
                Log.d("12314", videoList.get(i) + "");
//                        imgs.add(videoList.get(i).thumbnailURI);
            }
        }


        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_video, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
            // 加载封面图
//            Glide.with(context).load(imgs.get(position % imgs.size())).into(holder.img_thumb);
            // 加载视频
            if(videos.size() != 0) {
                String curURI = videos.get(position % videos.size());
                holder.videoView.setVideoURI(Uri.parse(curURI));
                int lenOfCurURI = curURI.length();
                String Date = curURI.substring(lenOfCurURI - 20, lenOfCurURI - 6);
//            holder.videoView.setVideoPath(videos[position % videos.length]);
                holder.date.setText(Date);
            }
            else{
                Toast.makeText(getApplicationContext(), "您还没有拍摄过视频！", Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            TextView date;

            public ViewHolder(View itemView) {
                super(itemView);
//                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.tv_hv);
                img_play = itemView.findViewById(R.id.img_play1);
                date = itemView.findViewById(R.id.textView6);
            }
        }
    }

}

