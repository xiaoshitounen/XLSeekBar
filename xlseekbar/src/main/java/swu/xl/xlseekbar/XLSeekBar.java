package swu.xl.xlseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class XLSeekBar extends View {
    //日志TAG
    private static final String TAG = "MySeekBar";

    //绘制背景的画笔
    private Paint paint_bg;
    //绘制进度的画笔
    private Paint paint_progress;
    //绘制圆点的画笔
    private Paint paint_thumb;

    //线条的粗细
    private int lineSize = 10;
    //背景线条的颜色
    private int lineColorBg = Color.BLACK;
    //进度线条以及圆点的颜色
    private int lineColorProgress = Color.MAGENTA;

    //控件样式：进度条 || 滑动条
    public static final int PROGRESS = 0;
    public static final int SLIDE = 1;
    private int lineStyle = SLIDE;

    //圆点的圆心坐标以及半径
    private int cx = 0;
    private int cy = 0;
    private int radius = 10;
    //圆点缩放的倍数
    private int thumbScale = 4;

    //记录触摸的位置 用于绘制进度
    private int position;
    //当前进度值
    private int currentProgress = 50;
    //总的进度值
    private int maxProgress = 100;

    //监听对象
    private OnProgressChangeListener listener;

    /**
     * 构造方法 Java代码创建的时候进入
     * @param context
     */
    public XLSeekBar(Context context, int lineSize, int lineStyle, int lineColorBg, int lineColorProgress, int currentProgress ,int maxProgress) {
        super(context);

        this.lineSize = lineSize;
        this.lineStyle = lineStyle;
        this.lineColorBg = lineColorBg;
        this.lineColorProgress = lineColorProgress;
        this.currentProgress = currentProgress;
        this.maxProgress = maxProgress;

        //初始化操作
        init();
    }

    /**
     * 构造方法 Xml代码创建的时候进入
     * @param context
     * @param attrs
     */
    public XLSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //解析属性
        if (attrs != null){
            //1.获取到所有的属性值集合
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MySeekBar);

            //2.解析单个属性
            lineSize = typedArray.getInteger(R.styleable.MySeekBar_lineSize,lineSize);

            lineColorBg = typedArray.getColor(
                    R.styleable.MySeekBar_lineColorBg,
                    lineColorBg
            );

            lineColorProgress = typedArray.getColor(
                    R.styleable.MySeekBar_lineColorProgress,
                    lineColorProgress
            );

            lineStyle = typedArray.getInt(R.styleable.MySeekBar_lineStyle, lineStyle);

            currentProgress = typedArray.getInteger(
                    R.styleable.MySeekBar_currentProgress,
                    currentProgress
            );
            maxProgress = typedArray.getInteger(
                    R.styleable.MySeekBar_maxProgress,
                    maxProgress
            );

            //3.释放资源
            typedArray.recycle();
        }

        //初始化操作
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        //绘制背景的画笔
        paint_bg = new Paint();
        paint_bg.setStrokeWidth(lineSize);
        paint_bg.setColor(lineColorBg);

        //绘制进度的画笔
        paint_progress = new Paint();
        paint_progress.setStrokeWidth(lineSize);
        paint_progress.setColor(lineColorProgress);

        //绘制原点的画笔
        paint_thumb = new Paint();
        paint_thumb.setStyle(Paint.Style.FILL);
        paint_thumb.setColor(lineColorProgress);

        //确定初始进度
        setCurrentProgress(currentProgress);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() > getHeight()){
            //横着摆放

            //绘制背景线条
            canvas.drawLine(
                    0, getHeight() >> 1,
                    getWidth(), getHeight() >> 1,
                    paint_bg
            );

            //绘制进度
            if (position > 0){
                canvas.drawLine(
                        0, getHeight() >> 1,
                        position, getHeight() >> 1,
                        paint_progress
                );
            }

            //SLIDE样式 绘制原点
            if (lineStyle == SLIDE) {
                radius = getHeight() / thumbScale;
                cx = radius;
                cy = getHeight() / 2;

                //当触摸点离两边的间距小于半径时，为了避免超过界限，将离边界的距离都设置为半径
                if (position < radius) cx = radius;
                else cx = Math.min(position, getWidth() - radius);

                //绘制圆点
                canvas.drawCircle(cx,cy,radius,paint_thumb);
            }

        }else {
            //竖着摆放

            //绘制背景线条
            canvas.drawLine(
                    getWidth() >> 1, 0,
                    getWidth() >> 1, getHeight(),
                    paint_bg
            );

            //绘制进度
            if (position < getHeight()){
                canvas.drawLine(
                        getWidth() >> 1, getHeight(),
                        getWidth() >> 1, position,
                        paint_progress
                );
            }

            //SLIDE样式 绘制圆点
            if (lineStyle == SLIDE) {
                radius = getWidth() / thumbScale;
                cx = getWidth() / 2;
                cy = getHeight() - radius;

                //当触摸点离两边的间距小于半径时，为了避免超过界限，将离边界的距离都设置为半径
                if (position < radius) cy = radius;
                else if (position < getHeight() - radius){
                    cy = position;
                }else {
                    cy = getHeight()-radius;
                }

                //绘制圆点
                canvas.drawCircle(cx,cy,radius,paint_thumb);
            }
        }

    }

    //大小切换
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //重新绘制进度
        if (getWidth() > getHeight()){
            position = currentProgress * getWidth() / maxProgress;
        }else {
            position = getHeight() - (currentProgress * getHeight() / maxProgress);
        }
    }

    //触摸方法
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //滑动条才需要触摸事件
        if (lineStyle == SLIDE){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //小圆点放大
                    thumbScale /= 2;

                    //改变进度以及圆点的位置
                    if (getWidth() > getHeight()){
                        //横向
                        position = (int) event.getX();
                    }else {
                        //竖向
                        position = (int) event.getY();
                    }

                    //回调进度
                    callBack();

                    break;
                case MotionEvent.ACTION_MOVE:
                    //改变进度以及圆点的位置
                    if (getWidth() > getHeight()){
                        //横向
                        position = (int) event.getX();
                    }else {
                        //竖向
                        position = (int) event.getY();
                    }

                    //回调进度
                    callBack();

                    break;
                case MotionEvent.ACTION_UP:
                    //小圆点恢复原状
                    thumbScale *= 2;

                    break;
            }

            //刷新
            invalidate();
        }

        return true;
    }

    //回调方法
    private void callBack(){
        if (listener != null){

            //确定进度
            if (getWidth() > getHeight()){
                //横着摆放
                currentProgress = position * maxProgress / getWidth();
            }else {
                //竖着摆放
                currentProgress = (getHeight() - position) * maxProgress / getHeight();
            }

            //确保进度合法
            if (currentProgress <= 0) currentProgress = 0;
            if (currentProgress >= maxProgress) currentProgress = maxProgress;

            //回调进度
            listener.progressChanged(currentProgress);
        }
    }

    /**
     * 监听接口
     * @return
     */
    public interface OnProgressChangeListener{
        //回调当前进度
        void progressChanged(float progress);
    }

    //setter、getter方法
    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    public int getLineColorBg() {
        return lineColorBg;
    }

    public void setLineColorBg(int lineColorBg) {
        this.lineColorBg = lineColorBg;
    }

    public int getLineColorProgress() {
        return lineColorProgress;
    }

    public void setLineColorProgress(int lineColorProgress) {
        this.lineColorProgress = lineColorProgress;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getThumbScale() {
        return thumbScale;
    }

    public void setThumbScale(int thumbScale) {
        this.thumbScale = thumbScale;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(final int currentProgress) {
        this.currentProgress = currentProgress;

        //设置初始进度的时候可能宽和高没有测量出来
        this.post(new Runnable() {
            @Override
            public void run() {
                //测量出来的宽高
                int width = getWidth();
                int height = getHeight();
                Log.d(TAG,"Width:"+getWidth());
                Log.d(TAG,"Height:"+getHeight());

                //根据进度重绘
                Log.d(TAG,"setCurrentProgress:"+currentProgress);
                if (width > height){
                    //横着
                    position = currentProgress * getWidth() / maxProgress;
                } else {
                    //竖着
                    position = getHeight() - (currentProgress * getHeight() / maxProgress);
                }

                //刷新
                invalidate();
                Log.d(TAG,"position:"+position);
            }
        });
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgressChangeListener(OnProgressChangeListener listener) {
        this.listener = listener;
    }
}
