package com.plantiq.plantiqserver.jobs;

import com.plantiq.plantiqserver.core.Job;
import com.plantiq.plantiqserver.model.Session;
import com.plantiq.plantiqserver.service.TimeService;

import java.util.ArrayList;

//--------------------------------------Job Class--------------------------------------
//This Job class has been created with the scope of deleting all the session tokens
//that are expired.
//-------------------------------------------------------------------------------------

public class CleanExpiredSessionTokensJob extends Job {

    //This method goes through all the sessions stored in the database and deletes those which are expired
    @Override
    public boolean run() {
        //Extraction of sessions which have an expiry date older that the current time
        ArrayList<Session> items = Session.collection().whereGreaterThan("expiry", TimeService.now().toString()).get();

        //Deletion of each session extracted into the list
        items.forEach((n)->{
            n.delete("token");
        });

        return true;
    }
}
