package me.refracdevelopment.simpletags.utilities;

import lombok.experimental.UtilityClass;
import me.refracdevelopment.simpletags.SimpleTags;

@UtilityClass
public class Tasks {

    public void run(Runnable callable) {
        runLater(callable, 1L);
    }

    public void runAsync(Runnable callable) {
        runAsyncLater(callable, 1L);
    }

    public void runLater(Runnable callable, long delay) {
        SimpleTags.getInstance().getFoliaLib().getImpl().runLater(callable, delay);
    }

    public void runAsyncLater(Runnable callable, long delay) {
        SimpleTags.getInstance().getFoliaLib().getImpl().runLaterAsync(callable, delay);
    }

    public void runTimer(Runnable callable, long delay, long interval) {
        SimpleTags.getInstance().getFoliaLib().getImpl().runTimer(callable, delay, interval);
    }

    public void runAsyncTimer(Runnable callable, long delay, long interval) {
        SimpleTags.getInstance().getFoliaLib().getImpl().runTimerAsync(callable, delay, interval);
    }
}