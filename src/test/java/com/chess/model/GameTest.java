package com.chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testInitialGameState() {
        assertEquals(Piece.Color.WHITE, game.getCurrentTurn());
        assertEquals(Game.GameState.IN_PROGRESS, game.getGameState());
        assertNull(game.getSelectedPosition());
        assertTrue(game.getValidMoves().isEmpty());
    }

    @Test
    void testSelectPiece() {
        // Select white pawn at e2
        Position pawnPos = new Position(6, 4);
        assertTrue(game.selectPosition(pawnPos));
        assertEquals(pawnPos, game.getSelectedPosition());
        assertFalse(game.getValidMoves().isEmpty());
    }

    @Test
    void testSelectOpponentPiece() {
        // Try to select black pawn at e7
        Position blackPawnPos = new Position(1, 4);
        assertFalse(game.selectPosition(blackPawnPos));
        assertNull(game.getSelectedPosition());
    }

    @Test
    void testValidPawnMove() {
        // Select and move white pawn from e2 to e4
        Position from = new Position(6, 4);
        Position to = new Position(4, 4);
        
        assertTrue(game.selectPosition(from));
        assertTrue(game.selectPosition(to));
        
        // Verify move was made
        assertNull(game.getBoardState().getPieceAt(from));
        Piece movedPiece = game.getBoardState().getPieceAt(to);
        assertNotNull(movedPiece);
        assertEquals(Piece.Type.PAWN, movedPiece.getType());
        assertEquals(Piece.Color.WHITE, movedPiece.getColor());
    }

    @Test
    void testInvalidPawnMove() {
        // Try to move white pawn from e2 to e5 (3 squares)
        Position from = new Position(6, 4);
        Position to = new Position(3, 4);
        
        assertTrue(game.selectPosition(from));
        assertFalse(game.selectPosition(to));
        
        // Verify pawn hasn't moved
        Piece piece = game.getBoardState().getPieceAt(from);
        assertNotNull(piece);
        assertEquals(Piece.Type.PAWN, piece.getType());
    }

    @Test
    void testUndoMove() {
        // Make a move and undo it
        Position from = new Position(6, 4);
        Position to = new Position(4, 4);
        
        game.selectPosition(from);
        game.selectPosition(to);
        
        assertTrue(game.canUndo());
        game.undoLastMove();
        
        // Verify piece is back in original position
        Piece piece = game.getBoardState().getPieceAt(from);
        assertNotNull(piece);
        assertEquals(Piece.Type.PAWN, piece.getType());
        assertEquals(Piece.Color.WHITE, piece.getColor());
        assertNull(game.getBoardState().getPieceAt(to));
    }

    @Test
    void testPawnCapture() {
        // Move white pawn to position for capture
        game.selectPosition(new Position(6, 4));
        game.selectPosition(new Position(4, 4));
        
        // Move black pawn to position to be captured
        game.selectPosition(new Position(1, 3));
        game.selectPosition(new Position(3, 3));
        
        // Perform capture
        game.selectPosition(new Position(4, 4));
        game.selectPosition(new Position(3, 3));
        
        // Verify capture
        Piece capturingPiece = game.getBoardState().getPieceAt(new Position(3, 3));
        assertNotNull(capturingPiece);
        assertEquals(Piece.Color.WHITE, capturingPiece.getColor());
    }

    @Test
    void testTurnOrder() {
        Position whiteFrom = new Position(6, 4);
        Position whiteTo = new Position(4, 4);
        
        // White's turn
        assertTrue(game.selectPosition(whiteFrom));
        assertTrue(game.selectPosition(whiteTo));
        assertEquals(Piece.Color.BLACK, game.getCurrentTurn());
        
        // Try to move white again
        assertFalse(game.selectPosition(new Position(6, 3)));
    }

    // @Test
    // void testMakeMove() {
    //     Position from = new Position(6, 4);  // e2
    //     Position to = new Position(4, 4);    // e4
        
    //     // Select piece first, then make move
    //     assertTrue(game.selectPosition(from));
    //     game.makeMove(from, to);
        
    //     // Verify move was made
    //     assertNull(game.getBoardState().getPieceAt(from));
    //     Piece movedPiece = game.getBoardState().getPieceAt(to);
    //     assertNotNull(movedPiece);
    //     assertEquals(Piece.Type.PAWN, movedPiece.getType());
    //     assertEquals(Piece.Color.BLACK, game.getCurrentTurn());
    // }
    
    
    @Test
    void testKnightMove() {
        Position from = new Position(7, 1);  // b1
        Position to = new Position(5, 2);    // c3
        
        assertTrue(game.selectPosition(from));
        assertTrue(game.selectPosition(to));
        
        // Verify knight moved correctly
        Piece knight = game.getBoardState().getPieceAt(to);
        assertNotNull(knight);
        assertEquals(Piece.Type.KNIGHT, knight.getType());
        assertEquals(Piece.Color.WHITE, knight.getColor());
    }

    @Test
    void testInvalidKnightMove() {
        Position from = new Position(7, 1);  // b1
        Position to = new Position(5, 1);    // b3 (straight line, invalid for knight)
        
        assertTrue(game.selectPosition(from));
        assertFalse(game.selectPosition(to));
        
        // Verify knight hasn't moved
        Piece knight = game.getBoardState().getPieceAt(from);
        assertNotNull(knight);
        assertEquals(Piece.Type.KNIGHT, knight.getType());
    }

    @Test
    void testBishopMove() {
        // First move pawn to allow bishop movement
        game.selectPosition(new Position(6, 3));  // d2
        game.selectPosition(new Position(4, 3));  // d4
        
        // Now move bishop
        Position from = new Position(7, 2);  // c1
        Position to = new Position(5, 4);    // e3
        
        assertTrue(game.selectPosition(from));
        assertTrue(game.selectPosition(to));
        
        // Verify bishop moved correctly
        Piece bishop = game.getBoardState().getPieceAt(to);
        assertNotNull(bishop);
        assertEquals(Piece.Type.BISHOP, bishop.getType());
    }

    @Test
    void testRookMove() {
        // First move pawn to allow rook movement
        game.selectPosition(new Position(6, 0));  // a2
        game.selectPosition(new Position(4, 0));  // a4
        
        // Now move rook
        Position from = new Position(7, 0);  // a1
        Position to = new Position(5, 0);    // a3
        
        assertTrue(game.selectPosition(from));
        assertTrue(game.selectPosition(to));
        
        // Verify rook moved correctly
        Piece rook = game.getBoardState().getPieceAt(to);
        assertNotNull(rook);
        assertEquals(Piece.Type.ROOK, rook.getType());
    }

    @Test
    void testQueenMove() {
        // First move pawn to allow queen movement
        game.selectPosition(new Position(6, 3));  // d2
        game.selectPosition(new Position(4, 3));  // d4
        
        // Now move queen diagonally
        Position from = new Position(7, 3);  // d1
        Position to = new Position(5, 5);    // f3
        
        assertTrue(game.selectPosition(from));
        assertTrue(game.selectPosition(to));
        
        // Verify queen moved correctly
        Piece queen = game.getBoardState().getPieceAt(to);
        assertNotNull(queen);
        assertEquals(Piece.Type.QUEEN, queen.getType());
    }

    @Test
    void testPieceBlocking() {
        // Try to move bishop without moving pawn first
        Position from = new Position(7, 2);  // c1
        Position to = new Position(5, 4);    // e3
        
        assertTrue(game.selectPosition(from));
        assertFalse(game.selectPosition(to));
        
        // Verify bishop hasn't moved
        Piece bishop = game.getBoardState().getPieceAt(from);
        assertNotNull(bishop);
        assertEquals(Piece.Type.BISHOP, bishop.getType());
    }
} 