package net.gittab.statemachine.guard;

/**
 * Guard.
 *
 * @author rookiedev
 * @date 2020/8/21 2:40 下午
 **/
@FunctionalInterface
public interface Guard {

    /**
     * evaluate a guard condition.
     * @return true, if guard evaluation is successful, false otherwise
     */
    boolean evaluate();

}
