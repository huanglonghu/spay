package com.example.godcode.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.godcode.R;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class PayPsdInputView extends RelativeLayout implements KeyBoard.RefreshPsd{

    private Context mContext;
    /**
     * 第一个圆开始绘制的圆心坐标
     */
    private float startX;
    private float startY;


    private float cX;


    /**
     * 实心圆的半径
     */
    private int radius = 20;
    /**
     * view的高度
     */
    private int height;
    private int width;

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    /**
     * 当前输入密码位数
     */
    private int textLength;
    private int bottomLineLength;
    /**
     * 最大输入位数
     */
    private int maxCount = 6;
    /**
     * 圆的颜色   默认BLACK
     */
    private int circleColor = Color.BLACK;
    /**
     * 底部线的颜色   默认GRAY
     */
    private int bottomLineColor = Color.parseColor("#bebebe");

    /**
     * 分割线的颜色
     */
    private int borderColor = Color.parseColor("#eeeeee");
    /**
     * 分割线的画笔
     */
    private Paint borderPaint;
    /**
     * 分割线开始的坐标x
     */
    private int divideLineWStartX;

    /**
     * 分割线的宽度  默认2
     */
    private int divideLineWidth = 2;
    /**
     * 竖直分割线的颜色
     */
    private int divideLineColor = Color.GRAY;
    private int focusedColor = Color.BLUE;
    private RectF rectF = new RectF();
    private int psdType = 0;


    /**
     * 矩形边框的圆角
     */
    private int rectAngle = 0;
    /**
     * 竖直分割线的画笔
     */
    private Paint divideLinePaint;
    /**
     * 圆的画笔
     */
    private Paint circlePaint;
    /**
     * 底部线的画笔
     */
    private Paint bottomLinePaint;

    /**
     * 需要对比的密码  一般为上次输入的
     */
    private String mComparePassword = null;


    /**
     * 当前输入的位置索引
     */
    private int position = 0;


    public PayPsdInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getAtt(attrs);
        initPaint();
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    private void getAtt(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.PayPsdInputView);
        maxCount = typedArray.getInt(R.styleable.PayPsdInputView_maxCount, maxCount);
        circleColor = typedArray.getColor(R.styleable.PayPsdInputView_circleColor, circleColor);
        bottomLineColor = typedArray.getColor(R.styleable.PayPsdInputView_bottomLineColor, bottomLineColor);
        radius = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_radius, radius);
        divideLineWidth = typedArray.getDimensionPixelSize(R.styleable.PayPsdInputView_divideLineWidth, divideLineWidth);
        divideLineColor = typedArray.getColor(R.styleable.PayPsdInputView_divideLineColor, divideLineColor);
        psdType = typedArray.getInt(R.styleable.PayPsdInputView_psdType, psdType);
        rectAngle = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_rectAngle, rectAngle);
        focusedColor = typedArray.getColor(R.styleable.PayPsdInputView_focusedColor, focusedColor);
        textLength = typedArray.getInt(R.styleable.PayPsdInputView_textLength, 0);
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        circlePaint = getPaint(5, Paint.Style.FILL, circleColor);

        bottomLinePaint = getPaint(2, Paint.Style.FILL, bottomLineColor);

        borderPaint = getPaint(3, Paint.Style.STROKE, borderColor);

        divideLinePaint = getPaint(divideLineWidth, Paint.Style.FILL, borderColor);

    }

    /**
     * 设置画笔
     *
     * @param strokeWidth 画笔宽度
     * @param style       画笔风格
     * @param color       画笔颜色
     * @return
     */
    private Paint getPaint(int strokeWidth, Paint.Style style, int color) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
        setBackgroundColor(Color.parseColor("#ffffff"));
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        divideLineWStartX = w / maxCount;
        startX = w / maxCount / 2;
        startY = h / 2;
        bottomLineLength = w / (maxCount + 2);
        rectF.set(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawWeChatBorder(canvas);
        drawPsdCircle(canvas);
    }

    private void drawWeChatBorder(Canvas canvas) {

        borderPaint.setColor(Color.parseColor("#bebebe"));
        canvas.drawRoundRect(rectF, rectAngle, rectAngle, borderPaint);
        divideLinePaint.setColor(Color.parseColor("#dedede"));
        for (int i = 0; i < maxCount - 1; i++) {
            canvas.drawLine((i + 1) * divideLineWStartX,
                    0,
                    (i + 1) * divideLineWStartX,
                    height,
                    divideLinePaint);
        }
    }

    /**
     * 画底部显示的分割线
     *
     *
     * @param canvas
     */

    private void drawBottomBorder(Canvas canvas) {

        for (int i = 0; i < maxCount; i++) {
            cX = startX + i * 2 * startX;
            canvas.drawLine(cX - bottomLineLength / 2,
                    height,
                    cX + bottomLineLength / 2,
                    height, bottomLinePaint);
        }
    }

    /**
     * 画密码实心圆
     *
     * @param canvas
     */
    private void drawPsdCircle(Canvas canvas) {
        for (int i = 0; i < textLength; i++) {
            canvas.drawCircle(startX + i * 2 * startX,
                    startY,
                    radius,
                    circlePaint);
        }
    }


    @Override
    public void setPsLength(int length) {
        this.textLength = length;
        invalidate();
    }

}