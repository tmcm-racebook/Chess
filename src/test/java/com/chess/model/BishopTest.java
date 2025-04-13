package com.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BishopTest {
    private BoardState boardState;
    private Bishop whiteBishop;
    private Bishop blackBishop;

    @BeforeEach
    void setUp() {
        boardState = new BoardState();
        whiteBishop = new Bishop(Piece.Color.WHITE, new Position(4, 4)); // Center of the board
        blackBishop = new Bishop(Piece.Color.BLACK, new Position(4, 4));
        boardState.setPieceAt(whiteBishop.getPosition(), whiteBishop);
    }

    @Test
    void testBishopValidMoves() {
        List<Position> moves = whiteBishop.getValidMoves(boardState);
        // Bishop in center should have moves in all four diagonal directions
        assertTrue(moves.contains(new Position(5, 5)), "Should be able to move down-right");
        assertTrue(moves.contains(new Position(5, 3)), "Should be able to move down-left");
        assertTrue(moves.contains(new Position(3, 5)), "Should be able to move up-right");
        assertTrue(moves.contains(new Position(3, 3)), "Should be able to move up-left");
    }

    @Test
    void testBishopCannotMoveThroughPieces() {
        // Place a piece in the bishop's path
        boardState.setPieceAt(new Position(5, 5), new Pawn(Piece.Color.WHITE, new Position(5, 5)));
        List<Position> moves = whiteBishop.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(6, 6)), "Should not be able to move through pieces");
    }

    @Test
    void testBishopCannotCaptureOwnPiece() {
        // Place a white piece in one of the bishop's possible moves
        boardState.setPieceAt(new Position(5, 5), new Pawn(Piece.Color.WHITE, new Position(5, 5)));
        List<Position> moves = whiteBishop.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(5, 5)), "Should not be able to capture own piece");
    }

    @Test
    void testBishopCanCaptureOpponentPiece() {
        // Place a black piece in one of the bishop's possible moves
        boardState.setPieceAt(new Position(5, 5), new Pawn(Piece.Color.BLACK, new Position(5, 5)));
        List<Position> moves = whiteBishop.getValidMoves(boardState);
        assertTrue(moves.contains(new Position(5, 5)), "Should be able to capture opponent's piece");
    }

    @Test
    void testBishopEdgeOfBoardMoves() {
        // Place bishop at corner of board
        whiteBishop.setPosition(new Position(0, 0));
        List<Position> moves = whiteBishop.getValidMoves(boardState);
        // Bishop at corner should only have moves in one diagonal direction
        assertTrue(moves.contains(new Position(1, 1)), "Should be able to move down-right");
        assertFalse(moves.contains(new Position(1, -1)), "Should not be able to move off the board");
    }

    @Test
    void testBishopBlockedByMultiplePieces() {
        // Place pieces blocking all four diagonal directions
        boardState.setPieceAt(new Position(5, 5), new Pawn(Piece.Color.WHITE, new Position(5, 5)));
        boardState.setPieceAt(new Position(5, 3), new Pawn(Piece.Color.WHITE, new Position(5, 3)));
        boardState.setPieceAt(new Position(3, 5), new Pawn(Piece.Color.WHITE, new Position(3, 5)));
        boardState.setPieceAt(new Position(3, 3), new Pawn(Piece.Color.WHITE, new Position(3, 3)));
        List<Position> moves = whiteBishop.getValidMoves(boardState);
        assertEquals(0, moves.size(), "Bishop should have no valid moves when completely blocked");
    }

    @Test
    void testBishopInvalidMoves() {
        // Set up a bishop in the center of the board
        Position bishopPos = new Position(4, 4);
        Bishop bishop = new Bishop(Piece.Color.WHITE, bishopPos);
        boardState.setPieceAt(bishopPos, bishop);

        // Try to make an invalid move (not diagonal)
        Position invalidPos = new Position(8, 4);
        List<Position> validMoves = bishop.getValidMoves(boardState);
        assertFalse(validMoves.contains(invalidPos), "Bishop should not be able to move to " + invalidPos);

        // Try to make another invalid move (diagonal but blocked)
        Position blockingPos = new Position(5, 5);
        boardState.setPieceAt(blockingPos, new Pawn(Piece.Color.WHITE, blockingPos));
        Position invalidPos2 = new Position(6, 6);
        validMoves = bishop.getValidMoves(boardState);
        assertFalse(validMoves.contains(invalidPos2), "Bishop should not be able to move through pieces");
    }
} 