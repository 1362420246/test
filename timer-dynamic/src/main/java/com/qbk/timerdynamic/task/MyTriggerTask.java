package com.qbk.timerdynamic.task;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.config.TriggerTask;

public class MyTriggerTask extends TriggerTask {

    private final String name;

    public String getName() {
        return name;
    }

    /**
     * Create a new {@link TriggerTask}.
     *
     * @param runnable the underlying task to execute
     * @param trigger  specifies when the task should be executed
     */
    public MyTriggerTask(Runnable runnable, Trigger trigger ,String name) {
        super(runnable, trigger);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyTriggerTask that = (MyTriggerTask) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
