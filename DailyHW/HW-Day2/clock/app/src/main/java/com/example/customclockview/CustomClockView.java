package com.example.customclockview;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomClockView extends View {

    private Paint mPaint;
    /**
     * 描边线的粗细
     */
    private final int strokeWidth = 2;
    /**
     * 时钟是否在走（即是否第一次onDraw）
     */
    private boolean isFirstRunning;

    private Handler mHandler;
    private Runnable clockRunnable;

    /**
     * 时钟圆的半径
     */
    private final int radius = 150;

    private final String[] clockNumbers = {"12","1","2","3","4","5","6","7","8","9","10","11"};
    /**
     * 时钟上需要绘制的数字
     */
    private String num;

    /**
     * 用于测量文本的宽、高度（这里主要是来获取高度）
     */
    private Rect textBounds = new Rect();

    private Calendar cal;

    private int hour,min,second;
    private float hourAngle,minAngle,secAngle;

    public CustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomClockView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mHandler = new Handler();
        clockRunnable = new Runnable() {//里面做的事情就是每隔一秒，刷新一次界面
            @Override
            public void run() {
                //线程中刷新界面
                postInvalidate();
                mHandler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //在这里是无需重载的，但是如果自定义的是一个ViewGroup，那么可能就需要重载这里，可以参考ViewPager的实现
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("weijuncheng", "onDraw");
        super.onDraw(canvas);
        if(!isFirstRunning){
            runClock();
        }else{
            initPaint();
            //绘制圆形部分
            drawClockCircle(canvas);
            //绘制刻度线
            drawClockScale(canvas);
            //绘制数字
            drawClockNumber(canvas);
            //绘制中心原点
            drawClockDot(canvas);
            //绘制三个指针
            drawClockPoint(canvas);
        }
    }

    /**
     * 绘制三个指针
     * @param canvas
     */
    private void drawClockPoint(Canvas canvas) {
        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR);//Calendar.HOUR获取的是12小时制，Calendar.HOUR_OF_DAY获取的是24小时制
        min = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
        //计算时分秒指针各自需要偏移的角度
        hourAngle = (float)hour / 12 * 360 + (float)min / 60 * (360 / (float) 12);//360/12是指每个数字之间的角度
        minAngle = (float)min / 60 * 360;
        secAngle = (float)second / 60 * 360;
        //下面将时、分、秒指针按照各自的偏移角度进行旋转，每次旋转前要先保存canvas的原始状态
        canvas.save();
        canvas.rotate(hourAngle,getWidth() / (float) 2, getHeight() / (float) 2);
        canvas.drawLine(getWidth() / (float) 2, getHeight() / (float) 2, getWidth() / (float) 2, getHeight() / (float) 2 - 65, mPaint);//时针长度设置为65

        canvas.restore();
        canvas.save();
        canvas.rotate(minAngle,getWidth() / (float) 2, getHeight() / (float) 2);
        canvas.drawLine(getWidth() / (float) 2, getHeight() / (float) 2, getWidth() / (float) 2, getHeight() / (float) 2 - 90 , mPaint);//分针长度设置为90

        canvas.restore();
        canvas.save();
        canvas.rotate(secAngle,getWidth() / (float) 2, getHeight() / (float) 2);
        canvas.drawLine(getWidth() / (float) 2, getHeight() / (float) 2, getWidth() / (float) 2, getHeight() / (float) 2 - 110 , mPaint);//秒针长度设置为110

        canvas.restore();
    }

    /**
     * 绘制中心原点
     */
    private void drawClockDot(Canvas canvas) {
        mPaint.reset();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / (float) 2, getHeight() / (float) 2, 6, mPaint);
        initPaint();
    }

    /**
     * 绘制数字（从正上方12点处开始绘制）
     * @param canvas
     */
    private void drawClockNumber(Canvas canvas) {
        //先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
        canvas.save();
        mPaint.setTextSize(25);
        //计算12点处 数字 的坐标
        int preX = getWidth() / 2;
        int preY = getHeight() / 2 - radius - strokeWidth - 10;//10为圆与数字文本之间的间距
        //x，y才是文本真正的准确坐标，需要减去文本的自身宽、高因素
        int x,y;
        //计算画布每次需要旋转的角度
        int degree = 360 / clockNumbers.length;
        for(int i = 0; i < clockNumbers.length; i++){
            num = clockNumbers[i];
            mPaint.getTextBounds(num, 0, num.length(), textBounds);
            x = (int) (preX - mPaint.measureText(num) / 2);
            y = preY - textBounds.height();//从文本的中心点处开始绘制
            canvas.drawText(num, x, y, mPaint);
            canvas.rotate(degree, getWidth() / (float) 2, getHeight() / (float) 2);//以圆中心进行旋转
        }
        //绘制完后，记得把画布状态复原
        canvas.restore();
    }

    /**
     * 绘制刻度线（总共60条）
     * 从正上方，即12点处开始绘制一条直线，后面的只是旋转一下画布角度即可
     * @param canvas
     */
    private void drawClockScale(Canvas canvas) {
        //先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
        canvas.save();
        //计算12点处刻度的开始坐标
        int startX = getWidth() / 2;
        int startY = getHeight() / 2 - radius;//y坐标即园中心点的y坐标-半径
        //计算12点处的结束坐标
        int stopX = startX;
        int stopY1 = startY + 30;//整点处的线长度为30
        int stopY2 = startY + 15;//非整点处的线长度为15
        //计算画布每次旋转的角度
        float degree = 360 / (float) 60;
        for(int i = 0; i < 60; i++){
            if(i % 5 == 0)
                canvas.drawLine(startX, startY, stopX, stopY1, mPaint);//绘制整点长的刻度
            else
                canvas.drawLine(startX, startY, stopX, stopY2, mPaint);//绘制非整点处短的刻度
            canvas.rotate(degree, getWidth() / (float) 2, getHeight() / (float) 2);//以圆中心进行旋转
        }
        //绘制完后，记得把画布状态复原
        canvas.restore();
    }

    /**
     * 绘制时钟的圆形部分
     * @param canvas
     */
    private void drawClockCircle(Canvas canvas) {
        //获得圆的圆点坐标
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        canvas.drawCircle(x, y, radius, mPaint);
    }

    private void initPaint() {
        mPaint.reset();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);//设置描边
        mPaint.setStrokeWidth(strokeWidth);//设置描边线的粗细
        mPaint.setAntiAlias(true);//设置抗锯齿，使圆形更加圆滑
    }

    private void runClock() {
        isFirstRunning = true;
        mHandler.postDelayed(clockRunnable, 1000);
    }


}

