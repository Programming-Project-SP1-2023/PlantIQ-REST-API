package com.plantiq.plantiqserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ANIMAL")
public class Animal {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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
