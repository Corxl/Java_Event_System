package me.corxl.EventSystem;

public abstract class Event {
    private boolean isCancelled = false;
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
    public boolean isCancelled() {
        return this.isCancelled;
    }
}
