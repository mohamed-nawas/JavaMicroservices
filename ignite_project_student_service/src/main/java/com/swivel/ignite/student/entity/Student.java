package com.swivel.ignite.student.entity;

import com.swivel.ignite.student.dto.request.StudentCreateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Student entity
 */
@Entity
@Table(name = "student")
@NoArgsConstructor
@Getter
@Setter
public class Student implements Serializable {

    @Transient
    private static final String STUDENT_ID_PREFIX = "sid-";

    @Id
    private String id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private Date tuitionJoinedOn;
    private String tuitionId;
    private String authUserId;

    public Student(StudentCreateRequestDto requestDto) {
        this.id = STUDENT_ID_PREFIX + UUID.randomUUID();
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
    }
}
