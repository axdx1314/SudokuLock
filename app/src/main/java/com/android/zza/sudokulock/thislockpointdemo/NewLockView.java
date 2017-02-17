package com.android.zza.sudokulock.thislockpointdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.zza.sudokulock.R;
import com.android.zza.sudokulock.newpakce.MyPoint;

import java.util.ArrayList;

/**
 * @author zza
 * Created by xalo on 17/1/6.
 */

public class NewLockView extends View{
    private boolean inited = true;
    private DisplayMetrics metrics;
    private MYNewPoint[][] myNewPoints = new MYNewPoint[3][3];
    private Bitmap nBitmap;
    private Bitmap sBitmap;
    private Bitmap eBitmap;
    private float bitR;
    private boolean isDrawing = true;
    private ArrayList<MYNewPoint> allPoint = new ArrayList<>();

    public NewLockView(Context context) {
        super(context);
    }

    public NewLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager manager =
                (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (inited){

            initView();
        }

        drawPoint(canvas);
    }


    private void drawPoint(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6);

        Paint epaint = new Paint();
        epaint.setColor(Color.RED);
        epaint.setDither(true);
        epaint.setAntiAlias(true);
        epaint.setStrokeWidth(6);

        for (int a = 0;a<myNewPoints.length;a++){
            for (int b = 0;b<myNewPoints[a].length;b++){

                if (myNewPoints[a][b].start == MYNewPoint.SELECT){

                    canvas.drawBitmap(sBitmap,myNewPoints[a][b].x - bitR,myNewPoints[a][b].y - bitR,paint);
                }else if (myNewPoints[a][b].start == MYNewPoint.ERROR){

                    canvas.drawBitmap(eBitmap,myNewPoints[a][b].x - bitR,myNewPoints[a][b].y - bitR,epaint);
                }else {

                    canvas.drawBitmap(nBitmap,myNewPoints[a][b].x - bitR,myNewPoints[a][b].y - bitR,paint);
                }
            }
        }

    }

    private void initView() {

        nBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.onetests);
        sBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.twotests);
        eBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.threetests);
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

        for (int a = 0;a<myNewPoints.length;a++){
            for (int b = 0;b<myNewPoints[a].length;b++){
                myNewPoints[a][b] = new MYNewPoint(offsetX+space*(a+1),offsetY+space*(b+1));
            }
        }

        inited = true;
    }


    float moveX;
    float moveY;
    int[] resultes;
    int x;
    int y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        moveX = event.getX();
        moveY = event.getY();
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                if (isDrawing){

                    resultes = getSelectPoint();
                    if (resultes != null){

                        x = resultes[0];
                        y = resultes[1];
                        myNewPoints[x][y].start = MyPoint.SELECT;
                        allPoint.add(myNewPoints[x][y]);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (isDrawing){

                    resultes = getSelectPoint();
                    if (resultes != null){

                        x = resultes[0];
                        y = resultes[1];
                        if (!allPoint.contains(myNewPoints[x][y])){

                            myNewPoints[x][y].start = MyPoint.SELECT;
                            allPoint.add(myNewPoints[x][y]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                Log.d("test","抬起");
                if (isDrawing){

                    if (allPoint.size() <= 3 && allPoint.size() >0){
                        Toast.makeText(getContext(),"至少选择四点",Toast.LENGTH_SHORT).show();

                        for (int a=0;a<allPoint.size();a++){

                            allPoint.get(a).start = MYNewPoint.ERROR;
                        }
                    }
                }

                if (allPoint.size() > 0){
                    isDrawing = false;
                }
                Log.d("test",allPoint.size()+"");
                break;

        }
        this.postInvalidate();
        return true;
    }

    /**
     * 选择的是否是九点中的任一点
     */
    private int[] getSelectPoint() {

        MYNewPoint begainSelectPoint = new MYNewPoint(moveX,moveY);
        for (int i = 0;i<myNewPoints.length;i++){
            for (int j = 0;j<myNewPoints[i].length;j++){

                if (myNewPoints[i][j].desitance(begainSelectPoint) < bitR){

                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return null;
    }
}
