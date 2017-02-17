package com.android.zza.sudokulock.againpakce;

/**
 * @author zza
 * Created by xalo on 17/1/5.
 */

public class NewPoint {



    float x;
    float y;

    public NewPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distace(NewPoint point){

        return (float)Math.sqrt((x-point.x)*(x-point.x)+(y-point.y)*(y-point.y));
    }
}
