package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Limit:                                                                            |
//  A) Required -> Validates the limit input is present and not null.                 |
//  B) Integer -> Validates the input is an integer.                                  |
//  C) Min -> Validates the limit is not less than 10.                                |
//  D) Max -> Validates the limit is not greater than 100.                            |
//                                                                                    |
//  Offset:                                                                           |
//  A) Required -> Validates the offset input is present and not null.                |
//  B) Integer -> Validates the input is an integer.                                  |
//  C) Min -> Validates the offset is not less than 0.                                |
//  D) Max -> Validates the offset is not greater than 100.                           |
//                                                                                    |
//  Sort By:                                                                          |
//  A) Required -> Validates the sorting value is present and not null.               |
//  B) min:2 -> Validates the sorting value is at least 2 characters long.            |
//  C) max:25 -> Validates the sorting value is no larger than 25 characters long.    |
//                                                                                    |
//  Sort:                                                                             |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) Enum:Sort -> Validates the input is one of the two values of the Sort enum:    |
//                  ASC or Desc                                                       |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class GetAllRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> limit = new ArrayList<>();

        limit.add("required");
        limit.add("integer");
        limit.add("min:10");
        limit.add("max:100");

        this.rules.put("limit",limit);

        ArrayList<String> offset = new ArrayList<>();

        offset.add("required");
        offset.add("min:0");
        offset.add("integer");
        offset.add("max:100");

        this.rules.put("offset",offset);

        ArrayList<String> sortBy = new ArrayList<>();

        sortBy.add("required");
        sortBy.add("min:3");
        sortBy.add("max:25");

        this.rules.put("sortBy",sortBy);

        ArrayList<String> sort = new ArrayList<>();

        sort.add("required");
        sort.add("enum:Sort");

        this.rules.put("sort",sort);
    }
}
