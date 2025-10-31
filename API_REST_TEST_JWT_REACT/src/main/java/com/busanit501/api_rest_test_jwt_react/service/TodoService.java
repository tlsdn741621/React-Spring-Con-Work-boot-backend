package com.busanit501.api_rest_test_jwt_react.service;

import com.busanit501.api_rest_test_jwt_react.dto.*;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface TodoService {
    Long register(TodoDTO todoDTO);
    TodoDTO read(Long tno);
    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);
    void remove(Long tno);
    void modify(TodoDTO todoDTO);
    CursorPageResponseDTO<TodoDTO> list2(CursorPageRequestDTO pageRequestDTO);
    Long getMaxTno();
}