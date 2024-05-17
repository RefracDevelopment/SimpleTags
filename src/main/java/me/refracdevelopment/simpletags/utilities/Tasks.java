package me.refracdevelopment.simpletags.utilities;

import me.refracdevelopment.simpletags.SimpleTags;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Tasks {

    private Tasks() {
    }

    public static void runAsync(Runnable callable) {
        SimpleTags.getInstance().getPaperLib().scheduling().asyncScheduler().run(callable);
    }

    public static void runAsyncTimer(Runnable callable, long delay) {
        SimpleTags.getInstance().getPaperLib().scheduling().asyncScheduler().runDelayed(callable, Duration.of(delay, TimeUnit.SECONDS.toChronoUnit()));
    }
}