package com.plantiq.plantiqserver.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

class GetAllRuleTest {
	private GetAllRule getAllRule;
	HashMap<String, Object> rules = new HashMap<>();

	@BeforeEach
	void setup() {
		getAllRule = new GetAllRule();
		getAllRule.setup();
	}

	@Test
	void testLimit() {
		List<String> limitRules = getAllRule.getRules().get("limit");
		assert limitRules.size() == 4;
		assert limitRules.contains("required");
		assert limitRules.contains("integer");
		assert limitRules.contains("min:10");
		assert limitRules.contains("max:100");
	}

	@Test
	public void testOffsetRules() {
		List<String> offsetRules = getAllRule.getRules().get("offset");
		assert offsetRules.contains("required");
		assert offsetRules.contains("integer");
		assert offsetRules.contains("min:0");
		assert offsetRules.contains("max:100");
	}

	@Test
	public void testSortByRules() {
		List<String> sortByRules = getAllRule.getRules().get("sortBy");
		assert sortByRules.contains("required");
		assert sortByRules.contains("min:3");
		assert sortByRules.contains("max:25");
	}

	@Test
	public void testSortRules() {
		List<String> sortRules = getAllRule.getRules().get("sort");
		assert sortRules.contains("required");
		assert sortRules.contains("enum:Sort");
	}
}