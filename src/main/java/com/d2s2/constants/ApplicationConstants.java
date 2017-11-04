package com.d2s2.constants;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class ApplicationConstants {

    public static final String IP = "127.0.0.1";
    public static final int PORT = randomWithRange(45000, 55000);
    public static final String USER_NAME = "vin";
    public static final int HOPS = 10;

    public static final int HEART_BEAT_THRESHOLD = 10;

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
}
