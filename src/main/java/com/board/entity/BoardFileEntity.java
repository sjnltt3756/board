package com.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_entity")
public class BoardFileEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)  //boardEntity와 N:1 관계
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;    // 부모 Entity 타입

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName){
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setBoardEntity(boardEntity);
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        return boardFileEntity;
    }
}
