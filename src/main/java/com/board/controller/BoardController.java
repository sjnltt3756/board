package com.board.controller;

import com.board.dto.BoardDTO;
import com.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String saveFrom(){
        return "save";
    }
    // 동일한 주소여도 post와 get일 경우에는 가능하지만 같은 mapping이라면 Exception 발생
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }
}
