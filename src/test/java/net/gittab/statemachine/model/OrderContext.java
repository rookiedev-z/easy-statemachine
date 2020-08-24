package net.gittab.statemachine.model;

import net.gittab.statemachine.StateEnum;

/**
 * OrderContext.
 *
 * @author rookiedev
 * @date 2020/8/24 21:10
 **/
public class OrderContext {

    private Long id;

    private StateEnum status;

    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StateEnum getStatus() {
        return status;
    }

    public void setStatus(StateEnum status) {
        this.status = status;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
