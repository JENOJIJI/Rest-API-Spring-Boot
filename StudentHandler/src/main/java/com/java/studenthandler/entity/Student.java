package com.java.studenthandler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rollNo;
	@Column
	private String name;
	@Column
	private int age;
	@Column
	private String branch;
	
	public Student() {

	}
	
	
	
	public Student(String name, int age, String branch) {
		super();
		this.name = name;
		this.age = age;
		this.branch = branch;
	}
	public Student(int rollNo,String name, int age, String branch) {
		super();
		this.rollNo=rollNo;
		this.name = name;
		this.age = age;
		this.branch = branch;
	}


	public int getRollNo() {
		return rollNo;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public String getBranch() {
		return branch;
	}
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}

}
