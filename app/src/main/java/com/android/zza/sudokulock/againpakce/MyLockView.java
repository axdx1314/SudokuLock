package com.android.zza.sudokulock.againpakce;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.zza.sudokulock.R;

import java.util.ArrayList;

/**
 * @author zza
 * Created by xalo on 17/1/5.
 */

public class MyLockView extends View{
    private boolean inited = false;
    private DisplayMetrics metrics;
    private NewPoint[][] newPoint = new NewPoint[3][3];
    Bitmap nBitmap;
    float bitR;
    boolean isdrawing = true;

    public MyLockView(Context context) {
        super(context);
    }

    public MyLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!inited){
            initView();
        }

        drawPoint(canvas);

    }

    /**
     * 画点
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {


        Paint paint = new Paint();

        for (int a=0;a<newPoint.length;a++){
            for (int b=0;b<newPoint[a].length;b++){

                canvas.drawBitmap(nBitmap,newPoint[a][b].x - bitR,newPoint[a][b].y - bitR,paint);
            }
        }
    }

    /**
     * 初始化点
     */
    private void initView() {

        nBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        bitR = nBitmap.getHeight()/2;

        int pHeight = metrics.heightPixels;
        int pWidth = metrics.widthPixels;


        float offsetX;
        float offsetY;
        float space;

        if (pHeight > pWidth){

            offsetX = 0;
            offsetY = (pHeight - pWidth)/2;
            space = pWidth/4;
        }else {

            offsetX = (pWidth - pHeight)/2;
            offsetY = 0;
            space = pHeight/4;
        }


        for (int a = 0;a<newPoint.length;a++){
            for (int b = 0;b<newPoint[a].length;b++){

                newPoint[a][b] = new NewPoint(offsetX+space*(a+1),offsetY+space*(b+1));
            }
        }

        inited = true;
    }


    float moveX;
    float moveY;
    int x;
    int y;
    int[] results;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moveX = event.getX();
        moveY = event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                if (isdrawing){

                    results = getSlectPoint();
                    if (results != null){
                        x = results[0];
                        y = results[1];
                        allPoint.add(newPoint[x][y]);
                    }

                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (isdrawing){

                    results = getSlectPoint();
                    if (results != null){

                        x = results[0];
                        y = results[1];
                        if (!allPoint.contains(newPoint[x][y])){
                            allPoint.add(newPoint[x][y]);
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_UP:

                if (isdrawing){
                    if (allPoint.size() <= 3&&allPoint.size()>0){
                        Toast.makeText(getContext(),"至少不少于四点",Toast.LENGTH_SHORT).show();
                    }
                }
                if (allPoint.size() > 0){
                    isdrawing = false;
                }
                break;
        }

        return true;
    }

    ArrayList<NewPoint> allPoint = new ArrayList<>();
    /**
     * 返回所选择的X，Y
     */
    private int[] getSlectPoint() {

        NewPoint selectPoint = new NewPoint(moveX,moveY);
        for (int a=1;a<newPoint.length;a++){
            for (int b=0;b<newPoint[a].length;b++){

                if (newPoint[a][b].distace(selectPoint) < bitR){

                    int[] result = new int[2];
                    result[0] = a;
                    result[1] = b;
                    return result;
                }
            }
        }
        return null;
    }
}
