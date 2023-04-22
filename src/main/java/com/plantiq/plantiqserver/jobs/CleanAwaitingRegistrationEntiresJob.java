package com.plantiq.plantiqserver.jobs;

import com.plantiq.plantiqserver.core.Job;
import com.plantiq.plantiqserver.model.AwaitingRegistration;
import com.plantiq.plantiqserver.service.TimeService;

import java.util.ArrayList;

public class CleanAwaitingRegistrationEntiresJob extends Job {

    @Override
    public boolean run() {

        ArrayList<AwaitingRegistration> items = AwaitingRegistration.collection().whereGreaterThan("date", TimeService.now().toString()).get();

        items.forEach((n)->{
            n.delete("token");
        });

        return true;
    }
}
