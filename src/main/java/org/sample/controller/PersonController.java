package org.sample.controller;

import java.util.List;

import org.sample.model.Person;
import org.sample.mybatis.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/persons")
public class PersonController {

    @Autowired
    protected PersonMapper personMapper;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Person>> getAll() throws Exception {
        HttpStatus status = HttpStatus.OK;
        List<Person> result = personMapper.getPersons();
        if(result == null || result.size() < 1) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<List<Person>>(result, status);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ResponseEntity<Person> get(@PathVariable("id") int id) throws Exception {
        Person result = personMapper.getPerson(id);
        HttpStatus status = HttpStatus.OK;
        if(result == null) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<Person>(result, status);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Person> insert(@RequestBody Person person) throws Exception {
        personMapper.insertPerson(person);
        return new ResponseEntity<Person>(person, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public ResponseEntity<Person> update(@PathVariable("id") int id, @RequestBody Person person) throws Exception {
        person.setId(id);
        int result = personMapper.updatePerson(person);
        if(result == 0)
            return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Person>(HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Person> delete(@PathVariable("id") int id) throws Exception {
        int result = personMapper.deletePerson(id);
        if(result == 0)
            return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Person>(HttpStatus.OK);
    }

    // MyBatisで発生する例外はSpringのDataAccessException（のサブクラス）へマッピングされる
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Person> exceptionHandler(Exception e) {
        if (e instanceof DuplicateKeyException) { // PrimaryKeyが重複したInsert
            return new ResponseEntity<Person>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<Person>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
