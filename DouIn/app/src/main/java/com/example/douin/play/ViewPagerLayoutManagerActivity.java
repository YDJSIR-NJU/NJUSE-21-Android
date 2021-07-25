package com.example.douin.play;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
;import com.bumptech.glide.Glide;
import com.example.douin.user.Info;
import com.example.douin.R;
import com.example.douin.list.ListActivity;
import com.example.douin.util.network.FeedParser;
import com.example.douin.util.network.VideoItem;
import com.example.douin.video.MyvideosActivity;
import com.example.douin.video.VideoActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * ViewPager管理器活动
 *
 * @author 方昊
 */
public class ViewPagerLayoutManagerActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerActivity";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_video);
        context = this;
//        网络请求已经改成异步，下面是历史遗留问题
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

        initView();

        initListener();

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler);

        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (Info.getinstance().isused) {
            mLayoutManager.scrollToPositionWithOffset(Info.getinstance().position, 0);
            mLayoutManager.setStackFromEnd(true);
            Info.getinstance().isused = false;
        }
    }

    private void initListener() {
        TextView play = findViewById(R.id.play);
        play.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        /**
         * 跳转到录制视频页面
         */
        ImageView add_video = findViewById(R.id.add_video);
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件
                Intent i = new Intent(getApplicationContext(), VideoActivity.class);
                startActivity(i);
            }
        });

        /**
         * 查看列表页面
         */
        TextView recommend = findViewById(R.id.recommend);

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
        findViewById(R.id.my_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件
                Intent i = new Intent(getApplicationContext(), MyvideosActivity.class);
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

    long mLastTime = 0;
    long mCurTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final TextView like = itemView.findViewById(R.id.click_heart);
        final RainView rainView = itemView.findViewById(R.id.rain_View);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        final ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        videoView.start();
        videoView.setOnInfoListener((mp, what, extra) -> {
            mediaPlayer[0] = mp;
            mp.setLooping(true);
            imgThumb.animate().alpha(0).setDuration(20).start();
            progressBar2.setVisibility(View.GONE);
            return false;
        });
        videoView.setOnPreparedListener(mp -> {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
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

        /**
         * 双击屏幕点赞
         */
        videoView.setOnClickListener(v -> {

            mLastTime = mCurTime;
            mCurTime = System.currentTimeMillis();

            if (mCurTime - mLastTime < 1000) {//双击事件
                mCurTime = 0;
                mLastTime = 0;
                char c = like.getText().toString().charAt(like.getText().toString().length() - 1);
                if (Character.isDigit(c)) {
                    like.setText(parseLike(Integer.parseInt(like.getText().toString()) + 1));
                }
                rainView.start(true);

            }

        });

    }

    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//        private int[] imgs = {R.mipmap.img_video_1,R.mipmap.img_video_2};
        private ArrayList<String> videos = new ArrayList<String>();// 存的都是URI
        private ArrayList<String> imgs = new ArrayList<String>();
        private ArrayList<Integer> likes = new ArrayList<Integer>();
        private ArrayList<String> names = new ArrayList<String>();
        private ArrayList<String> des = new ArrayList<String>();
        private ArrayList<String> userimgs = new ArrayList<String>();

//        private int[] likes ={50,60};

        public MyAdapter(){
            Thread netWorkRequest = new Thread(new Runnable(){
                @Override
                public void run() {
                    FeedParser feedParser = new FeedParser();
                    ArrayList<VideoItem> videoList = feedParser.getVideoList();
                    for (VideoItem videoItem : videoList) {
                        Log.d("NETR", videoItem.toString());
                    }
                    try {
                        for (int i = 0; i < videoList.size(); i++) {
                            videos.add(videoList.get(i).feedURL);
                            imgs.add(videoList.get(i).thumbnailURI);
                            likes.add(videoList.get(i).likecount);
                            names.add(videoList.get(i).nickname);
                            des.add(videoList.get(i).description);
                            userimgs.add(videoList.get(i).avatarURI);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            netWorkRequest.start();
            try{
                netWorkRequest.join(2000); // 给你两秒加载，加载不完拉倒
            }catch (Exception e){
                e.printStackTrace();
            }

//  videos=new ArrayList<String>();
//            videos.add("https://ydjsir-edu.oss-cn-shanghai.aliyuncs.com/SE2/5A446/douinDemo/video_1.mp4");
//            videos.add("https://ydjsir-edu.oss-cn-shanghai.aliyuncs.com/SE2/5A446/douinDemo/video_2.mp4");
//            videos.add("https://ydjsir-edu.oss-cn-shanghai.aliyuncs.com/SE2/5A446/douinDemo/video_3.mp4");
//            videos.add("https://ydjsir-edu.oss-cn-shanghai.aliyuncs.com/SE2/5A446/douinDemo/video_4.mp4");

        }


        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
            if(videos.size() != 0) { // 如果Video为0，下面的就没必要做了
                // 加载封面图
                Glide.with(context).load(imgs.get(position % imgs.size())).into(holder.img_thumb);
                // 加载视频
                holder.videoView.setVideoURI(Uri.parse(videos.get(position % videos.size())));


                holder.like.setText(parseLike(likes.get(position % videos.size())));

                holder.name.setText("@" + names.get(position % videos.size()));

                holder.description.setText(des.get(position % videos.size()));

                Glide.with(context).load(userimgs.get(position % videos.size())).into(holder.userimage);
            }
            else{
                Toast.makeText(getApplicationContext(), "当前加载的视频列表为空！", Toast.LENGTH_LONG).show();
            }

//            holder.videoView.setVideoPath(videos[position % videos.length]);
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击事件
                    if (v.getId() == R.id.click_heart) {
                        Log.d("like", "ok");
                        char c = holder.like.getText().toString().charAt(holder.like.getText().toString().length() - 1);
                        if (Character.isDigit(c)) {
                            holder.like.setText(parseLike(Integer.parseInt(holder.like.getText().toString()) + 1));
                        }
                        holder.rainView.start(true);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return videos.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            TextView like;
            RainView rainView;
            TextView name;
            TextView description;
            ImageView userimage;

            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                like = itemView.findViewById(R.id.click_heart);
                rainView = itemView.findViewById(R.id.rain_View);
                name = itemView.findViewById(R.id.nickname);
                description = itemView.findViewById(R.id.description);
                userimage = itemView.findViewById(R.id.user_image);
            }
        }
    }

    /**
     * 针对点赞数格式化应该显示的字符串
     * @param like 点赞数
     * @return 格式化后的字符串
     *
     * @author YDJSIR
     */
    private String parseLike(int like) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
        if (like > 1000000) {
            return df.format(like / 1000000.0) + "m";
        }
        if (like >= 10000) {
            return df.format(like / 10000.0) + "w";
        }
        if (like >= 1000) {
            return df.format(like / 1000.0) + "k";
        }
        return like + "";
    }
}

