package com.example.lmssystem.dto;

import com.example.lmssystem.enums.Gender;
import com.example.lmssystem.enums.Group;

public record StudentDTO(
        String surname ,
        String name ,
        String phoneNumber ,
        Long courseId
) {
}
