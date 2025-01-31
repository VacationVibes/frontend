package com.github.sviatoslavslysh.vacationvibes.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Shader;

import androidx.appcompat.widget.AppCompatImageView;

public class HistoryCircularImageView extends AppCompatImageView {
    private Paint borderPaint;
    private Paint imagePaint;
    private final float borderWidth = 8f;

    public HistoryCircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(0xFFE3B8F6);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);

        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bitmap);
        super.onDraw(tempCanvas);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        imagePaint.setShader(shader);
        float radius = getResources().getDisplayMetrics().density * 16;
        RectF rect = new RectF(borderWidth, borderWidth, width - borderWidth, height - borderWidth);
        canvas.drawRoundRect(rect, radius, radius, imagePaint);
        canvas.drawRoundRect(rect, radius, radius, borderPaint);
    }
}
