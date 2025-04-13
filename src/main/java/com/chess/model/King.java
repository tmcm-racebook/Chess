package com.chess.model;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean hasMoved;

    public King(Color color, Position position) {
        super(color, Type.KING, position);
        hasMoved = false;
    }

    @Override
    public List<Position> getValidMoves(BoardState boardState) {
        List<Position> moves = new ArrayList<>();
        Position pos = getPosition();
        
        // All eight possible moves for a king
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
        for (int[] dir : directions) {
            int newRow = pos.getRow() + dir[0];
            int newCol = pos.getCol() + dir[1];
            Position newPos = new Position(newRow, newCol);
            
            if (boardState.isValidPosition(newPos) && !wouldPutKingInCheck(newPos, boardState)) {
                Piece pieceAtTarget = boardState.getPieceAt(newPos);
                if (pieceAtTarget == null || pieceAtTarget.getColor() != getColor()) {
                    moves.add(newPos);
                }
            }
        }
        
        // Add castling moves if available
        System.out.println("Checking castling for " + getColor() + " king at " + pos.getRow() + "," + pos.getCol());
        System.out.println("hasMoved: " + hasMoved + ", isInCheck: " + isInCheck(boardState));
        
        if (!hasMoved && !isInCheck(boardState)) {
            // Check if king is in starting position
            int expectedRow = getColor() == Color.WHITE ? 7 : 0;
            if (pos.getRow() == expectedRow && pos.getCol() == 4) {
                System.out.println("King in correct starting position for castling");
                // Kingside castling
                if (canCastleKingside(boardState)) {
                    System.out.println("Adding kingside castle move");
                    Position castleMove = new Position(pos.getRow(), 6);
                    moves.add(castleMove);
                    System.out.println("Added kingside castle move to " + castleMove.getRow() + "," + castleMove.getCol());
                } else {
                    System.out.println("Cannot castle kingside");
                }
                // Queenside castling
                if (canCastleQueenside(boardState)) {
                    System.out.println("Adding queenside castle move");
                    Position castleMove = new Position(pos.getRow(), 2);
                    moves.add(castleMove);
                    System.out.println("Added queenside castle move to " + castleMove.getRow() + "," + castleMove.getCol());
                } else {
                    System.out.println("Cannot castle queenside");
                }
            } else {
                System.out.println("King not in starting position: " + pos.getRow() + "," + pos.getCol());
            }
        } else {
            if (hasMoved) {
                System.out.println("King has already moved");
            }
            if (isInCheck(boardState)) {
                System.out.println("King is in check");
            }
        }
        
        System.out.println("Valid moves for " + getColor() + " king: ");
        for (Position move : moves) {
            System.out.println("  " + move.getRow() + "," + move.getCol());
        }
        
        return moves;
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
    }

    public void makeMove(Position position) {
        super.setPosition(position);
        hasMoved = true;
    }

    private boolean wouldPutKingInCheck(Position newPos, BoardState boardState) {
        // Save the current position and state
        Position oldPos = getPosition();
        boolean wasMovedBefore = hasMoved;
        
        // Temporarily move the king
        super.setPosition(newPos);
        
        // Check if the king would be in check at the new position
        boolean inCheck = isSquareUnderAttack(newPos, boardState);
        
        // Restore the king's position and state
        super.setPosition(oldPos);
        hasMoved = wasMovedBefore;
        
        return inCheck;
    }

    private boolean isSquareUnderAttack(Position pos, BoardState boardState) {
        // Check if any opponent's piece can attack this square
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = boardState.getPieceAt(new Position(row, col));
                if (piece != null && piece.getColor() != getColor()) {
                    // For the opponent's king, only check one square radius to avoid recursion
                    if (piece instanceof King) {
                        int rowDiff = Math.abs(pos.getRow() - row);
                        int colDiff = Math.abs(pos.getCol() - col);
                        if (rowDiff <= 1 && colDiff <= 1) {
                            return true;
                        }
                        continue;
                    }
                    
                    // For other pieces, check their valid moves
                    List<Position> moves = piece.getValidMoves(boardState);
                    if (moves.contains(pos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(BoardState boardState) {
        return isSquareUnderAttack(getPosition(), boardState);
    }

    private boolean canCastleKingside(BoardState boardState) {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        
        // Check if rook is in correct position and hasn't moved
        Position rookPos = new Position(row, 7);
        Piece rook = boardState.getPieceAt(rookPos);
        if (!(rook instanceof Rook)) {
            System.out.println("No rook found for kingside castle");
            return false;
        }
        if (rook.getColor() != getColor()) {
            System.out.println("Kingside rook is wrong color");
            return false;
        }
        if (((Rook) rook).hasMoved()) {
            System.out.println("Kingside rook has moved");
            return false;
        }

        // Check if squares between king and rook are empty
        for (int c = col + 1; c < 7; c++) {
            Position pos = new Position(row, c);
            if (boardState.getPieceAt(pos) != null) {
                System.out.println("Path blocked for kingside castle at " + c);
                return false;
            }
        }

        // Check if squares king moves through are not under attack
        // Only check f1 and g1 (or f8 and g8 for black)
        for (int c = col + 1; c <= col + 2; c++) {
            Position pos = new Position(row, c);
            if (isSquareUnderAttack(pos, boardState)) {
                System.out.println("Square under attack for kingside castle at " + c);
                return false;
            }
        }

        return true;
    }

    private boolean canCastleQueenside(BoardState boardState) {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        
        // Check if rook is in correct position and hasn't moved
        Position rookPos = new Position(row, 0);
        Piece rook = boardState.getPieceAt(rookPos);
        if (!(rook instanceof Rook)) {
            System.out.println("No rook found for queenside castle");
            return false;
        }
        if (rook.getColor() != getColor()) {
            System.out.println("Queenside rook is wrong color");
            return false;
        }
        if (((Rook) rook).hasMoved()) {
            System.out.println("Queenside rook has moved");
            return false;
        }

        // Check if squares between king and rook are empty
        // Check all squares from b1 to d1 (or b8 to d8 for black)
        for (int c = 1; c < col; c++) {
            Position pos = new Position(row, c);
            if (boardState.getPieceAt(pos) != null) {
                System.out.println("Path blocked for queenside castle at " + c);
                return false;
            }
        }

        // Check if squares king moves through are not under attack
        // Only check d1 and c1 (or d8 and c8 for black)
        for (int c = col - 1; c >= col - 2; c--) {
            Position pos = new Position(row, c);
            if (isSquareUnderAttack(pos, boardState)) {
                System.out.println("Square under attack for queenside castle at " + c);
                return false;
            }
        }

        return true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
} 