package hu.rivalsnetwork.rivalsapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Scheduler {
    private final JavaPlugin plugin;

    public Scheduler(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void run(@NotNull Runnable task) {
        Bukkit.getScheduler().runTask(this.plugin, task);
    }

    public void runAsync(@NotNull Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, task);
    }

    public void runLater(@NotNull Runnable task, long ticksLater) {
        Bukkit.getScheduler().runTaskLater(this.plugin, task, ticksLater);
    }

    public void runLaterAsync(@NotNull Runnable task, long ticksLater) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, task, ticksLater);
    }

    public void runTimer(@NotNull Runnable task, long init, long repeat) {
        Bukkit.getScheduler().runTaskTimer(this.plugin, task, init, repeat);
    }

    public void runTimerAsync(@NotNull Runnable task, long init, long repeat) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, task, init, repeat);
    }

    public void runTimer(@NotNull Consumer<BukkitTask> task, long init, long repeat) {
        Bukkit.getScheduler().runTaskTimer(this.plugin, task, init, repeat);
    }

    public void runTimerAsync(@NotNull Consumer<BukkitTask> task, long init, long repeat) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, task, init, repeat);
    }
}
