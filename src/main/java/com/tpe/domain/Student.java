package com.tpe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

//Class seviye annolardır. Tüm variablelar icin gelir.

@Getter //Getter ve setter methodları elimizle yazmamıza gerek yok LOMBOK bunu bizim icin yapıyor.
@Setter
@AllArgsConstructor //parametreli const
//@RequiredArgsConstructor // final ile setlediğimiz fieldlarımızdan parametreli  constructor üretiyor. final yazmayan parametreye eklenmez.
@NoArgsConstructor //parametresiz const


@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   //@Setter(AccessLevel.NONE) //idnin setterı calısmasın demiş olduk.
    private Long id;

    @NotNull(message = "First name can not be null")
    @NotBlank(message = "Last name can not be white space")
    @Size(min=2, max=25,message = "First name '${validatedValue}' must be between {min} and {max} long ") //${validatedValue} name den alır.
    @Column(nullable = false, length = 25)

    private /*final*/ String name;

    private /*final*/ String lastName;

    private /*final*/ Integer grade;

    @Column(nullable = false, unique = true)
    @Email(message = "Provide valid email") //@ ve . yı kontrol eder.
    private /*final*/ String email;

    private /*final*/ String phoneNumber;

    //@Setter(AccessLevel.NONE) //createDate nin setterı calısmasın demiş olduk.
    private LocalDateTime createDate = LocalDateTime.now(); //DB de olusturulma tarihi

    //Getter -setter

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public int getGrade() {
//        return grade;
//    }
//
//    public void setGrade(int grade) {
//        this.grade = grade;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public LocalDateTime getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(LocalDateTime createDate) {
//        this.createDate = createDate;
//    }
//
//    //const
//
//    public Student(Long id, String name, String lastName, int grade, String email, String phoneNumber, LocalDateTime createDate) {
//        this.id = id;
//        this.name = name;
//        this.lastName = lastName;
//        this.grade = grade;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.createDate = createDate;
//    }
//
//    public Student() {
//    }
//
//    //toString
//
//    @Override
//    public String toString() {
//        return "Student{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", grade=" + grade +
//                ", email='" + email + '\'' +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                ", createDate=" + createDate +
//                '}';
//    }
}
