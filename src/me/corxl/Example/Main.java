package me.corxl.Example;

import me.corxl.EventSystem.EventManager;

public class Main {
    private EventManager manager;

    public Main() {
        manager = new EventManager(); // <- Can be used as a singleton
        // Add class to trigger list
        manager.addEventTrigger(ExampleEvent.class);

        // Create Listener class instance and register it to the event manager
        manager.registerEventListener(new ExampleListener());

        // Create event
        ExampleEvent event = new ExampleEvent("Hello World!");

        // Invoke all methods that are listeners to this event.
        manager.invokeTriggers(event);

    }

    public static void main(String[] args) {
        new Main();
    }
}
