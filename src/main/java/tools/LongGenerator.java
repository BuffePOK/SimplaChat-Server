package tools;

import databank.Databank;

import java.util.Random;

public class LongGenerator {
    static Random random = new Random();

    private LongGenerator() {}

    public static synchronized long generate() {
        long ans = 0;
        while(true) {
            if(!Databank.isHasID(ans = random.nextLong()))
                return ans;
        }
    }
}
