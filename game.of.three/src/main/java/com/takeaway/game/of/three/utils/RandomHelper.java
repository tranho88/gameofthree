package com.takeaway.game.of.three.utils;

import java.util.Random;

public class RandomHelper {
    final static Random random = new Random();

    /**
     * Get random number between min and max inclusively
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNumber(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Get random number which is less than or equal max
     * @param max
     * @return
     */
    public static int getRandomNumber(int max){
        return getRandomNumber(0, max);
    }

}
