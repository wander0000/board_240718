package com.boot.board_240718.repository;

import com.boot.board_240718.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
    //페이징처리하면서 검색 단어 처리도 하려면
    Page<Board> findByTitleContainingOrContent(String title, String content, Pageable pageable);

}
