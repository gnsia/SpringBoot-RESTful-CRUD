package com.example.demo.persistence;

import com.example.demo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    // TodoEntity의 userId라는 멤버가 테이블의 컬럼이 될때 user_id로 자동으로 변환하는 패턴있다.
    @Query(value = "select * from Todo t where t.user_id = ?1", nativeQuery = true)
    List<TodoEntity> findByUserId(String userId);
}
