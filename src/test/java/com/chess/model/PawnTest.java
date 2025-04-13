package com.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PawnTest {
    private BoardState boardState;
    private Pawn whitePawn;
    private Pawn blackPawn;

    @BeforeEach
    void setUp() {
        boardState = new BoardState();
        whitePawn = new Pawn(Piece.Color.WHITE, new Position(6, 0)); // Starting position for white pawn
        blackPawn = new Pawn(Piece.Color.BLACK, new Position(1, 0)); // Starting position for black pawn
        boardState.setPieceAt(whitePawn.getPosition(), whitePawn);
        boardState.setPieceAt(blackPawn.getPosition(), blackPawn);
    }

    @Test
    void testInitialWhitePawnMoves() {
        List<Position> moves = whitePawn.getValidMoves(boardState);
        assertEquals(2, moves.size(), "White pawn should have 2 valid moves from starting position");
        assertTrue(moves.contains(new Position(5, 0)), "Should be able to move one square forward");
        assertTrue(moves.contains(new Position(4, 0)), "Should be able to move two squares forward");
    }

    @Test
    void testInitialBlackPawnMoves() {
        List<Position> moves = blackPawn.getValidMoves(boardState);
        assertEquals(2, moves.size(), "Black pawn should have 2 valid moves from starting position");
        assertTrue(moves.contains(new Position(2, 0)), "Should be able to move one square forward");
        assertTrue(moves.contains(new Position(3, 0)), "Should be able to move two squares forward");
    }

    @Test
    void testPawnCannotMoveMoreThanOneSquareAfterFirstMove() {
        // Move white pawn one square forward
        whitePawn.setPosition(new Position(5, 0));
        List<Position> moves = whitePawn.getValidMoves(boardState);
        assertEquals(1, moves.size(), "Pawn should only have one valid move after first move");
        assertTrue(moves.contains(new Position(4, 0)), "Should only be able to move one square forward");
        assertFalse(moves.contains(new Position(3, 0)), "Should not be able to move two squares forward after first move");
    }

    @Test
    void testPawnDiagonalCapture() {
        // Place a black piece diagonally in front of white pawn
        boardState.setPieceAt(new Position(5, 1), new Pawn(Piece.Color.BLACK, new Position(5, 1)));
        List<Position> moves = whitePawn.getValidMoves(boardState);
        assertTrue(moves.contains(new Position(5, 1)), "Should be able to capture diagonally");
    }

    @Test
    void testPawnCannotCaptureOwnPiece() {
        // Place a white piece diagonally in front of white pawn
        boardState.setPieceAt(new Position(5, 1), new Pawn(Piece.Color.WHITE, new Position(5, 1)));
        List<Position> moves = whitePawn.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(5, 1)), "Should not be able to capture own piece");
    }

    @Test
    void testPawnCannotMoveForwardIfBlocked() {
        // Place a piece directly in front of white pawn
        boardState.setPieceAt(new Position(5, 0), new Pawn(Piece.Color.BLACK, new Position(5, 0)));
        List<Position> moves = whitePawn.getValidMoves(boardState);
        assertFalse(moves.contains(new Position(5, 0)), "Should not be able to move forward if blocked");
        assertFalse(moves.contains(new Position(4, 0)), "Should not be able to move two squares if blocked");
    }

    @Test
    void testPawnEnPassant() {
        // Set up en passant scenario
        whitePawn.setPosition(new Position(3, 0));
        Pawn blackPawn = new Pawn(Piece.Color.BLACK, new Position(1, 1));
        boardState.setPieceAt(blackPawn.getPosition(), blackPawn);
        
        // Move black pawn two squares forward
        blackPawn.setPosition(new Position(3, 1));
        
        List<Position> moves = whitePawn.getValidMoves(boardState);
        assertTrue(moves.contains(new Position(2, 1)), "Should be able to capture en passant");
    }
} 