package com.android.zza.sudokulock.thislockpointdemo;

/**
 * @author zza
 * Created by xalo on 17/1/6.
 */

public class MYNewPoint {

    public static int NOMAL = 0;
    public static int SELECT = 1;
    public static int ERROR = 2;

    float x;
    float y;

    int start = NOMAL;
    public MYNewPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float desitance(MYNewPoint point){

        return (float)Math.sqrt((x-point.x)*(x-point.x)+(y-point.y)*(y-point.y));
    }
}
