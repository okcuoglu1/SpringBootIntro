package com.tpe.controller;


import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController //Controller ı özelleştirip REST mimarisini kullanıcağımızı söyledik.
                //Status kodlarını da göndericeğimizi belirtiyoruz.
@RequestMapping("/students") //http://localhost:8080/students //Handler mapping yardımıyla  /student ile bir endpoint gelirse gelen requesti bu class da karşıla.
public class StudentController {

    //!!! LOGGER
   Logger logger =  LoggerFactory.getLogger(StudentController.class);

    @Autowired //DI
    private StudentService studentService;


    //!!! Bütün ögrencileri getirelim.
    //NOT:
    //Birden çok kişi geleceği için List<> yapısını kullandık.
    //Array kullanmamız için kişi sayısının belli olması gerekirdi.
    //ResponseEntity<> sayesinde hem Student'ları hem de Status Code'ları Repository'e göndermiş oluyoruz.
    @GetMapping // http://localhost:8080/students + GET
    public ResponseEntity<List<Student>> getAll() {
       List<Student> students =  studentService.getAll();

       return ResponseEntity.ok(students); //Students + HTTP.status code(200)

    }

    //!!! Create new student
    @PostMapping //http://localhost:8080/students + POST + Json
    public ResponseEntity<Map<String,String>> createStudent( @Valid @RequestBody Student student){
        //@Valid Student entity de olusturdugumuz validationları burada tekrar kontrol edilmesini saglarız. Bu sayede controller katmanında bunu valid etmis oluruz.
        //Bir hata varsa bunu  diğer katmanlara taşımadan controller katmanında bunu engellemiş oluruz.
        //@RequestBody jsonları studenta mappler tam tersi de olur.
        //Request gelirken bodysinde json bir data gelicek.Bu gelen datayı Student classına mapple demis oluyoruz.

        studentService.createStudent(student);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Student is created successfully");
        map.put("status", "True");

        return new ResponseEntity<>(map, HttpStatus.CREATED);

    }

    //!!! Get a student by ID via RequestParam -> http://localhost:8080/students/query?id=1
    @GetMapping("/query") //Aynı http methodlar var ise path eklemeliyiz.
    //(@RequestParam("id") Long id) string ifadeli id Long id parametresine set edilir.
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){

       Student student = studentService.findStudent(id);
       return ResponseEntity.ok(student); //return new ResponseEntity<>(map, HttpStatus.CREATED); soldakiyle aynı iş yapar.

    }

    //!!! Get a Student by ID via PathVariable
    @GetMapping("/{id}") //Path variable kullanıyorsak süslü parantez kullanılır.
    public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id ){
        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);

    }

    //!!! Delete Student with id ->http://localhost:8080/students/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") Long id){
        studentService.deleteStudent(id);

        Map<String,String> map = new HashMap<>();
        map.put("message", "Student is deleted successfuly");
        map.put("status", "true");


        return new ResponseEntity<>(map, HttpStatus.OK); // return ResponseEntity.ok(map);


    }

    // !!! Update Student

    @PutMapping("{id}") //http://localhost:8080/students/1 -> endpoint + id + JSON + HTTP-method
    //@PathVariable -> Sadece 1 data alıcaksak parantezle id olarak belirtmemize gerek yok.Spring boot bunu otomatik mappler.
    public ResponseEntity<Map<String,String>> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO){

        studentService.updateStudent(id,studentDTO);

        Map<String,String> map = new HashMap<>();
        map.put("message", "Student is updated successfuly");
        map.put("status", "true");


        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    //!!! Pageable şeklinde getall yapıcaz.//paging client-side(performansli degildir)---server-side(parcalama service'de yapilir, ağ yorulmaz)
    @GetMapping("/page") //http://localhost:8080/students/page?page=1&size=2&sort=name&direction=ASC
    public ResponseEntity<Page<Student>> getAllWithPage(
            @RequestParam("page") int page, //Kacıncı sayfa gelsin
            @RequestParam("size") int size, //Sayfa başı kaç urun?
            @RequestParam("sort") String prop, //Hangi field a göre sıralanıcak.
            @RequestParam("direction") Sort.Direction direction //Sıralama türü

    ){
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,prop));
        Page<Student> studentPage =  studentService.getAllWithPage(pageable);

        return ResponseEntity.ok(studentPage);

    }

    //!!! Get by Lastname
    @GetMapping("/querylastname")
    public ResponseEntity<List<Student>> getStudentByLastName(@RequestParam("lastname") String lastname){
       List<Student> list = studentService.findStudent(lastname);
       return ResponseEntity.ok(list);

    }



    // !!! get All Student By Grade ( JPQL )
    @GetMapping("/grade/{grade}") // http://localhost:8080/students/grade/75  + GET
    public ResponseEntity<List<Student>> getStudentsEqualsGrade(@PathVariable("grade") Integer grade) {

        List<Student> list = studentService.findAllEqualsGrade(grade);

        return ResponseEntity.ok(list);
    }


    // !!! DB den direk DTO olarak datami almak istersem ??
    @GetMapping("/query/dto") // http://localhost:8080/students/query/dto?id=1   + GET
    public ResponseEntity<StudentDTO> getStudentDTO(@RequestParam("id") Long id) {
        StudentDTO studentDTO = studentService.findStudentDTOById(id);

        return  ResponseEntity.ok(studentDTO);

    }

    //!!! view-LOGGER deneme
    @GetMapping("/welcome") //http://localhost:8080/students/welcome + GET
    //HttpServletRequest gelen request üzerinden herhangi bi dataya ulasmak istiyorsak.
    public String welcome(HttpServletRequest request){
        logger.warn("---------------Welcome{}", request.getServletPath()); //---------------Welcome/students/welcome
        return "Welcome to Student Controller";
    }









}
