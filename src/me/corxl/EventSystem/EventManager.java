package me.corxl.EventSystem;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This event manager uses reflection to allow classes to listen to events
 * and invoke event handlers to process these events.
 *
 * @author Reif Servis
 * @version 1.1
 * @since 2023-07-31
 */
public class EventManager {
    private final HashMap<Class<? extends Event>, List<EventCaller>> eventHandlers = new HashMap<>();

    /**
     * Default constructor
     */
    public EventManager() {}

    /**
     * Constructor which allows a single event class to be added when instantiated.
     * @param eventClazz Event to be added as a trigger
     */
    public EventManager(Class<? extends Event> eventClazz) {
        this.addEventTrigger(eventClazz);
    }
    /**
     * Constructor which allows a list of event classes to be added when instantiated.
     * @param eventClazz First event to be added (requires at least one).
     * @param eventClazzes Subsequent list of event classes to be handled.
     */
    @SafeVarargs
    public EventManager(Class<? extends Event> eventClazz, Class<? extends Event>... eventClazzes) {
        this.addEventTriggers(eventClazz, eventClazzes);
    }
    /**
     * Adds a single amount of event classes to the event handler list.
     *
     * @param eventClazz Event to be added as a trigger
     */
    public void addEventTrigger(Class<? extends Event> eventClazz) {
        this.eventHandlers.put(eventClazz, new LinkedList<>());
    }

    /**
     * Adds an unspecified amount of event classes to the event handler list.
     * Requires at least 1 event to be passed in.
     *
     * @param eventClazz First event to be added (requires at least one).
     * @param eventClazzes Subsequent list of event classes to be handled.
     */

    @SafeVarargs
    public final void addEventTriggers(Class<? extends Event> eventClazz, Class<? extends Event>... eventClazzes) {
        this.addEventTrigger(eventClazz);
        for (Class<? extends Event> clazz : eventClazzes) {
            this.addEventTrigger(clazz);
        }
    }

    /**
     * This method is used to register classes that wish to have their
     * methods act as event triggers. Methods that wish to be declared as
     * event triggers must have the event class as their only parameters as well as the
     * \@EventHandler annotation.
     *
     * @param listener This is the class that will listen to events
     */
    public void registerEventListener(Listener listener) {
        Class<? extends Listener> clazz = listener.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getAnnotation(EventHandler.class) == null) continue;
            if (m.getParameterCount() != 1) continue;
            if (!eventHandlers.containsKey(m.getParameterTypes()[0])) continue;
            eventHandlers.get(m.getParameterTypes()[0]).add(new EventCaller(listener, m));
        }
    }
    /**
     * This method is used to register classes that wish to have their
     * methods act as event triggers. Methods that wish to be declared as
     * event triggers must have the event class as their only parameters as well as the
     * \@EventHandler annotation.
     *
     * @param listener This is the first Listener class that will listen to events
     * @param listeners This is the list of Listeners to be registered.
     */
    public void registerEventListeners(Listener listener, Listener... listeners) {
        this.registerEventListener(listener);
        for (Listener l : listeners) {
            this.registerEventListener(l);
        }
    }

    /**
     * Invokes all current triggered based on event's class type.
     *
     * @param event Event to be passed to all subscribers.
     */
    public void invokeTriggers(Event event) {
        Class<? extends Event> clazz = event.getClass();
        if (!eventHandlers.containsKey(clazz)) {
            throw new RuntimeException("Event class is not registered within this manager instance. Use #addEventTrigger(Event) to register this event.");
        }
        eventHandlers.get(clazz).forEach((eventCaller ->
        {
            try {
                eventCaller.method.invoke(eventCaller.caller, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    /**
     * Struct for storing event callers and methods to invoke
     */
    private record EventCaller(Object caller, Method method) {}

}
