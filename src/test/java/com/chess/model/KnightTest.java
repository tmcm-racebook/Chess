package com.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class KnightTest {
    private BoardState boardState;
    private Knight whiteKnight;
    private Knight blackKnight;

    @BeforeEach
    void setUp() {
        boardState = new BoardState();
        whiteKnight = new Knight(Piece.Color.WHITE, new Position(4, 4)); // Center of the board
        blackKnight = new Knight(Piece.Color.BLACK, new Position(4, 4));
        boardState.setPieceAt(whiteKnight.getPosition(), whiteKnight);
    }

    @Test
    void testKnightValidMoves() {
        List<Position> moves = whiteKnight.getValidMoves(boardState);
        assertEquals(8, moves.size(), "Knight in center should have 8 possible moves");
        
        // Check all L-shaped moves
        assertTrue(moves.contains(new Position(6, 5)), "Should be able to move 2 up, 1 right");
        assertTrue(moves.contains(new Position(6, 3)), "Should be able to move 2 up, 1 left");
        assertTrue(moves.contains(new Position(2, 5)), "Should be able to move 2 down, 1 right");
        assertTrue(moves.contains(new Position(2, 3)), "Should be able to move 2 down, 1 left");
        assertTrue(moves.contains(new Position(5, 6)), "Should be able to move 1 up, 2 right");
        assertTrue(moves.contains(new Position(5, 2)), "Should be able to move 1 up, 2 left");
        assertTrue(moves.contains(new Position(3, 6)), "Should be able to move 1 down, 2 right");
        assertTrue(moves.contains(new Position(3, 2)), "Should be able to move 1 down, 2 left");
    }

    @Test
    void testKnightCannotCaptureOwnPiece() {
        // Place a white piece in one of the knight's possible moves
        boardState.setPieceAt(new Position(6, 5), new Pawn(Piece.Color.WHITE, new Position(6, 5)));
        List<Position> moves = whiteKnight.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(6, 5)), "Should not be able to capture own piece");
    }

    @Test
    void testKnightCanCaptureOpponentPiece() {
        // Place a black piece in one of the knight's possible moves
        boardState.setPieceAt(new Position(6, 5), new Pawn(Piece.Color.BLACK, new Position(6, 5)));
        List<Position> moves = whiteKnight.getValidMoves(boardState);
        assertTrue(moves.contains(new Position(6, 5)), "Should be able to capture opponent's piece");
    }

    @Test
    void testKnightEdgeOfBoardMoves() {
        // Place knight at edge of board
        whiteKnight.setPosition(new Position(0, 0));
        List<Position> moves = whiteKnight.getValidMoves(boardState);
        assertEquals(2, moves.size(), "Knight at corner should have 2 possible moves");
        assertTrue(moves.contains(new Position(2, 1)), "Should be able to move 2 down, 1 right");
        assertTrue(moves.contains(new Position(1, 2)), "Should be able to move 1 down, 2 right");
    }

    @Test
    void testKnightCannotJumpOverPieces() {
        // Place pieces around the knight
        boardState.setPieceAt(new Position(5, 5), new Pawn(Piece.Color.WHITE, new Position(5, 5)));
        boardState.setPieceAt(new Position(5, 3), new Pawn(Piece.Color.WHITE, new Position(5, 3)));
        List<Position> moves = whiteKnight.getValidMoves(boardState);
        // Knight can still move to all positions as it jumps over pieces
        assertEquals(8, moves.size(), "Knight should be able to jump over pieces");
    }
} 