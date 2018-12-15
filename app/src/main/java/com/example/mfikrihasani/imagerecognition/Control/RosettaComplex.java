package com.example.mfikrihasani.imagerecognition.Control;

public class RosettaComplex {
    public final double re;
    public final double im;

    public RosettaComplex() {
        this(0, 0);
    }

    public RosettaComplex(double r, double i) {
        re = r;
        im = i;
    }

    public RosettaComplex add(RosettaComplex b) {
        return new RosettaComplex(this.re + b.re, this.im + b.im);
    }

    public RosettaComplex sub(RosettaComplex b) {
        return new RosettaComplex(this.re - b.re, this.im - b.im);
    }

    public RosettaComplex mult(RosettaComplex b) {
        return new RosettaComplex(this.re * b.re - this.im * b.im,
                this.re * b.im + this.im * b.re);
    }

}
