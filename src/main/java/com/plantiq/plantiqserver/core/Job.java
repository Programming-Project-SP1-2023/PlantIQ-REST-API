package com.plantiq.plantiqserver.core;

import java.io.Serializable;

public abstract class Job implements Serializable {

    public abstract boolean run();
}
