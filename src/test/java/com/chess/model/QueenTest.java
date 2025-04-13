package com.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class QueenTest {
    private BoardState boardState;
    private Queen whiteQueen;
    private Queen blackQueen;

    @BeforeEach
    void setUp() {
        boardState = new BoardState();
        whiteQueen = new Queen(Piece.Color.WHITE, new Position(4, 4)); // Center of the board
        blackQueen = new Queen(Piece.Color.BLACK, new Position(4, 4));
        boardState.setPieceAt(whiteQueen.getPosition(), whiteQueen);
    }

    @Test
    void testQueenValidMoves() {
        List<Position> moves = whiteQueen.getValidMoves(boardState);
        // Queen in center should have moves in all eight directions
        // Horizontal and vertical
        assertTrue(moves.contains(new Position(5, 4)), "Should be able to move down");
        assertTrue(moves.contains(new Position(3, 4)), "Should be able to move up");
        assertTrue(moves.contains(new Position(4, 5)), "Should be able to move right");
        assertTrue(moves.contains(new Position(4, 3)), "Should be able to move left");
        // Diagonal
        assertTrue(moves.contains(new Position(5, 5)), "Should be able to move down-right");
        assertTrue(moves.contains(new Position(5, 3)), "Should be able to move down-left");
        assertTrue(moves.contains(new Position(3, 5)), "Should be able to move up-right");
        assertTrue(moves.contains(new Position(3, 3)), "Should be able to move up-left");
    }

    @Test
    void testQueenCannotMoveThroughPieces() {
        // Place a piece in the queen's path
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.WHITE, new Position(5, 4)));
        List<Position> moves = whiteQueen.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(6, 4)), "Should not be able to move through pieces");
    }

    @Test
    void testQueenCannotCaptureOwnPiece() {
        // Place a white piece in one of the queen's possible moves
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.WHITE, new Position(5, 4)));
        List<Position> moves = whiteQueen.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(5, 4)), "Should not be able to capture own piece");
    }

    @Test
    void testQueenCanCaptureOpponentPiece() {
        // Place a black piece in one of the queen's possible moves
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.BLACK, new Position(5, 4)));
        List<Position> moves = whiteQueen.getValidMoves(boardState);
        assertTrue(moves.contains(new Position(5, 4)), "Should be able to capture opponent's piece");
    }

    @Test
    void testQueenEdgeOfBoardMoves() {
        // Place queen at corner of board
        whiteQueen.setPosition(new Position(0, 0));
        List<Position> moves = whiteQueen.getValidMoves(boardState);
        // Queen at corner should have moves in three directions
        assertTrue(moves.contains(new Position(1, 0)), "Should be able to move down");
        assertTrue(moves.contains(new Position(0, 1)), "Should be able to move right");
        assertTrue(moves.contains(new Position(1, 1)), "Should be able to move down-right");
        assertFalse(moves.contains(new Position(-1, 0)), "Should not be able to move off the board");
        assertFalse(moves.contains(new Position(0, -1)), "Should not be able to move off the board");
    }

    @Test
    void testQueenBlockedByMultiplePieces() {
        // Place pieces blocking all eight directions
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.WHITE, new Position(5, 4)));
        boardState.setPieceAt(new Position(3, 4), new Pawn(Piece.Color.WHITE, new Position(3, 4)));
        boardState.setPieceAt(new Position(4, 5), new Pawn(Piece.Color.WHITE, new Position(4, 5)));
        boardState.setPieceAt(new Position(4, 3), new Pawn(Piece.Color.WHITE, new Position(4, 3)));
        boardState.setPieceAt(new Position(5, 5), new Pawn(Piece.Color.WHITE, new Position(5, 5)));
        boardState.setPieceAt(new Position(5, 3), new Pawn(Piece.Color.WHITE, new Position(5, 3)));
        boardState.setPieceAt(new Position(3, 5), new Pawn(Piece.Color.WHITE, new Position(3, 5)));
        boardState.setPieceAt(new Position(3, 3), new Pawn(Piece.Color.WHITE, new Position(3, 3)));
        List<Position> moves = whiteQueen.getValidMoves(boardState);
        assertEquals(0, moves.size(), "Queen should have no valid moves when completely blocked");
    }
} 