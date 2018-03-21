package com.example.cristianverdes.bakingapp.utils;

/**
 * Created by cristian.verdes on 21.03.2018.
 */

public class Preconditions {
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
}
