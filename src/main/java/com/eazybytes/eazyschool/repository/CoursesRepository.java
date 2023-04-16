package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Course, Integer> {

    List<Course> findByOrderByNameDesc();

    List<Course> findByOrderByName();
}
