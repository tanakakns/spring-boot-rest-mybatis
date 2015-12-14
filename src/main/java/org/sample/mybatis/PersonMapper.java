package org.sample.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sample.model.Person;

public interface PersonMapper {
    @Select("select * from person")
    List<Person> getPersons();

    @Select("select * from person where id = #{id}")
    Person getPerson(@Param("id") int id);

    @Insert("insert into person (id, name) values (#{id}, #{name})")
    int insertPerson(Person person);

    @Update("update person set name=#{name} where id=#{id}")
    int updatePerson(Person person);

    @Delete("delete from person where id = #{id}")
    int deletePerson(@Param("id") int id);
}
