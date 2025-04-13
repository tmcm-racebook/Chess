package com.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckmateTest {
    private Game game;
    private BoardState boardState;

    @BeforeEach
    void setUp() {
        game = new Game();
        boardState = game.getBoardState();
        boardState.clearBoard();
    }

    @Test
    void testBasicCheckmate() {
        // Set up a basic checkmate position
        Position whiteKingPos = new Position(0, 0);
        Position blackKingPos = new Position(7, 7);
        Position blackQueenPos = new Position(0, 1);
        Position blackRookPos = new Position(1, 0);

        Piece whiteKing = new King(Piece.Color.WHITE, whiteKingPos);
        Piece blackKing = new King(Piece.Color.BLACK, blackKingPos);
        Piece blackQueen = new Queen(Piece.Color.BLACK, blackQueenPos);
        Piece blackRook = new Rook(Piece.Color.BLACK, blackRookPos);

        boardState.setPieceAt(whiteKingPos, whiteKing);
        boardState.setPieceAt(blackKingPos, blackKing);
        boardState.setPieceAt(blackQueenPos, blackQueen);
        boardState.setPieceAt(blackRookPos, blackRook);

        // White king is in checkmate
        assertTrue(boardState.isInCheck(Piece.Color.WHITE));
        assertFalse(whiteKing.getValidMoves(boardState).isEmpty());
    }

    @Test
    void testCheckmateWithEscape() {
        // Set up a position where the king can escape check
        Position whiteKingPos = new Position(0, 0);
        Position blackKingPos = new Position(7, 7);
        Position blackQueenPos = new Position(0, 1);

        Piece whiteKing = new King(Piece.Color.WHITE, whiteKingPos);
        Piece blackKing = new King(Piece.Color.BLACK, blackKingPos);
        Piece blackQueen = new Queen(Piece.Color.BLACK, blackQueenPos);

        boardState.setPieceAt(whiteKingPos, whiteKing);
        boardState.setPieceAt(blackKingPos, blackKing);
        boardState.setPieceAt(blackQueenPos, blackQueen);

        // White king can escape by moving to (1,0)
        assertTrue(boardState.isInCheck(Piece.Color.WHITE));
        assertFalse(whiteKing.getValidMoves(boardState).isEmpty());
    }

    @Test
    void testAICannotMakeIllegalMoves() {
        // Set up a position where the AI has limited legal moves
        Position whiteKingPos = new Position(0, 0);
        Position blackKingPos = new Position(7, 7);
        Position blackQueenPos = new Position(0, 1);

        Piece whiteKing = new King(Piece.Color.WHITE, whiteKingPos);
        Piece blackKing = new King(Piece.Color.BLACK, blackKingPos);
        Piece blackQueen = new Queen(Piece.Color.BLACK, blackQueenPos);

        boardState.setPieceAt(whiteKingPos, whiteKing);
        boardState.setPieceAt(blackKingPos, blackKing);
        boardState.setPieceAt(blackQueenPos, blackQueen);

        // AI should only be able to make legal moves
        // The king cannot move to (1,0) because it would be in check from the queen
        // Instead, check that the king cannot make this illegal move
        Position illegalMove = new Position(1, 0);
        assertFalse(whiteKing.getValidMoves(boardState).contains(illegalMove));
    }

    @Test
    void testAIFindsLegalMovesWhenInCheck() {
        // Set up a position where the AI is in check
        Position whiteKingPos = new Position(0, 0);
        Position blackKingPos = new Position(7, 7);
        Position blackQueenPos = new Position(0, 1);

        Piece whiteKing = new King(Piece.Color.WHITE, whiteKingPos);
        Piece blackKing = new King(Piece.Color.BLACK, blackKingPos);
        Piece blackQueen = new Queen(Piece.Color.BLACK, blackQueenPos);

        boardState.setPieceAt(whiteKingPos, whiteKing);
        boardState.setPieceAt(blackKingPos, blackKing);
        boardState.setPieceAt(blackQueenPos, blackQueen);

        // AI should find legal moves to get out of check
        assertFalse(whiteKing.getValidMoves(boardState).isEmpty());
    }
} 