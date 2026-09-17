package com.user_service.users.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "edu_users", schema = "edu_tech_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "UUID")
    private UUID userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "grade")
    private String grade;

    @Column(name = "password")
    private String password;

}
