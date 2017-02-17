package com.android.zza.sudokulock.newpakce;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
 * Created by xalo on 17/1/4.
 */

public class SudokuLockView extends View{
    private final String TAG = "sudokuTest";
    private DisplayMetrics metrics;
    private boolean inited = false;//标识，是否初始化过
    MyPoint[][] myPoint = new MyPoint[3][3];//三行三列
    private Bitmap nBitmap;
    private Bitmap sBitmap;
    private Bitmap eBitmap;
    private float bitR;
    private boolean isDrawing = true;
    ArrayList<MyPoint> allPoint = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();

    Paint paint = new Paint();
    Paint epaint = new Paint();

    private float moveX;
    private float moveY;
    int x;
    int y;
    int[] getSelect;

    public SudokuLockView(Context context) {
        super(context);
    }

    public SudokuLockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#2211ff"));
        paint.setStrokeWidth(6);

        epaint.setDither(true);
        epaint.setAntiAlias(true);
        epaint.setColor(Color.RED);
        epaint.setStrokeWidth(6);

        if (!inited){
            initView();
        }
        drawPoint(canvas);

        drawPointLines(canvas);
    }

    /**
     * 确定点，划线
     * @param canvas
     */
    private void drawPointLines(Canvas canvas) {
        if (allPoint.size() > 0){
            MyPoint A = allPoint.get(0);
            for (int a = 1;a<allPoint.size();a++){
                MyPoint B = allPoint.get(a);
                drawLines(canvas,A,B);
                A = B;
            }
            if (isDrawing){
                drawLines(canvas,A,new MyPoint(moveX,moveY));
            }
        }
    }

    /**
     * 通过两点绘制线
     * @param canvas
     * @param a
     * @param b
     */
    private void drawLines(Canvas canvas, MyPoint a, MyPoint b) {


        if (a.start == MyPoint.ERROR){

            canvas.drawLine(a.x,a.y,b.x,b.y,epaint);
        }else {

            canvas.drawLine(a.x,a.y,b.x,b.y,paint);
        }
    }

    /**
     * 绘制点
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {


        for (int i = 0;i<myPoint.length;i++){
            for (int j = 0;j<myPoint[i].length;j++){

                if (myPoint[i][j].start == MyPoint.SELECT){

                    canvas.drawBitmap(sBitmap,myPoint[i][j].x-bitR,myPoint[i][j].y-bitR,paint);
                }else if (myPoint[i][j].start == MyPoint.ERROR){

                    canvas.drawBitmap(eBitmap,myPoint[i][j].x-bitR,myPoint[i][j].y-bitR,paint);
                }else {

                    canvas.drawBitmap(nBitmap,myPoint[i][j].x-bitR,myPoint[i][j].y-bitR,paint);
                }
            }
        }
    }



    public void clearAll(){


        for (int a=0;a<myPoint.length;a++){
            for (int b = 0;b<myPoint[a].length;b++){
                myPoint[a][b].start = MyPoint.NOMAL;
            }
        }

        list.removeAll(list);
        isDrawing = true;
        allPoint.clear();
        this.postInvalidate();
    }



    public void okAll(){
        String m = "";
        for (int a=0;a<list.size();a++){

            m += list.get(a).toString();
        }
        Toast.makeText(getContext(),m,Toast.LENGTH_SHORT).show();

        for (int a=0;a<myPoint.length;a++){
            for (int b = 0;b<myPoint[a].length;b++){
                myPoint[a][b].start = MyPoint.NOMAL;
            }
        }

        allPoint.clear();
        this.postInvalidate();
    }


    /**
     * init point
     */
    private void initView() {


        /**添加点的图片资源**/
        nBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.onetests);
        sBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.twotests);
        eBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.threetests);

        bitR = nBitmap.getHeight()/2;
        /***
         获取屏幕宽高
         通过屏幕宽高判断屏幕方向
         为了让九宫处于屏幕中间，根据屏幕设置不同的偏移量
         设置每个点的位置
         ***/
        int pHigth = metrics.heightPixels;
        int pWidth = metrics.widthPixels;

        float offsetX;
        float offsetY;
        float space;

        if (pHigth > pWidth){

            offsetX = 0;
            offsetY = (pHigth - pWidth)/2;
            space = pWidth/4;
        }else {

            offsetX = (pWidth - pHigth)/2;
            offsetY = 0;
            space = pHigth/4;
        }


        /**one lines**/
        myPoint[0][0] = new MyPoint(offsetX+space,offsetY+space);
        myPoint[0][1] = new MyPoint(offsetX+space*2,offsetY+space);
        myPoint[0][2] = new MyPoint(offsetX+space*3,offsetY+space);

        /**two lines**/
        myPoint[1][0] = new MyPoint(offsetX+space,offsetY+space*2);
        myPoint[1][1] = new MyPoint(offsetX+space*2,offsetY+space*2);
        myPoint[1][2] = new MyPoint(offsetX+space*3,offsetY+space*2);

        /**three lines**/
        myPoint[2][0] = new MyPoint(offsetX+space,offsetY+space*3);
        myPoint[2][1] = new MyPoint(offsetX+space*2,offsetY+space*3);
        myPoint[2][2] = new MyPoint(offsetX+space*3,offsetY+space*3);

        inited = true;
    }

    /**
     * 点击操作事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        moveX = event.getX();
        moveY = event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                if (isDrawing){

                    getSelect = getSelectPoint();
                    if (getSelect != null){

                        x = getSelect[0];
                        y = getSelect[1];
                        myPoint[x][y].start = MyPoint.SELECT;
                        allPoint.add(myPoint[x][y]);
                        setPassWord(x,y);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (isDrawing){

                    getSelect = getSelectPoint();
                    if (getSelect != null){

                        x = getSelect[0];
                        y = getSelect[1];
                        if (!allPoint.contains(myPoint[x][y])){

                            myPoint[x][y].start = MyPoint.SELECT;
                            allPoint.add(myPoint[x][y]);
                            setPassWord(x,y);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                if (isDrawing){

                    if (allPoint.size() <= 3 && allPoint.size()>0){
                        Toast.makeText(getContext(),"至少不少于四点",Toast.LENGTH_SHORT).show();

                        for (int i = 0;i<allPoint.size();i++){

                            allPoint.get(i).start = MyPoint.ERROR;
                        }
                    }
                }

                if (allPoint.size() > 0){

                    isDrawing = false;
                }
//                Log.d("test",allPoint.size()+"");
                break;
        }
        this.postInvalidate();
        return true;
    }

    /**
     * 设置选中的点的值
     * @param x
     * @param y
     */
    private void setPassWord(int x, int y) {

        if (myPoint[x][y] == myPoint[0][0]){
            list.add("1");
        }else if (myPoint[x][y] == myPoint[0][1]){
            list.add("2");
        }else if (myPoint[x][y] == myPoint[0][2]){
            list.add("3");
        }else if (myPoint[x][y] == myPoint[1][0]){
            list.add("4");
        }else if (myPoint[x][y] == myPoint[1][1]){
            list.add("5");
        }else if (myPoint[x][y] == myPoint[1][2]){
            list.add("6");
        }else if (myPoint[x][y] == myPoint[2][0]){
            list.add("7");
        }else if (myPoint[x][y] == myPoint[2][1]){
            list.add("8");
        }else if (myPoint[x][y] == myPoint[2][2]){
            list.add("9");
        }
    }


    /**
     * 返回是否选择的是点
     */
    private int[] getSelectPoint() {

        MyPoint begainSelectPoint = new MyPoint(moveX,moveY);
        for (int i = 0;i<myPoint.length;i++){
            for (int j = 0;j<myPoint[i].length;j++){

                if (myPoint[i][j].distance(begainSelectPoint) < bitR){

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
