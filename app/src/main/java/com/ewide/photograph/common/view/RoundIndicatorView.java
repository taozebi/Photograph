package com.ewide.photograph.common.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.ewide.photograph.R;

/**
 * 仿支付宝芝麻信用圆形仪表盘
 * Created by Taoze on 2018/05/17.
 */

public class RoundIndicatorView extends View {

    private Paint paint;
    private Paint paint_2;
    private Paint paint_3;
    private Paint paint_4;
    private Paint paint_5;
    private Context context;
    private int maxNum;
    private String topText;
    private String unit;
    private String desc;
    private int startAngle;
    private int sweepAngle;
    private int radius;
    private int mWidth;
    private int mHeight;
    private int sweepInWidth;//内圆的宽度
    private int sweepOutWidth;//外圆的宽度
    private int currentNum=0;//需设置setter、getter 供属性动画使用
    private String[] labels ={"较差","中等","良好","优秀","极好"};
    private int[] indicatorColor = {0xffffffff,0x00ffffff,0x99ffffff,0xffffffff};

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
        invalidate();
    }

    public RoundIndicatorView(Context context) {
        this(context,null);
    }

    public RoundIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundIndicatorView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setBackgroundColor(0xFFFF6347);
        initAttr(attrs);
        initPaint();
    }

    public void setCurrentNumAnim(int num) {
        if(num != currentNum){
            float duration = (float)Math.abs(num-currentNum)/maxNum *1500+500; //根据进度差计算动画时间
            ObjectAnimator anim = ObjectAnimator.ofInt(this,"currentNum",num);
            anim.setDuration((long) Math.min(duration,2000));
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    int color = calculateColor(value);
                    setBackgroundColor(color);
                }
            });
            anim.start();
        }
        setCurrentNum(num);
    }
    private int calculateColor(int value){
        ArgbEvaluator evealuator = new ArgbEvaluator();
        float fraction = 0;
        int color = 0;
        if(value <= maxNum/2){
            fraction = (float)value/(maxNum/2);
//            color = (int) evealuator.evaluate(fraction,0xFFFF6347,0xFFFF8C00); //由红到橙
            color = (int) evealuator.evaluate(fraction,0xFFFF6347,0xFF00CED1); //由红到蓝
        }else {
            fraction = ( (float)value-maxNum/2 ) / (maxNum/2);
//            color = (int) evealuator.evaluate(fraction,0xFFFF8C00,0xFF00CED1); //由橙到蓝
            color = (int) evealuator.evaluate(fraction,0xFF00CED1,0xFFFF6347); //蓝到红
        }
        return color;
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffffffff);
        paint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_5  = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.RoundIndicatorView);
        maxNum = array.getInt(R.styleable.RoundIndicatorView_maxNum,500);
//        startAngle = array.getInt(R.styleable.RoundIndicatorView_startAngle,160);
//        sweepAngle = array.getInt(R.styleable.RoundIndicatorView_sweepAngle,220);
        startAngle = array.getInt(R.styleable.RoundIndicatorView_startAngle,170);
        sweepAngle = array.getInt(R.styleable.RoundIndicatorView_sweepAngle,200);
        //内外圆的宽度
        sweepInWidth = dp2px(5);
        sweepOutWidth = dp2px(3);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY ){
            mWidth = wSize;
        }else {
            mWidth =dp2px(300);
        }
        if (hMode == MeasureSpec.EXACTLY ){
            mHeight= hSize;
        }else {
            mHeight =dp2px(500);
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = getMeasuredWidth()/3; //不要在构造方法里初始化，那时还没测量宽高
        canvas.save();
        canvas.translate(mWidth/2,(mWidth)/2);
        drawTopText(canvas); //画上面的文字
        drawRound(canvas);  //画内外圆
        drawScale(canvas);//画刻度
        drawIndicator(canvas); //画当前进度值
        drawCenterText(canvas);//画中间的文字
        canvas.restore();
    }

    private void drawTopText(Canvas canvas){
        canvas.save();
        paint_5.setStyle(Paint.Style.FILL);
        paint_5.setTextSize(radius/5);
        paint_5.setColor(0xffffffff);
        Rect r = new Rect();
        paint_5.getTextBounds(getTopText(),0,getTopText().length(),r);
        canvas.drawText(getTopText(),-r.width()/2,-radius-r.height(),paint_5);
        canvas.restore();
    }

    private void drawCenterText(Canvas canvas) {
        canvas.save();
        paint_4.setStyle(Paint.Style.FILL);
        paint_4.setTextSize(radius/4);
        paint_4.setColor(0xffffffff);
        canvas.drawText(currentNum+getUnit(),-paint_4.measureText(currentNum+getUnit())/2,0,paint_4);
        paint_4.setTextSize(radius/6);
        String content = getDesc();
        if(currentNum < maxNum*1/5){
            content += getLabels()[0];
        }else if(currentNum >= maxNum*1/5 && currentNum < maxNum*2/5){
            content += getLabels()[1];
        }else if(currentNum >= maxNum*2/5 && currentNum < maxNum*3/5){
            content += getLabels()[2];
        }else if(currentNum >= maxNum*3/5 && currentNum < maxNum*4/5){
            content += getLabels()[3];
        }else if(currentNum >= maxNum*4/5){
            content += getLabels()[4];
        }
        Rect r = new Rect();
        paint_4.getTextBounds(content,0,content.length(),r);
        canvas.drawText(content,-r.width()/2,r.height()+20,paint_4);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        paint_2.setStyle(Paint.Style.STROKE);
        int sweep;
        if(currentNum<=maxNum){
            sweep = (int)((float)currentNum/(float)maxNum*sweepAngle);
        }else {
            sweep = sweepAngle;
        }
        paint_2.setStrokeWidth(sweepOutWidth);
        Shader shader =new SweepGradient(0,0,indicatorColor,null);
        paint_2.setShader(shader);
        int w = dp2px(10);
        RectF rectf = new RectF(-radius-w , -radius-w , radius+w , radius+w);
        if(sweep > 0){
            canvas.drawArc(rectf,startAngle,sweep,false,paint_2);
        }
        float x = (float) ((radius+dp2px(10))*Math.cos(Math.toRadians(startAngle+sweep)));
        float y = (float) ((radius+dp2px(10))*Math.sin(Math.toRadians(startAngle+sweep)));
        paint_3.setStyle(Paint.Style.FILL);
        paint_3.setColor(0xffffffff);
        paint_3.setMaskFilter(new BlurMaskFilter(dp2px(3), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
        canvas.drawCircle(x,y,dp2px(3),paint_3);
        canvas.restore();
    }

    private void drawScale(Canvas canvas) {
        canvas.save();
        float angle = (float)sweepAngle/30;//刻度间隔
        canvas.rotate(-270+startAngle); //将起始刻度点旋转到正上方（270)
        for (int i = 0; i <= 30; i++) {
            if(i%6 == 0){   //画粗刻度和刻度值
                paint.setStrokeWidth(dp2px(2));
                paint.setAlpha(0x70);
                canvas.drawLine(0, -radius-sweepInWidth/2,0, -radius+sweepInWidth/2+dp2px(1), paint);
                drawText(canvas,i*maxNum/30+"",paint);
            }else {         //画细刻度
                paint.setStrokeWidth(dp2px(1));
                paint.setAlpha(0x50);
                canvas.drawLine(0,-radius-sweepInWidth/2,0, -radius+sweepInWidth/2, paint);
            }
            if(i==3 || i==9 || i==15 || i==21 || i==27){  //画刻度区间文字
                paint.setStrokeWidth(dp2px(2));
                paint.setAlpha(0x50);
                drawText(canvas,getLabels()[(i-3)/6], paint);
            }
            canvas.rotate(angle); //逆时针
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas ,String text ,Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(sp2px(16));
//        paint.setColor(0xffffffff);
        float width = paint.measureText(text); //相比getTextBounds来说，这个方法获得的类型是float，更精确些
//        Rect rect = new Rect();
//        paint.getTextBounds(text,0,text.length(),rect);
        canvas.drawText(text,-width/2 , -radius + dp2px(24),paint);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        //内圆
        paint.setAlpha(0x40);
        paint.setStrokeWidth(sweepInWidth);
        RectF rectf = new RectF(-radius,-radius,radius,radius);
        canvas.drawArc(rectf,startAngle,sweepAngle,false,paint);
        //外圆
        paint.setStrokeWidth(sweepOutWidth);
        int w = dp2px(10);
        RectF rectf2 = new RectF(-radius-w , -radius-w , radius+w , radius+w);
        canvas.drawArc(rectf2,startAngle,sweepAngle,false,paint);
        canvas.restore();
    }




    //一些工具方法
    protected int dp2px(int dp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }
    protected int sp2px(int sp){
        return (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics());
    }
    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}