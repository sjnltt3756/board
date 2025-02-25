package com.board.dto;

import com.board.entity.BoardEntity;
import com.board.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DTO(Data Transfer Object), VO, Bean -> 데이터를 전송할 때 사용하는 객체
@Getter
@Setter
@ToString
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {

    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits; // 게시글 조회수
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdateTime;

    //private MultipartFile boardFile;    // MultipartFile 타입으로 save.html -> controller 파일을 담는다.
    private List<MultipartFile> boardFile; // 다중파일 첨부할 때는 List<>를 사용한다   // MultipartFile 타입으로 save.html -> controller 파일을 담는다.
    //private String originalFileName;    // 원본 단일 파일 이름
    private List<String> originalFileName;    // 원본 다중 첨부 파일 이름
    //private String storedFileName;      // 서버 저장용 단일 파일 이름
    private List<String> storedFileName;      // 서버 저장용 다중 첨부 파일 이름
    private int fileAttached;           // 파일 첨부 여부(첨부 1, 미첨부 0)

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdateTime(boardEntity.getUpdatedTime());
        if(boardEntity.getFileAttached()==0){
            boardDTO.setFileAttached(boardEntity.getFileAttached());    // 0
        }else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached());    // 1
            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntities()) {
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
                // 파일 이름을 가져가야 함
                // originalFileName, StoredFileName : board_file_table (BoardFileEntity)
                // join
                // select * from board_table b, board_file_table bf where b.id = bf.board_id and where b.id = ?
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }
        return boardDTO;
    }
}
