package com.waynejackson.firebasedatademo.utils;

import java.util.Random;

/**
 * Created by wayne.jackson on 1/5/17.
 */

public class RandomSentenceGenerator {
    private static final int NUM_WORDS = 5;
    private static final String SPACE = " ";
    private static final  String PERIOD = ".";

    private static final String article[] = { "the", "a", "one", "some", "any" };
    private static final String noun[] = { "boy", "girl", "dog", "town", "car" };
    private static final String verb[] = { "drove", "jumped", "ran", "walked", "skipped" };
    private static final String preposition[] = { "to", "from", "over", "under", "on" };

    private static Random r = new Random();

    public static String getRandomSentence() {
        String sentence;
        sentence = article[rand()];
        char c = sentence.charAt(0);
        sentence = sentence.replace( c, Character.toUpperCase(c) );
        sentence += SPACE + noun[rand()] + SPACE;
        sentence += (verb[rand()] + SPACE + preposition[rand()]);
        sentence += (SPACE + article[rand()] + SPACE + noun[rand()]);
        sentence += PERIOD;
        return sentence;
    }

    private static int rand() {
        int ri = r.nextInt() % NUM_WORDS;
        if (ri < 0) {
            ri += NUM_WORDS;
        }
        return ri;
    }
}
