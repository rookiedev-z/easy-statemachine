package net.gittab.statemachine;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import net.gittab.statemachine.enums.EventEnum;
import net.gittab.statemachine.enums.StateEnum;
import net.gittab.statemachine.state.StateRepresentation;
import net.gittab.statemachine.transition.InternalTransitionBehaviour;
import net.gittab.statemachine.transition.Transition;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateRepresentationTest.
 *
 * @author rookiedev
 * @date 2020/8/26 11:21 上午
 **/
public class StateRepresentationTest {

    private Transition<StateEnum, EventEnum> actualTransition = null;

    private boolean executed = false;

    private int order = 0;
    private int subOrder = 0;
    private int superOrder = 0;

    @Test
    public void uponEntering_EntryActionExecuted(){
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.B);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(StateEnum.A, StateEnum.B, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        actualTransition = null;
        stateRepresentation.addEntryAction((tran, context) -> {
           actualTransition = tran;
        });
        stateRepresentation.entry(transition, "state machine context");
        Assert.assertEquals(transition, actualTransition);
    }

    @Test
    public void uponLeaving_EnteringActionsNotExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.B);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(StateEnum.A, StateEnum.B, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        actualTransition = null;
        stateRepresentation.addEntryAction((tran, context) -> {
            actualTransition = tran;
        });
        stateRepresentation.exit(transition, "state machine context");
        Assert.assertNull(actualTransition);
    }

    @Test
    public void uponLeaving_LeavingActionsExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(StateEnum.A, StateEnum.B, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        actualTransition = null;
        stateRepresentation.addExitAction((tran, context) -> {
            actualTransition = tran;
        });
        stateRepresentation.exit(transition, "state machine context");
        Assert.assertEquals(transition, actualTransition);
    }

    @Test
    public void uponEntering_LeavingActionsNotExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(StateEnum.A, StateEnum.B, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        actualTransition = null;
        stateRepresentation.addExitAction((tran, context) -> {
            actualTransition = tran;
        });
        stateRepresentation.entry(transition, "state machine context");
        Assert.assertNull(actualTransition);
    }

    @Test
    public void includesUnderlyingState() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Assert.assertTrue(stateRepresentation.includes(StateEnum.A));

    }

    @Test
    public void doesNotIncludeUnrelatedState() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Assert.assertFalse(stateRepresentation.includes(StateEnum.B));
    }

    @Test
    public void includesSubState() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.S1);
        stateRepresentation.addSubState(new StateRepresentation<>(StateEnum.S11));
        Assert.assertTrue(stateRepresentation.includes(StateEnum.S11));
    }

    @Test
    public void doesNotIncludeSuperstate() {
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);
        subStateRepresentation.setSuperstate(new StateRepresentation<>(StateEnum.S1));
        Assert.assertFalse(subStateRepresentation.includes(StateEnum.S1));

    }

    @Test
    public void isIncludedInUnderlyingState() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Assert.assertTrue(stateRepresentation.isIncludedIn(StateEnum.A));
    }

    @Test
    public void isNotIncludedInUnrelatedState() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Assert.assertFalse(stateRepresentation.isIncludedIn(StateEnum.B));
    }

    @Test
    public void isNotIncludedInSubState() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.S1);
        stateRepresentation.addSubState(new StateRepresentation<>(StateEnum.S11));
        Assert.assertFalse(stateRepresentation.isIncludedIn(StateEnum.S11));
    }

    @Test
    public void isIncludedInSuperstate() {
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);
        subStateRepresentation.setSuperstate(new StateRepresentation<>(StateEnum.S1));
        Assert.assertTrue(subStateRepresentation.isIncludedIn(StateEnum.S1));
    }

    @Test
    public void whenTransitioningFromSuperToSubState_SubStateEntryActionsExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S1);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(superStateRepresentation.getState(), subStateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        executed = false;
        subStateRepresentation.addEntryAction((tran, context) -> {
            executed = true;
        });
        subStateRepresentation.entry(transition, "state machine context");
        Assert.assertTrue(executed);
    }

    @Test
    public void whenTransitioningFromSuperToSubState_SuperEntryActionsNotExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S2);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S21);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(superStateRepresentation.getState(), subStateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        executed = false;
        superStateRepresentation.addEntryAction((tran, context) -> {
            executed = true;
        });
        superStateRepresentation.entry(transition, "state machine context");
        Assert.assertFalse(executed);
    }

    @Test
    public void whenTransitioningFromSuperToSubState_SuperExitActionsNotExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S2);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S21);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(superStateRepresentation.getState(), subStateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        executed = false;
        superStateRepresentation.addExitAction((tran, context) -> {
            executed = true;
        });
        superStateRepresentation.exit(transition, "state machine context");
        Assert.assertFalse(executed);
    }

    @Test
    public void whenTransitioningFromSubToSuperstate_SubStateExitActionsExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S1);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(subStateRepresentation.getState(), superStateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        executed = false;
        subStateRepresentation.addExitAction((tran, context) -> {
            executed = true;
        });
        subStateRepresentation.exit(transition, "state machine context");
        Assert.assertTrue(executed);
    }

    @Test
    public void whenEnteringSubState_SuperEntryActionsExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S2);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S21);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(StateEnum.S13, subStateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        executed = false;
        superStateRepresentation.addEntryAction((tran, context) -> {
            executed = true;
        });
        subStateRepresentation.entry(transition, "state machine context");
        Assert.assertTrue(executed);
    }

    @Test
    public void whenLeavingSubState_SuperExitActionsExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S1);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(subStateRepresentation.getState(), StateEnum.S21, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        executed = false;
        superStateRepresentation.addExitAction((tran, context) -> {
            executed = true;
        });
        subStateRepresentation.exit(transition, "state machine context");
        Assert.assertTrue(executed);
    }

    @Test
    public void whenFromSubToAnotherSub_SuperExitActionsNotExecuted() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S1);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation2 = new StateRepresentation<>(StateEnum.S12);

        superStateRepresentation.addSubState(subStateRepresentation);
        superStateRepresentation.addSubState(subStateRepresentation2);

        subStateRepresentation.setSuperstate(superStateRepresentation);
        subStateRepresentation2.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(subStateRepresentation.getState(), subStateRepresentation2.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        executed = false;
        superStateRepresentation.addExitAction((tran, context) -> {
            executed = true;
        });
        subStateRepresentation.exit(transition, "state machine context");
        Assert.assertFalse(executed);
    }

    @Test
    public void entryActionsExecuteInOrder() {
        final ArrayList<Integer> orders = new ArrayList<>();
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.B);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(StateEnum.A, stateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        stateRepresentation.addEntryAction((tran, context) -> {
            orders.add(0);
        });
        stateRepresentation.addEntryAction((tran, context) -> {
            orders.add(1);
        });
        stateRepresentation.entry(transition, "state machine context");

        Assert.assertEquals(2, orders.size());
        Assert.assertEquals(Integer.valueOf(0), orders.get(0));
        Assert.assertEquals(Integer.valueOf(1), orders.get(1));
    }

    @Test
    public void exitActionsExecuteInOrder() {
        final ArrayList<Integer> orders = new ArrayList<>();
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(stateRepresentation.getState(), StateEnum.B, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        stateRepresentation.addExitAction((tran, context) -> {
            orders.add(0);
        });
        stateRepresentation.addExitAction((tran, context) -> {
            orders.add(1);
        });
        stateRepresentation.exit(transition, "state machine context");

        Assert.assertEquals(2, orders.size());
        Assert.assertEquals(Integer.valueOf(0), orders.get(0));
        Assert.assertEquals(Integer.valueOf(1), orders.get(1));
    }

    @Test
    public void eventNotConfigure_EventCannotFired() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Assert.assertFalse(stateRepresentation.canFire(EventEnum.X, "state machine context"));
    }

    @Test
    public void whenTransitionGuardConditionNotMet_EventCannotFired() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(stateRepresentation.getState(), stateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        stateRepresentation.addEventBehaviour(new InternalTransitionBehaviour<>(transition, (tran1, context1) -> false, (tran2, context2) ->{}));
        Assert.assertFalse(stateRepresentation.canFire(EventEnum.X, "state machine context"));
    }

    @Test
    public void whenTransitionGuardConditionMet_EventCanFired() {
        StateRepresentation<StateEnum, EventEnum, String> stateRepresentation = new StateRepresentation<>(StateEnum.A);
        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(stateRepresentation.getState(), stateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };
        stateRepresentation.addEventBehaviour(new InternalTransitionBehaviour<>(transition, (tran1, context1) -> true, (tran2, context2) ->{}));
        Assert.assertTrue(stateRepresentation.canFire(EventEnum.X, "state machine context"));
    }

    @Test
    public void whenTransitionExistsInSuper_SubCanFired() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S1);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(subStateRepresentation.getState(), StateEnum.S21, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };

        superStateRepresentation.addEventBehaviour(new InternalTransitionBehaviour<>(transition, (tran1, context1) -> true, (tran2, context2) ->{}));
        Assert.assertTrue(subStateRepresentation.canFire(EventEnum.X, "state machine context"));
    }

    @Test
    public void whenEnteringSubState_SuperstateEntryActionsExecuteBeforeSubState() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S2);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S21);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(StateEnum.S13, subStateRepresentation.getState(), EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };

        order = 0;
        subOrder = 0;
        superOrder = 0;
        superStateRepresentation.addEntryAction((tran, context) -> {
            superOrder = order++;
        });

        subStateRepresentation.addEntryAction((tran, context) -> {
            subOrder = order++;
        });

        subStateRepresentation.entry(transition, "state machine context");
        Assert.assertTrue(superOrder < subOrder);
    }

    @Test
    public void whenExitingSubState_SubStateExitActionsExecuteBeforeSuperstate() {
        StateRepresentation<StateEnum, EventEnum, String> superStateRepresentation = new StateRepresentation<>(StateEnum.S1);
        StateRepresentation<StateEnum, EventEnum, String> subStateRepresentation = new StateRepresentation<>(StateEnum.S11);

        superStateRepresentation.addSubState(subStateRepresentation);
        subStateRepresentation.setSuperstate(superStateRepresentation);

        Transition<StateEnum, EventEnum> transition = new Transition<StateEnum, EventEnum>(subStateRepresentation.getState(), StateEnum.S21, EventEnum.X) {
            @Override
            public StateEnum transition() {
                return null;
            }
        };

        order = 0;
        subOrder = 0;
        superOrder = 0;
        superStateRepresentation.addExitAction((tran, context) -> {
            superOrder = order++;
        });

        subStateRepresentation.addExitAction((tran, context) -> {
            subOrder = order++;
        });

        subStateRepresentation.exit(transition, "state machine context");
        Assert.assertTrue(superOrder > subOrder);
    }



}
