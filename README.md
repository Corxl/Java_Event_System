# Description
Event system that functions similarly to [Spigot's event system](https://www.spigotmc.org/wiki/using-the-event-api/). This system uses reflection to allow users to subscribe classes to events.

## How to implement
### Creating an Event
Events are used to store and pass data to subscribers whenever something happens in code.
For example, if you were building a game and wanted the ability for a UI to update a scoreboard when someone scores a point, you can use events to tell the UI when to update. The main advantage of this is to minimize class dependencies. This way, the logic for handling the scoreboard and the visuals on the UI are not both dependent on each other. The logic becomes more isolated from the visuals. 

**Step 1**. Create a class that extends the included Event class. This class stores methods for handling whether or not the event is canceled.

**Step 2**. Register that class in your **EventManager** by invoking **#addEventTrigger(Class)**. This allows listeners to listen to when this event is called. 

Events are meant to contain data you want to be processed by listeners. By default, all Events have a getter and setter for whether or not the event should be canceled. 

### Creating an Event Listener
Event listeners are used to, well, listen to events. The event manager needs to know which methods from which classes need to be processed as events. 

**Step 1**. Create a class that implements the Listener interface. This is the only way to declare a class as a listener. 

**Step 2**. Add this class as a registered event by calling #registerEventListener(Listener) on your EventManager. Note: the desired event must be added to the triggers before an event listener can be registered. In other words, register ALL events before registering each listener.

**Step 3**. Create the method(s) you would like to use to handle an event. Each event requires the @EventHandler annotation and must only include the desired Event as a parameter. Each listener class can contain an indefinite amount of event handlers. 

**[Examples](https://github.com/Corxl/Java_Event_System/tree/master/src/me/corxl/Example)** 
