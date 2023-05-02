package com.tpe.repository;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository//Bu annotation'ı yazmasak da extends JpaRepository'den dolayı çalışır. Kod okunabilirliğini arttırmak için yine de yazıyoruz.
public interface StudentRepository extends JpaRepository<Student, Long> {
     //1. parametre entity classımız. 2.parametre primary keyin data type'ı.
    // extends JpaRepository<Student, Long> => Spring data JPA nın bize sagladıgı  methodları kullanmamıza yarar. Sql kodu yazmadan methodlarla işlem yapmamıza olanak sağlar.

     //Bu methodun bodysini Spring Boot dolduruyor.
     boolean existsByEmail(String email);

     List<Student> findByLastName(String lastName);

     //JPQL
     //Parametreyi sorguda kullanıcaksak @Param annotationu kullanmamız gerek.
     @Query("SELECT s FROM Student s WHERE s.grade=:pGrade")
     List<Student> findAllEqualsGrade(@Param("pGrade") Integer grade);

     //SQL
     @Query(value = "SELECT * FROM Student s WHERE s.grade=:pGrade", nativeQuery = true)
     List<Student> findAllEqualsGradeWithSql(@Param("pGrade") Integer grade);

     //JPQL mucizesi ile POJO-DTO dönüşümü
     //JPQL sayesinde gelen recordlar DTO ya dönüşüyor.
     //Bu sekilde mappleme yaparsak fieldlar EŞİT olmalıdır. DTO da id yoksa pojoda id varsa hata alırız.
     @Query("SELECT new com.tpe.dto.StudentDTO(s) FROM Student s WHERE s.id=:id")
     Optional<StudentDTO> findStudentDTOById(@Param("id") Long id);


}
