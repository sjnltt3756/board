package com.board.controller;

import com.board.dto.BoardDTO;
import com.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/")
    public String findAll(Model model){     // 데이터를 가져올 때 Model 객체를 사용한다.
        // DB에서 전체 데이터 게시글을 가져와서 index.html에 보여준다.
        List<BoardDTO> boardDTOList =  boardService.findAll();  // boardDto 객체가 담겨있는 List를 서비스단에서 찾고
        // 가져온 데이터를 모델 객체 담는다.
        model.addAttribute("boardList",boardDTOList);   // "boardList"라는 이름으로 boardDTOList 객체를 담는 코드 (타임리프 사용)

        return "list";
    }
}
