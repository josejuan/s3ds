package com.s3ds.recon;

import java.util.function.Function;

public class Util {
    public static <A, B> Function<A, B> safe(UnsafeFunc<A, B> k) {
        return a -> {
            try {
                return k.apply(a);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
    }

    public interface UnsafeFunc<A, B> {
        B apply(A x) throws Exception;
    }
}
