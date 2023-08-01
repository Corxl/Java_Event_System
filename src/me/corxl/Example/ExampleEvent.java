package me.corxl.Example;

import me.corxl.EventSystem.Event;

public class ExampleEvent extends Event {
    private String message;
    public ExampleEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
