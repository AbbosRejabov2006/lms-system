package com.example.lmssystem.repository;


import com.example.lmssystem.entity.User;
import com.example.lmssystem.enums.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends  JpaRepository<User, Long> {

}
