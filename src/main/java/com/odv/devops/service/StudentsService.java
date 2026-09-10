package com.odv.devops.service;

import java.util.List;
import java.util.Optional;

import com.odv.devops.entitie.Students;
import com.odv.devops.repositorie.StudentsRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentsService {

    private final StudentsRepository studentsRepository;

    public StudentsService(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    public List<Students> getAll() {
        return studentsRepository.findAll();
    }

    public Students create(Students student) {
        return studentsRepository.save(student);
    }

    public Optional<Students> update(Long id, Students student) {
        return studentsRepository.findById(id).map(existingStudent -> {
            existingStudent.setNom(student.getNom());
            existingStudent.setPrenom(student.getPrenom());
            existingStudent.setDateN(student.getDateN());
            return studentsRepository.save(existingStudent);
        });
    }

    public boolean delete(Long id) {
        if (!studentsRepository.existsById(id)) {
            return false;
        }

        studentsRepository.deleteById(id);
        return true;
    }
}
