package com.board.service;

import com.board.dto.BoardDTO;
import com.board.entity.BoardEntity;
import com.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// DTO -> Entity (Entity Class)
// Entity -> Dto (DTO Class)

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {
        BoardEntity saveEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(saveEntity);
    }

    public List<BoardDTO> findAll() {
        //repository에서 무언가 가져올 때는 대부분 Entity로 온다.
        List<BoardEntity> boardEntityList = boardRepository.findAll();  // List형태의 Entity가 넘어온다.
        //  Entity로 넘어온 객체를 Dto로 옮겨담아서 컨트롤러로 반환한다.
        List<BoardDTO> boardDTOList = new ArrayList<>();
        // EntityList를 DtoList에 하나씩 담는다.
        for (BoardEntity boardEntity : boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        // EntityList를 DtoList에 담고 컨트롤러로 리턴
        return boardDTOList;
    }
}
