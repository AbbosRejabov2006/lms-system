package com.example.lmssystem.service;

import com.example.lmssystem.dto.StudentDTO;
import com.example.lmssystem.entity.Group;
import com.example.lmssystem.entity.Role;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.GroupRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.repository.StudentRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    public User checkUser(StudentDTO studentDTO) {
        User user = userRepository.findByPhoneNumber(studentDTO.phoneNumber()).orElseThrow();
        if (Objects.equals(user.getFirstName() , studentDTO.name()) && Objects.equals(user.getLastName() , studentDTO.surname())){
            for (Role role : user.getRole()) {
                if (role.getName().equals("STUDENT")) {
                    return user;
                }
            }
            List<Role> role = user.getRole();
            role.add(roleRepository.findByName("STUDENT").orElseThrow());
            user.setRole(role);
            return userRepository.save(user);
        }
        throw new RuntimeException("Invalid student");
    }
    public boolean chekCourse(User user , Long courseId){
        List<Group> groups = groupRepository.findAllByCourse_Id(courseId);
        for (Group group : groups) {
            for (User student : group.getStudents()) {
                return !Objects.equals(student.getId() , user.getId());
            }
        }
        return true;
    }

    public ResponseEntity<?> add(User student, Long courseId) {
        Group grouped = null;
        List<Group> groups = groupRepository.findAllByCourse_Id(courseId);
        for (Group group : groups) {
            if (group.getRoom().getCapacity()>group.getStudents().size()) {
                group.getStudents().add(student);
                grouped= groupRepository.save(group);
            }
        }
        if(grouped == null){
            grouped = groupService.addStudentToNewGroup(courseId);
            List<User> students = grouped.getStudents();
            students.add(student);
            grouped.setStudents(students);
            grouped = groupRepository.save(grouped);
        }
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .message(Utils.getMessage("success"))
                        .data(grouped)
                        .success(true)
                        .build()

        );

    }
}



