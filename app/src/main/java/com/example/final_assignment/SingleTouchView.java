package com.example.final_assignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SingleTouchView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private int paintColor = 0xFF000000;
    private Paint canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;


    int pen_width; //MainActivity.java에서 width값 불러오기
    int basecolor = 5; //바탕색에 따라 새로만들기, 지우개 색 달라지므로 변수 설정, 처음 실행했을때 흰색 설

   public SingleTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(setSize(pen_width));
        paint.setColor(paintColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor) {
        //    invalidate();
        paintColor = Color.parseColor(newColor);
        paint.setColor(paintColor);
    }

    public void setFunc(int num) {
        //새로 만들기 버튼 눌렀을 때
        if (num == 0) {
            if(basecolor == 1){
                canvasBitmap.eraseColor(Color.RED);
                invalidate();
            }
            else if(basecolor == 2){
                canvasBitmap.eraseColor(Color.GREEN);
                invalidate();
            }
            else if(basecolor == 3){
                canvasBitmap.eraseColor(Color.BLUE);
                invalidate();
            }
            else if(basecolor == 4){
                canvasBitmap.eraseColor(Color.BLACK);
                invalidate();
            }
            else if(basecolor == 5){
                canvasBitmap.eraseColor(Color.WHITE);
                invalidate();
            }
        }
        else if (num == 1) {
            paint.setStrokeWidth(10f);
        }

        //지우개 버튼 눌렀을 때
        else if (num == 2) {
            if(basecolor == 1){
                paint.setColor(Color.RED);
            }
            else if(basecolor == 2){
                paint.setColor(Color.GREEN);
            }
            else if(basecolor == 3){
                paint.setColor(Color.BLUE);
            }
            else if(basecolor == 4){
                paint.setColor(Color.BLACK);
            }
            else if(basecolor == 5){
                paint.setColor(Color.WHITE);
            }
            paint.setStrokeWidth(setSize(pen_width));
            paint.setStrokeCap(Paint.Cap.SQUARE);
        }

        else{
        }
    }

    //브러쉬, 지우개 사이즈 받기
    public int setSize(int num) {
        paint.setStrokeWidth(num);
        pen_width = num;
        return pen_width;
    }

    public void setBackgroundColor(int num){
        if(num == 1){
            basecolor = 1;
            drawCanvas.drawColor(Color.RED);
        }
        else if(num == 2){
            basecolor = 2;
            drawCanvas.drawColor(Color.GREEN);
        }
        else if(num == 3){
            basecolor = 3;
            drawCanvas.drawColor(Color.BLUE);
        }
        else if(num == 4){
            basecolor = 4;
            drawCanvas.drawColor(Color.BLACK);
        }
        else if(num == 5){
            basecolor = 5;
            drawCanvas.drawColor(Color.WHITE);
        }

    }
}