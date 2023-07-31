package EventSystem;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class EventManager {
    private final HashMap<Class<? extends Event>, List<EventCaller>> eventHandlers = new HashMap<>();
    protected EventManager() {

    }
    protected void addEventTrigger(Class<? extends Event> event) {
        this.eventHandlers.put(event, new LinkedList<>());
    }

    @SafeVarargs
    protected final void addEventTriggers(Class<? extends Event>... event) {
        for (Class<? extends Event> clazz : event) {
            this.eventHandlers.put(clazz, new LinkedList<>());
        }
    }

    public void registerEvents(Listener listener) {
        Class<? extends Listener> clazz = listener.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getAnnotation(EventHandler.class) == null) continue;
            if (m.getParameterCount() != 1) continue;
            if (!eventHandlers.containsKey(m.getParameterTypes()[0])) continue;
            eventHandlers.get(m.getParameterTypes()[0]).add(new EventCaller(listener, m));
        }
    }

    public void InvokeTriggers(Event event) {
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

    private record EventCaller(Object caller, Method method) {}

}
