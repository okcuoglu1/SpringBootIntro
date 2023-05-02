package com.tpe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //Dolasan json datada sadece jsonda Key deger, bookName yapar. Sadece okunurlugu arttırmak icin.
    @JsonProperty("bookName")
    private String name;

    @JsonIgnore //Hibernatedeki toStringdeki aynı classlar birbirine gidip durdugu icin stackoverflow alırdık;
    //Aynı hatayı burda da alıyoruz.Bunu cözmek icin @JsonIgnore Bu classa 2.kere gelme demiş olduk.
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    //Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Student getStudent() {
        return student;
    }



}
