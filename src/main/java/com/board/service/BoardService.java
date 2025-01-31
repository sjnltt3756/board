package com.board.service;

import com.board.dto.BoardDTO;
import com.board.entity.BoardEntity;
import com.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    // 수동적인 쿼리를 수행해야 하는 경우 일관성 유지를 위해 @Transactional를 사용
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> byId = boardRepository.findById(id);
        if (byId.isPresent()){
            BoardEntity boardEntity = byId.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else{
            return null;
        }
    }

    /*
        repository에 update 기능은 없다.
        save()로 insert와 update를 같이 사용
        구분 방법은 id가 있냐 없냐의 차이
        id값이 존재하면 update로 받아들임
     */

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }
}
