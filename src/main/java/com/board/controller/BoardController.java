package com.board.controller;

import com.board.dto.BoardDTO;
import com.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
        /*
            해당 게시글의 조회수를 하나 올리고,
            게시글 데이터를 가져와서 detail.html에 출력
         */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "detail";
    }

    @GetMapping("update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model){
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", boardDTO);
        return "detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model){     // @RequestParam을 이용해도 되지만 SpringBoot에서 제공하는 방식을 활용
//        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
    }
}
