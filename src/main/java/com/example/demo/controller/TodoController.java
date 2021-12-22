package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; //임시 유저 아이디 생성

            //(1) TodoEntity로 변환한다
            TodoEntity entity = TodoDTO.toEntity(dto);

            //(2) id를 null로 초기화한다. 생성 당시에는 id값이 없어야 하기때문
            entity.setId(null);

            //(3) 임시 사용자 아이디를 설정해준다. 추후 인증과 인가 과정에서 수정
            entity.setUserId(temporaryUserId);

            //(4) 서비스를 이용해 TodoEntity 생성
            List<TodoEntity> entities = service.create(entity);

            //(5) 자바 스트림을 이용해 리턴된 TodoEntity 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //(6) 변환된 TodoDTO 리스트를 이용하여 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //(7) ResponseDTO를 반환
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user";

        //(1) 서비스 메소드의 retirieve() 메서드를 사용해 Todo리스트를 가져온다.
        List<TodoEntity> entities = service.retrieve(temporaryUserId);

        //(2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        //(3) 변환되 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //(4) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user";
        // (1) dto를 entity로 변환한다.
        TodoEntity entity = TodoDTO.toEntity(dto);

        // (2) id를 temporaryUserId로 초기화한다. 추후 수정
        entity.setUserId(temporaryUserId);

        // (3) 서비스를 이용해 entity를 업데이트
        List<TodoEntity> entities = service.update(entity);

        // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // (6) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            //(1) TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            //(2) 임시 사용자 아이디를 설정해 준다.
            entity.setUserId(temporaryUserId);

            //(3) service를 이용해 entity를 삭제한다.
            List<TodoEntity> entities = service.delete(entity);

            //(4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //(5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //(6) ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            //(7) 혹시 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
