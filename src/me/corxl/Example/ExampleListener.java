package me.corxl.Example;

import me.corxl.EventSystem.EventHandler;
import me.corxl.EventSystem.Listener;

public class ExampleListener implements Listener {

    @EventHandler
    public void onHello(ExampleEvent event) {
        System.out.println(event.getMessage() + " from ExampleListener!");
    }
}
