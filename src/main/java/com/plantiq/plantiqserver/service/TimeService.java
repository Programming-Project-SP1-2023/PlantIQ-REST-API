package com.plantiq.plantiqserver.service;

//---------------------------------Service Class---------------------------------
//The TimeService class has been created with the scope of obtaining dates.
// The 3 types of dates it will return are:
// 1) Current Timestamp
// 2) Future Timestamp
// 3) Past Timestamp
//-------------------------------------------------------------------------------
public class TimeService {

    //This method is used to gain the timestamp of a future day.
    //It will get the current timestamp and sum an offset of days received as the parameter.
    public static Long nowPlusDays(int offset) {

        return TimeService.now() + offset * (24 * 3600);
    }

    //Very Similar to the above method, this method will get the current time subtract an offset
    //of days received as the parameter
    public static Long nowMinusDays(int offset) {

        return TimeService.now() - offset * (24 * 3600);
    }

    //This method returns the current timestamp.
    public static Long now() {
        return System.currentTimeMillis() / 1000L;
    }

}
