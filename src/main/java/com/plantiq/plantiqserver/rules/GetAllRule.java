package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

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
        limit.add("integer");
        offset.add("max:100");

        this.rules.put("offset",offset);

        ArrayList<String> sortBy = new ArrayList<>();

        sortBy.add("required");
        sortBy.add("min:3");
        sortBy.add("max:25");

        this.rules.put("sortBy",sortBy);

        ArrayList<String> order = new ArrayList<>();

        order.add("required");
        order.add("enum:Sort");

        this.rules.put("order",order);
    }
}
