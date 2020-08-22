package net.gittab.statemachine.action;

/**
 * Action.
 *
 * @author rookiedev
 * @date 2020/8/21 3:03 下午
 **/
@FunctionalInterface
public interface Action {

    /**
     * execute action.
     */
    void execute();
}
