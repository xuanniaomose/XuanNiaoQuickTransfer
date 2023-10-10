package xuanniao.transmission.trclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LineProgressBar extends View {
    private Paint mBackPaint;
    private final int mColor = getResources().getColor(R.color.blue_700);
    private Paint mPaint;
    private final float mStrokeWidth = 25;
    private int mProgress = 25;
    private int mMax = 100;
    private int mWidth;

    public LineProgressBar(Context context) {
        super(context);
        init();
    }

    public LineProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setParameter(int progress, int max) {
        this.mProgress = progress;
        this.mMax = max;
    }

    public void notifyParameter(int progress) {
        this.mProgress = progress;
    }

    //完成相关参数初始化
    private void init() {
        // 进度未被填充的部分
        mBackPaint = new Paint();
        mBackPaint.setColor(getResources().getColor(R.color.gray_100));
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeWidth(mStrokeWidth);

        // 进度的部分
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    //重写测量大小的onMeasure方法和绘制View的核心方法onDraw()
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = (int) mStrokeWidth / 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float length = (mProgress / (float) mMax) * mWidth;
        canvas.drawLine(0,0,mWidth,0,mBackPaint);
        canvas.drawLine(0,0,length,0,mPaint);
    }
}