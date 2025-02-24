package com.board.service;

import com.board.dto.BoardDTO;
import com.board.entity.BoardEntity;
import com.board.entity.BoardFileEntity;
import com.board.repository.BoardFileRepository;
import com.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class)
// Entity -> Dto (DTO Class)

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()){
            // 파일이 없는 경우
            BoardEntity saveEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(saveEntity);
        }else{
            // 파일이 있는 경우
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일 이름을 가져옴
                3. 서버 저장용 이름을 추가
                 - 내사진.jpg -> 87987897_내사진.jpg 겹치면 안되는 값
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */
            MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
            String originalFilename = boardFile.getOriginalFilename(); // 2.
            // java에서 제공하는 UUID를 사용하기도 한다.
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
            String savePath = "/Users/jiminsu/Documents/" + storedFileName; // 4. /Users/사용자이름/87987897_내사진.img
            boardFile.transferTo(new File(savePath)); // 5.
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();
            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity);
        }

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


    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber()-1;  // 몇 페이지를 보고싶은지
        int pageLimit = 3;  // 한 페이지에 몇개씩 볼건지
        Page<BoardEntity> boardEntities =
                // 보고싶은 페이지 인덱스 , 페이지 당 글 갯수 , id기준 내림차순 정렬
                // 한 페이지당 3개씩 id기준 내림차순 정렬
                // page 위치에 있는 값은 0부터 시작하기 때문에 사용자로부터 받은 pageNumber에서 -1을 해줘야 올바른 데이터를 가져올 수 있다.
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록 : id, 작성자, 제목, 조회수, 작성시간 <- 이걸 담을 dto 생성자 추가하기
        Page<BoardDTO> boardDTOS =
                boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(),board.getBoardHits(),board.getCreatedTime()));  // board 는 entity객체의 매개변수, DTO로 옮겨닮는 작업
        return boardDTOS;
    }
}
