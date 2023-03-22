package com.plantiq.plantiqserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Animal {
	@Id
	@Generated
	private Long id;
	private String name;
	private String type;
	private int age;

	public Animal(String name, String type, int age) {
		this.name = name;
		this.type = type;
		this.age = age;
	}
}
