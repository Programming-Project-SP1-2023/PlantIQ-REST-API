package com.plantiq.plantiqserver.service;

//Class created to obtain current timestamps and future timestamps in UNIX format
public class TimeService {

    //This method is used to gain the timestamp of a future day.
    //It will get the current timestamp and sum an offset of days received as the parameter.
    public static Long nowPlusDays(int offset){

        return TimeService.now() + offset*(24*3600);
    }

    public static Long nowMinusDays(int offset){

        return TimeService.now() - offset*(24*3600);
    }

    //This method returns the current timestamp.
    public static Long now(){
        return System.currentTimeMillis() / 1000L;
    }

}
