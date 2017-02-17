package com.android.zza.sudokulock.newpakce;

/**
 * @author zza
 * Created by xalo on 17/1/4.
 */

public class MyPoint {


    /**
     * 点绘制后的三种状态
     */
    public static int NOMAL = 1;
    public static int SELECT = 2;
    public static int ERROR = 3;




    float x;
    float y;

    int start = NOMAL;//默认状态
    public MyPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * 两点之间的距离
     */
    public float distance(MyPoint point){

        /**      (x1-x2)^+(y1-y2)^       **/
        float distance = (float) Math.sqrt((x-point.x)*(x-point.x)+(y-point.y)*(y-point.y));
        return distance;
    }

    public static String mA = "a";
    public static String mB = "b";
    public static String mC = "c";
    public static String mD = "d";
    public static String mE = "e";
    public static String mF = "f";
    public static String mG = "g";
    public static String mH = "h";
    public static String mI = "i";

//    public static String mA ;
//    public static String mB ;
//    public static String mC ;
//    public static String mD ;
//    public static String mE ;
//    public static String mF ;
//    public static String mG ;
//    public static String mH ;
//    public static String mI ;

    String pass = "";


}
