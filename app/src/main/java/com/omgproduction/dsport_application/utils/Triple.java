package com.omgproduction.dsport_application.utils;

/**
 * Created by Florian on 20.03.2017.
 */

public class Triple<A,B,C>  {
    private A _1;
    private B _2;
    private C _3;

    public Triple(A _1, B _2, C _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    public A get_1() {
        return _1;
    }

    public void set_1(A _1) {
        this._1 = _1;
    }

    public B get_2() {
        return _2;
    }

    public void set_2(B _2) {
        this._2 = _2;
    }

    public C get_3() {
        return _3;
    }

    public void set_3(C _3) {
        this._3 = _3;
    }
}
