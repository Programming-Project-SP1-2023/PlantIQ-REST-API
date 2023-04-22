package com.plantiq.plantiqserver.jobs;

import com.plantiq.plantiqserver.core.Job;
import com.plantiq.plantiqserver.model.Session;
import com.plantiq.plantiqserver.service.TimeService;

import java.util.ArrayList;

public class CleanExpiredSessionTokensJob extends Job {

    @Override
    public boolean run() {

        ArrayList<Session> items = Session.collection().whereGreaterThan("expiry", TimeService.now().toString()).get();

        items.forEach((n)->{
            n.delete("token");
        });

        return true;
    }
}
