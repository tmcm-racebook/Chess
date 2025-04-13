package com.chess.model;

import java.util.ArrayList;
import java.util.List;

public class BoardState {
    private static final int BOARD_SIZE = 8;
    private final Piece[][] board;

    public BoardState() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize white pieces
        board[7][0] = new Rook(Piece.Color.WHITE, new Position(7, 0));
        board[7][1] = new Knight(Piece.Color.WHITE, new Position(7, 1));
        board[7][2] = new Bishop(Piece.Color.WHITE, new Position(7, 2));
        board[7][3] = new Queen(Piece.Color.WHITE, new Position(7, 3));
        board[7][4] = new King(Piece.Color.WHITE, new Position(7, 4));
        board[7][5] = new Bishop(Piece.Color.WHITE, new Position(7, 5));
        board[7][6] = new Knight(Piece.Color.WHITE, new Position(7, 6));
        board[7][7] = new Rook(Piece.Color.WHITE, new Position(7, 7));

        // Initialize white pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[6][col] = new Pawn(Piece.Color.WHITE, new Position(6, col));
        }

        // Initialize black pieces
        board[0][0] = new Rook(Piece.Color.BLACK, new Position(0, 0));
        board[0][1] = new Knight(Piece.Color.BLACK, new Position(0, 1));
        board[0][2] = new Bishop(Piece.Color.BLACK, new Position(0, 2));
        board[0][3] = new Queen(Piece.Color.BLACK, new Position(0, 3));
        board[0][4] = new King(Piece.Color.BLACK, new Position(0, 4));
        board[0][5] = new Bishop(Piece.Color.BLACK, new Position(0, 5));
        board[0][6] = new Knight(Piece.Color.BLACK, new Position(0, 6));
        board[0][7] = new Rook(Piece.Color.BLACK, new Position(0, 7));

        // Initialize black pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = new Pawn(Piece.Color.BLACK, new Position(1, col));
        }
    }

    public Piece getPieceAt(Position position) {
        return board[position.getRow()][position.getCol()];
    }

    public void setPieceAt(Position position, Piece piece) {
        board[position.getRow()][position.getCol()] = piece;
        if (piece != null) {
            piece.setPosition(position);
        }
    }

    public boolean isValidPosition(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    public void movePiece(Position from, Position to) {
        Piece piece = getPieceAt(from);
        if (piece != null) {
            setPieceAt(to, piece);
            setPieceAt(from, null);
        }
    }

    public boolean isInCheck(Piece.Color color) {
        Position kingPosition = findKingPosition(color);
        if (kingPosition == null) return false;

        // Check if any opponent piece can capture the king
        Piece.Color opponentColor = color == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == opponentColor) {
                    if (piece.getValidMoves(this).contains(kingPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Piece.Color color) {
        if (!isInCheck(color)) return false;

        // Check if any piece of the given color can make a move that gets out of check
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color) {
                    for (Position move : piece.getValidMoves(this)) {
                        // Simulate the move
                        Piece capturedPiece = getPieceAt(move);
                        movePiece(piece.getPosition(), move);
                        boolean stillInCheck = isInCheck(color);
                        // Undo the move
                        movePiece(move, piece.getPosition());
                        setPieceAt(move, capturedPiece);
                        if (!stillInCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isStalemate(Piece.Color color) {
        if (isInCheck(color)) return false;

        // Check if any piece of the given color can make a legal move
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color) {
                    for (Position move : piece.getValidMoves(this)) {
                        // Simulate the move
                        Piece capturedPiece = getPieceAt(move);
                        movePiece(piece.getPosition(), move);
                        boolean stillInCheck = isInCheck(color);
                        // Undo the move
                        movePiece(move, piece.getPosition());
                        setPieceAt(move, capturedPiece);
                        if (!stillInCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private Position findKingPosition(Piece.Color color) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color && piece.getType() == Piece.Type.KING) {
                    return piece.getPosition();
                }
            }
        }
        return null;
    }

    public List<Piece> getPieces(Piece.Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPieceAt(new Position(row, col));
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public void clearBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
    }
} 