package com.busanit501.api_rest_test_jwt_react.service;

import com.busanit501.api_rest_test_jwt_react.domain.Todo;
import com.busanit501.api_rest_test_jwt_react.dto.*;
import com.busanit501.api_rest_test_jwt_react.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Long register(TodoDTO todoDTO) {
        Todo todo = modelMapper.map(todoDTO, Todo.class); // 오타 수정
        Long tno = todoRepository.save(todo).getTno(); // getTno() 오타 수정
        return tno;
    }

    @Override
    public TodoDTO read(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow(); // 예외 발생 시 자동으로 NoSuchElementException 던짐
        return modelMapper.map(todo, TodoDTO.class);
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        Page<TodoDTO> result = todoRepository.list(pageRequestDTO);
        return PageResponseDTO.<TodoDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total((int) result.getTotalElements())
                .build();
    }
    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public void modify(TodoDTO todoDTO) {
        Optional<Todo> result = todoRepository.findById(todoDTO.getTno());
        Todo todo = result.orElseThrow();

        todo.changeTitle(todoDTO.getTitle());
        todo.changeDueDate(todoDTO.getDueDate());
        todo.changeComplete(todoDTO.isComplete());

        todoRepository.save(todo);
    }

    // 검색 적용 후, 커서 기반 페이지 네이션 코드,
    @Override
    public CursorPageResponseDTO<TodoDTO> list2(CursorPageRequestDTO pageRequestDTO) {


        // ✅ QueryDSL 기반 검색 메서드 호출
        Page<TodoDTO> pageResult = todoRepository.list2(pageRequestDTO);

        List<TodoDTO> dtoList = pageResult.getContent();

        // ✅ 다음 페이지의 커서 설정 (마지막 데이터의 tno 사용)
        Long nextCursor = dtoList.isEmpty() ? null : dtoList.get(dtoList.size() - 1).getTno();

        // ✅ 다음 데이터 존재 여부 확인
        boolean hasNext = dtoList.size() == pageRequestDTO.getSize(); // 받아온 데이터 크기가 요청한 size와 같으면 다음 데이터가 있음

        return CursorPageResponseDTO.<TodoDTO>builder()
                .dtoList(dtoList)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .total((int) pageResult.getTotalElements()) // ✅ 전체 개수 반환
                .build();
    }

    // ✅ 가장 큰 tno 값을 가져오는 메서드
    public Long getMaxTno() {
        return todoRepository.findMaxTno().orElse(0L); // 데이터가 없으면 기본값 0L 반환
    }

}
