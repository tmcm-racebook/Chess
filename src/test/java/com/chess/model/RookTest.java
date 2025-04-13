package com.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class RookTest {
    private BoardState boardState;
    private Rook whiteRook;
    private Rook blackRook;

    @BeforeEach
    void setUp() {
        boardState = new BoardState();
        whiteRook = new Rook(Piece.Color.WHITE, new Position(4, 4)); // Center of the board
        blackRook = new Rook(Piece.Color.BLACK, new Position(4, 4));
        boardState.setPieceAt(whiteRook.getPosition(), whiteRook);
    }

    @Test
    void testRookValidMoves() {
        List<Position> moves = whiteRook.getValidMoves(boardState);
        // Rook in center should have moves in all four directions
        assertTrue(moves.contains(new Position(5, 4)), "Should be able to move down");
        assertTrue(moves.contains(new Position(3, 4)), "Should be able to move up");
        assertTrue(moves.contains(new Position(4, 5)), "Should be able to move right");
        assertTrue(moves.contains(new Position(4, 3)), "Should be able to move left");
    }

    @Test
    void testRookCannotMoveThroughPieces() {
        // Place a piece in the rook's path
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.WHITE, new Position(5, 4)));
        List<Position> moves = whiteRook.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(6, 4)), "Should not be able to move through pieces");
    }

    @Test
    void testRookCannotCaptureOwnPiece() {
        // Place a white piece in one of the rook's possible moves
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.WHITE, new Position(5, 4)));
        List<Position> moves = whiteRook.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(5, 4)), "Should not be able to capture own piece");
    }

    @Test
    void testRookCanCaptureOpponentPiece() {
        // Place a black piece in one of the rook's possible moves
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.BLACK, new Position(5, 4)));
        List<Position> moves = whiteRook.getValidMoves(boardState);
        assertTrue(moves.contains(new Position(5, 4)), "Should be able to capture opponent's piece");
    }

    @Test
    void testRookEdgeOfBoardMoves() {
        // Place rook at corner of board
        whiteRook.setPosition(new Position(0, 0));
        List<Position> moves = whiteRook.getValidMoves(boardState);
        // Rook at corner should have moves in two directions
        assertTrue(moves.contains(new Position(1, 0)), "Should be able to move down");
        assertTrue(moves.contains(new Position(0, 1)), "Should be able to move right");
        assertFalse(moves.contains(new Position(-1, 0)), "Should not be able to move off the board");
        assertFalse(moves.contains(new Position(0, -1)), "Should not be able to move off the board");
    }

    @Test
    void testRookBlockedByMultiplePieces() {
        // Place pieces blocking all four directions
        boardState.setPieceAt(new Position(5, 4), new Pawn(Piece.Color.WHITE, new Position(5, 4)));
        boardState.setPieceAt(new Position(3, 4), new Pawn(Piece.Color.WHITE, new Position(3, 4)));
        boardState.setPieceAt(new Position(4, 5), new Pawn(Piece.Color.WHITE, new Position(4, 5)));
        boardState.setPieceAt(new Position(4, 3), new Pawn(Piece.Color.WHITE, new Position(4, 3)));
        List<Position> moves = whiteRook.getValidMoves(boardState);
        assertEquals(0, moves.size(), "Rook should have no valid moves when completely blocked");
    }

    @Test
    void testRookHasMovedFlag() {
        assertFalse(whiteRook.hasMoved(), "Rook should not have moved initially");
        whiteRook.setPosition(new Position(5, 4));
        assertTrue(whiteRook.hasMoved(), "Rook should have moved after position change");
    }
} 