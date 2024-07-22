package com.boot.board_240718.controller;

import com.boot.board_240718.model.Board;
import com.boot.board_240718.repository.BoardRepository;
import com.boot.board_240718.validator.BoardValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
//    public String list(Model model) {
//    public String list(Model model, Pageable pageable) {
//    public String list(Model model, @PageableDefault(size = 2) Pageable pageable) {//검색기능 넣기전 페이징처리
//    public String list(Model model, @PageableDefault(size = 2) Pageable pageable, @RequestParam String searchText) {//페이지 요청시에는 searchText가 없는 상태이므로 오류남
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                                    @RequestParam(required = false, defaultValue = "") String searchText) {//
        log.info("@# list() ");
        log.info("@# /list searchText "+searchText);
//        List<Board> boards = boardRepository.findAll();//전체 조회
//        Page<Board> boards = boardRepository.findAll(PageRequest.of(1, 20));
//        Page<Board> boards = boardRepository.findAll(PageRequest.of(0, 20));
//        Page<Board> boards = boardRepository.findAll(pageable); 검색넣기 전 페이징처리
        Page<Board> boards = boardRepository.findByTitleContainingOrContent(searchText, searchText, pageable);
//        int startPage = Math.max(0, boards.getPageable().getPageNumber()-4);//시작페이지(5개 이전페이지 표시) -> 현재페이지-4
        int startPage = Math.max(1, boards.getPageable().getPageNumber()-4);//시작페이지(5개 이전페이지 표시) -> 현재페이지-4
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber()+4);//끝페이지(현재페이지의 5개 이후페이지 표시)


//        log.info("@# boards =>" +boards);
//        boards.getTotalElements(); //총 건수

        model.addAttribute("boards",boards);//게시글들을 모델에 담아줌
        model.addAttribute("startPage",startPage);//첫페이지
        model.addAttribute("endPage",endPage);//끝페이지

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
        boardValidator.validate(board, bindingResult);//게시글 입력할 때 유효성 검사

        if (bindingResult.hasErrors()) {//유효성 검사 : 게시글제목에 notnull을 걸어놨는데 값이 없이 넘어오면
            return "board/form"; //다시 글쓰기로 보냄
        }
        boardRepository.save(board);//레파지토리 이용해서 바로 저장

        return "redirect:/board/list";
    }
}
