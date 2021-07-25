package com.example.douin.play;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.douin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 表情雨效果
 *
 * @author 方昊
 */
public class RainView extends View {
    private Paint paint;
    //图片处理
    private Matrix matrix;
    private Random random;
    //判断是否运行的，默认没有
    private boolean isRun;
    //表情包集合
    private List<ItemEmoji> bitmapList;

    private int[] imgResIds = {
            R.mipmap.emoji2,
            R.mipmap.emoji3,
            R.mipmap.emoji4,
            R.mipmap.emoji5,
            R.mipmap.emoji6,
            R.mipmap.emoji7,
    };

    public RainView(Context context) {
        this(context, null);
    }

    public RainView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int randomEmoji() {
        int rand = random.nextInt(100);
        if (rand % 3 == 0) {
            return R.mipmap.emoji3;
        } else if (rand % 5 == 0) {
            return R.mipmap.emoji5;
        } else if (rand % 4 == 0) {
            return R.mipmap.emoji7;
        } else if (rand % 11 == 0) {
            return R.mipmap.emoji4;
        } else if (rand % 13 == 0) {
            return R.mipmap.emoji6;
        }
        return R.mipmap.emoji2;
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        matrix = new Matrix();
        random = new Random();
        bitmapList = new ArrayList<>();
    }

    private void initData() {
        int numOfEmoji = random.nextInt(5);
        for (int i = 0; i < numOfEmoji; i++) {
            ItemEmoji itemEmoji = new ItemEmoji();
            //表情图片
            int imgResId = randomEmoji();
            itemEmoji.bitmap = BitmapFactory.decodeResource(getResources(), imgResId);
            //起始横坐标在[100,getWidth()-100) 之间
            itemEmoji.x = random.nextInt(getWidth() - 200) + 100;
            //起始纵坐标在(-getHeight(),0] 之间，即一开始位于屏幕上方以外
            itemEmoji.y = -random.nextInt(getHeight());
            //横向偏移[-2,2) ，即左右摇摆区间
            itemEmoji.offsetX = random.nextInt(4) - 2;
            //纵向固定下落12
            itemEmoji.offsetY = 12;
            //缩放比例[0.8,1.2) 之间
            if (imgResId != R.mipmap.emoji2)
                itemEmoji.scale = (float) (random.nextInt(40) + 80) / 100f;
            else
                itemEmoji.scale = (float) (random.nextInt(20) + 20) / 100f;
            bitmapList.add(itemEmoji);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isRun) {

            //用于判断表情下落结束，结束即不再进行重绘
            boolean isInScreen = false;
            for (int i = 0; i < bitmapList.size(); i++) {
                matrix.reset();
                //缩放
                matrix.setScale(bitmapList.get(i).scale, bitmapList.get(i).scale);
                //下落过程坐标
                bitmapList.get(i).x = bitmapList.get(i).x + bitmapList.get(i).offsetX;
                bitmapList.get(i).y = bitmapList.get(i).y + bitmapList.get(i).offsetY;
                if (bitmapList.get(i).y <= getHeight()) {//当表情仍在视图内，则继续重绘
                    isInScreen = true;
                }
                //位移
                matrix.postTranslate(bitmapList.get(i).x, bitmapList.get(i).y);
                canvas.drawBitmap(bitmapList.get(i).bitmap, matrix, paint);
            }
            if (isInScreen) {
                postInvalidate();
            } else {
                release();
            }
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        if (bitmapList != null && bitmapList.size() > 0) {
            for (ItemEmoji itemEmoji : bitmapList) {
                if (!itemEmoji.bitmap.isRecycled()) {
                    itemEmoji.bitmap.recycle();
                }
            }
            bitmapList.clear();
        }
    }

    public void start(boolean isRun) {
        this.isRun = isRun;
        initData();
        postInvalidate();
    }
}
