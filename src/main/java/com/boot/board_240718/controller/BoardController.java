package com.boot.board_240718.controller;

import com.boot.board_240718.model.Board;
import com.boot.board_240718.repository.BoardRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boards = boardRepository.findAll();//전체 조회
        model.addAttribute("boards",boards);//게시글들을 모델에 담아줌

        return "board/list";

    }
    @GetMapping("/form")
//    public String form(Model model) {
    public String form(Model model, @RequestParam(required = false) Long id) {
        log.info("@# GetMapping form()");
        log.info("@# id =>" +id);

        if(id != null){//만약 파라미터에 id값이 있으면
//            Optional<Board> board = boardRepository.findById(id);
            Board board = boardRepository.findById(id).orElse(null);//아이디로 찾아서 보드로 받기
            log.info("@# board =>" +board);
            model.addAttribute("board",board);//모델에 담기
        }else {
            model.addAttribute("board", new Board());//id 없으면 깡통상태 Board를 들고감
        }
        return "board/form";
    }

    @PostMapping("/form")
//    public String form(@ModelAttribute Board board, Model model) {
    public String form(@Valid Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {//유효성 검사 : 게시글제목에 notnull을 걸어놨는데 값이 없이 넘어오면
            return "board/form"; //다시 글쓰기로 보냄
        }
        boardRepository.save(board);//레파지토리 이용해서 바로 저장

        return "redirect:/board/list";
    }
}
