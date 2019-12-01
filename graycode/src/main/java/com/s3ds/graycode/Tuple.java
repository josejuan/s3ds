package com.s3ds.graycode;

public class Tuple<A, B> {
    public final A fst;
    public final B snd;

    public Tuple(A fst, B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public static <A, B> Tuple<A, B> tuple(A fst, B snd) {
        return new Tuple<>(fst, snd);
    }

    public A first() {
        return fst;
    }

    public B second() {
        return snd;
    }
}
