package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Course;
import com.example.lmssystem.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByTeacherId(Long teacherId);
    List<Group> findAllByCourse_Id(Long course);
}