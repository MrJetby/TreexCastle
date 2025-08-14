package me.jetby.treexCastle;

import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class ShulkerManager {
    private final Main plugin;
    private final Random RANDOM = new Random();

    public String getRandomType() {

        int r = 0;
        int randInt = RANDOM.nextInt(plugin.getTypes().getTypes().size());
        for (String type : plugin.getTypes().getTypes().keySet()) {
            if (r==randInt) {
                return type;
            }
            r++;
        }

        return null;
    }

}
