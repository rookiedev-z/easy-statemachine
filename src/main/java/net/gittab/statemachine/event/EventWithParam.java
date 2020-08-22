package net.gittab.statemachine.event;

/**
 * EventWithParam.
 *
 * @author rookiedev
 * @date 2020/8/21 2:20 下午
 **/
public class EventWithParam<E> {

    private final E event;

    private final Class<?> [] paramTypes;

    public EventWithParam(final E event, final Class<?>... paramTypes){
        assert paramTypes != null : "param types arg is null";
        this.event = event;
        this.paramTypes = paramTypes;;
    }

    public E getEvent(){
        return this.event;
    }
}
