package com.java.studenthandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.studenthandler.entity.Student;

public interface StudentRepository extends JpaRepository<Student,Integer>{
	

}
