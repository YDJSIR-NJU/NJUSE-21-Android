package com.example.douin.video;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.douin.R;
import com.example.douin.play.ViewPagerLayoutManagerActivity;
import com.example.douin.user.Info;
import com.example.douin.video.db.VDBHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 录制视频并上传活动
 *
 * @author 方昊
 */
public class VideoActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    Camera mCamera;
    Uri photoUri;
    String fileUriS;
    private String path;
    private MediaRecorder mMediaRecorder = new MediaRecorder();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video);

//        try {
//            prepareVideoRecorder();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        dispatchTakeVideoIntent();

    }

    //调用相机应用录制视频
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            //相机可用

            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {

                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HH_mmss").format(new Date());

                try {
                    photoUri = FileProvider.getUriForFile(
                            this,
                            getPackageName() + ".provider",
                            Objects.requireNonNull(createMediaFile()));
                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    takeVideoIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

                onActivityResult(REQUEST_VIDEO_CAPTURE, RESULT_OK, takeVideoIntent);
            }
        }
    }

    private File createMediaFile() throws IOException {
        // 如果 SD 卡存在，则在外部存储建立一个文件夹用于存放视频
        if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            // 选择自己的文件夹
            String path = Environment.getExternalStorageDirectory().getPath();
            // Constants.video_url 是一个常量，代表存放视频的文件夹
            File mediaStorageDir = new File(path + "/vid");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("TAG", "文件夹创建失败");
                    return null;
                }
            }
            // 文件根据当前的时间戳给自己命名
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HH_mmss").format(new Date());
            String imageFileName = "V" + timeStamp;
            String suffix = ".mp4";
            fileUriS = mediaStorageDir + File.separator + imageFileName + suffix;
            System.out.println(fileUriS);
            return new File(mediaStorageDir + File.separator + imageFileName + suffix);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//            Uri videoUri = intent.getData();
            Uri videoUri = Uri.parse(fileUriS);
            VideoView fullvideoView = findViewById(R.id.video_view1);
            fullvideoView.setVideoURI(videoUri);

            fullvideoView.start();

            fullvideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    mp.setLooping(true);
                    return false;
                }
            });

            findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击事件Log.d("YDJSIR",videoUri.toString());

                    Intent i = new Intent(getApplicationContext(), ViewPagerLayoutManagerActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMediaRecorder.setOnErrorListener(null);
                    mMediaRecorder.setOnInfoListener(null);
                    mMediaRecorder.setPreviewDisplay(null);
                    if (mMediaRecorder != null) {
                        try {
                            mMediaRecorder.stop();
                            mMediaRecorder.reset();
                            mMediaRecorder.release();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            mMediaRecorder = null;
//                            mMediaRecorder = new MediaRecorder();
//                            mMediaRecorder.stop();
                        }
                        mMediaRecorder = null;
                    }
                    VDBHelper V = new VDBHelper(VideoActivity.this);
                    Intent i;
                    if (V.insert(Info.getinstance().user_email, fileUriS)) {
                        Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_LONG).show();
                        i = new Intent(getApplicationContext(), MyvideosActivity.class);
                    } else {
                        Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_LONG).show();
                        i = new Intent(getApplicationContext(), ViewPagerLayoutManagerActivity.class);
                    }
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    private void prepareVideoRecorder() throws IOException {
        Camera camera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (camera != null) {
            camera.setDisplayOrientation(90);
            camera.unlock();
            mMediaRecorder.setCamera(camera);

        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HH_mmss").format(new Date());

        String dir = Environment.getExternalStorageDirectory().toString();

        path = dir + "/" + timeStamp + ".mp4";

        System.out.println(path);
        System.out.println("YDJSIR");

        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mMediaRecorder.setProfile(mProfile);
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置录制的视频编码和音频编码
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mMediaRecorder.setVideoSize(1920, 1080);
        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoEncodingBitRate(1024 * 1024 * 20);
        mMediaRecorder.setOutputFile(path);

        mMediaRecorder.prepare();
        try {
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Camera getCamera(int position) {
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = null;
        try {
            cam = Camera.open(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cam;
    }

    private void releaseCameraAndPreview() {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }


}
