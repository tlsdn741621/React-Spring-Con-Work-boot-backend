package com.busanit501.api_rest_test_jwt_react.repository.search;

import com.busanit501.api_rest_test_jwt_react.dto.CursorPageRequestDTO;
import com.busanit501.api_rest_test_jwt_react.dto.PageRequestDTO;
import com.busanit501.api_rest_test_jwt_react.dto.TodoDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {
    Page<TodoDTO> list(
            PageRequestDTO pageRequestDTO);
    Page<TodoDTO> list2(CursorPageRequestDTO pageRequestDTO);
}