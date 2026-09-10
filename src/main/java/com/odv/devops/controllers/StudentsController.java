package com.odv.devops.controllers;

import java.util.List;

import com.odv.devops.entitie.Students;
import com.odv.devops.service.StudentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentsController {

    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    public List<Students> getAll() {
        return studentsService.getAll();
    }

    @PostMapping
    public ResponseEntity<Students> create(@RequestBody Students student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentsService.create(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Students> update(@PathVariable Long id, @RequestBody Students student) {
        return studentsService.update(id, student)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!studentsService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
