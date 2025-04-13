package com.chess.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import com.chess.util.SoundManager;
import com.chess.util.SoundManager.SoundType;
import com.chess.ai.RandomBot;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;

public class Game {
    private BoardState boardState;
    private final ObjectProperty<Piece.Color> currentTurn;
    private final ObjectProperty<GameState> gameStateProperty;
    private Position selectedPosition;
    private List<Position> validMoves;
    private final ListProperty<Move> moveHistory;
    private GameState gameState;
    private StringProperty messageProperty;
    private final RandomBot aiOpponent;

    public enum GameState {
        IN_PROGRESS,
        CHECK,
        CHECKMATE,
        STALEMATE
    }

    public Game() {
        boardState = new BoardState();
        currentTurn = new SimpleObjectProperty<>(Piece.Color.WHITE);
        selectedPosition = null;
        validMoves = new ArrayList<Position>();
        ObservableList<Move> observableMoves = FXCollections.observableArrayList();
        moveHistory = new SimpleListProperty<>(observableMoves);
        gameState = GameState.IN_PROGRESS;
        gameStateProperty = new SimpleObjectProperty<>(gameState);
        messageProperty = new SimpleStringProperty("");
        aiOpponent = new RandomBot(this);
        aiOpponent.setEnabled(true); // AI is always enabled as black
    }

    public void restart() {
        boardState = new BoardState();
        currentTurn.set(Piece.Color.WHITE);
        selectedPosition = null;
        validMoves.clear();
        moveHistory.clear();
        gameState = GameState.IN_PROGRESS;
        gameStateProperty.set(gameState);
        messageProperty.set("");
    }

    public StringProperty getMessageProperty() {
        return messageProperty;
    }

    public void setMessage(String message) {
        messageProperty.set(message);
    }

    public boolean selectPosition(Position position) {
        Piece piece = boardState.getPieceAt(position);
        
        // If a piece is already selected
        if (selectedPosition != null) {
            Piece selectedPiece = boardState.getPieceAt(selectedPosition);
            if (selectedPiece == null) {
                clearSelection();
                return false;
            }

            // Get valid moves for the selected piece
            List<Position> currentValidMoves = getValidMovesForPiece(selectedPiece);
            
            // Check if the target position is in the valid moves list
            if (currentValidMoves.contains(position)) {
                // Check if trying to capture a king
                Piece targetPiece = boardState.getPieceAt(position);
                if (targetPiece != null && targetPiece.getType() == Piece.Type.KING) {
                    setMessage("Error: Cannot capture the king");
                    clearSelection();
                    return false;
                }
                
                // Only check if move would put king in check
                if (!wouldPutKingInCheck(selectedPosition, position)) {
                    makeMove(selectedPosition, position);
                    clearSelection();
                    return true;
                } else {
                    setMessage("Error: Move would put king in check");
                    clearSelection();
                    return false;
                }
            } else {
                System.out.println("Invalid move for " + selectedPiece.getType());
                // If clicking on own piece, select it instead
                if (piece != null && piece.getColor() == getCurrentTurn()) {
                    selectedPosition = position;
                    validMoves = getValidMovesForPiece(piece);
                    System.out.println("New piece selected: " + selectedPiece.getType());
                    return true;
                } else {
                    setMessage("Error: Invalid move for " + selectedPiece.getType());
                    clearSelection();
                    return false;
                }
            }
        }

        // Select a new piece if it's the current player's turn
        if (piece != null && piece.getColor() == getCurrentTurn()) {
            selectedPosition = position;
            validMoves = getValidMovesForPiece(piece);
            return true;
        } else if (piece != null) {
            setMessage("Error: Cannot select opponent's piece");
            return false;
        }

        return false;
    }

    public List<Position> getValidMovesForPiece(Piece piece) {
        // Get the piece's valid moves according to its own validation logic
        List<Position> moves = piece.getValidMoves(boardState);
        return moves;
    }

    private boolean wouldPutKingInCheck(Position from, Position to) {
        // Save pieces before simulation
        Piece movingPiece = boardState.getPieceAt(from);
        Piece capturedPiece = boardState.getPieceAt(to);
        Position originalPos = movingPiece.getPosition();
        
        // Simulate the move
        boardState.setPieceAt(from, null);
        boardState.setPieceAt(to, movingPiece);
        movingPiece.setPosition(to);
        
        boolean inCheck = isInCheck(movingPiece.getColor());
        
        // Restore the board state exactly as it was
        boardState.setPieceAt(to, capturedPiece);
        boardState.setPieceAt(from, movingPiece);
        movingPiece.setPosition(originalPos);
        
        return inCheck;
    }

    public void makeMove(Position to) {
        if (selectedPosition != null) {
            makeMove(selectedPosition, to);
            clearSelection();
        }
    }

    private void makeMove(Position from, Position to) {
        Piece piece = boardState.getPieceAt(from);
        if (piece == null || piece.getColor() != getCurrentTurn()) {
            return;
        }

        Piece capturedPiece = boardState.getPieceAt(to);
        
        // Clear en passant flags from previous turn
        clearEnPassantFlags();
        
        // Handle castling
        if (piece instanceof King && Math.abs(to.getCol() - from.getCol()) == 2) {
            // Kingside castling
            if (to.getCol() > from.getCol()) {
                Position rookFrom = new Position(from.getRow(), 7);
                Position rookTo = new Position(from.getRow(), 5);
                Piece rook = boardState.getPieceAt(rookFrom);
                boardState.setPieceAt(rookFrom, null);
                boardState.setPieceAt(rookTo, rook);
                if (rook instanceof Rook) {
                    ((Rook) rook).makeMove(rookTo);
                }
            }
            // Queenside castling
            else {
                Position rookFrom = new Position(from.getRow(), 0);
                Position rookTo = new Position(from.getRow(), 3);
                Piece rook = boardState.getPieceAt(rookFrom);
                boardState.setPieceAt(rookFrom, null);
                boardState.setPieceAt(rookTo, rook);
                if (rook instanceof Rook) {
                    ((Rook) rook).makeMove(rookTo);
                }
            }
        }
        
        // Handle en passant capture
        if (piece instanceof Pawn && to.getCol() != from.getCol() && capturedPiece == null) {
            Position capturedPawnPos = new Position(from.getRow(), to.getCol());
            capturedPiece = boardState.getPieceAt(capturedPawnPos);
            boardState.setPieceAt(capturedPawnPos, null);
        }
        
        // Record move in history
        Move move = new Move(from, to, piece, capturedPiece);
        moveHistory.add(move);
        
        // Update board and piece
        boardState.setPieceAt(from, null);
        boardState.setPieceAt(to, piece);
        if (piece instanceof King) {
            ((King) piece).makeMove(to);
        } else if (piece instanceof Rook) {
            ((Rook) piece).makeMove(to);
        } else {
            piece.setPosition(to);
        }
        
        // Handle pawn promotion
        if (piece instanceof Pawn) {
            int promotionRow = piece.getColor() == Piece.Color.WHITE ? 0 : 7;
            if (to.getRow() == promotionRow) {
                Platform.runLater(() -> {
                    // Create promotion dialog
                    ChoiceDialog<Piece.Type> dialog = new ChoiceDialog<>(Piece.Type.QUEEN, 
                        Piece.Type.QUEEN, Piece.Type.ROOK, Piece.Type.BISHOP, Piece.Type.KNIGHT);
                    dialog.setTitle("Pawn Promotion");
                    dialog.setHeaderText("Choose piece for pawn promotion");
                    dialog.setContentText("Promote pawn to:");

                    // Show dialog and handle result
                    dialog.showAndWait().ifPresent(type -> {
                        promotePawn(to, type);
                        // Update game state after promotion
                        updateGameState();
                        
                        // Switch turns if game is still in progress
                        if (gameState == GameState.IN_PROGRESS || gameState == GameState.CHECK) {
                            Piece.Color opponentColor = getCurrentTurn() == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;
                            setCurrentTurn(opponentColor);
                            
                            // If it's black's turn, make AI move
                            if (getCurrentTurn() == Piece.Color.BLACK) {
                                makeAIMove();
                            }
                        }
                    });
                });
                return; // Return here to wait for promotion choice
            }
        }
        
        // Only handle turn switching here if it's not a pawn promotion
        // Update game state
        updateGameState();
        
        // Switch turns if game is still in progress and not a pawn promotion
        if ((gameState == GameState.IN_PROGRESS || gameState == GameState.CHECK) &&
            !(piece instanceof Pawn && to.getRow() == (piece.getColor() == Piece.Color.WHITE ? 0 : 7))) {
            Piece.Color opponentColor = getCurrentTurn() == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;
            setCurrentTurn(opponentColor);
            
            // If it's black's turn, make AI move
            if (getCurrentTurn() == Piece.Color.BLACK) {
                makeAIMove();
            }
        }
    }

    private void makeAIMove() {
        // Use Platform.runLater to ensure proper JavaFX thread handling
        Platform.runLater(() -> {
            // Get all black pieces
            List<Piece> aiPieces = boardState.getPieces(Piece.Color.BLACK);
            if (!aiPieces.isEmpty()) {
                Random random = new Random();
                // Try to find a valid move
                List<Move> legalMoves = new ArrayList<>();
                
                // Collect all legal moves that don't put own king in check
                for (Piece aiPiece : aiPieces) {
                    List<Position> validMoves = getValidMovesForPiece(aiPiece);
                    for (Position possibleMove : validMoves) {
                        if (!wouldPutKingInCheck(aiPiece.getPosition(), possibleMove)) {
                            legalMoves.add(new Move(aiPiece.getPosition(), possibleMove, aiPiece, null));
                        }
                    }
                }
                
                // If there are legal moves, make a random one
                if (!legalMoves.isEmpty()) {
                    Move selectedMove = legalMoves.get(random.nextInt(legalMoves.size()));
                    makeMove(selectedMove.getFrom(), selectedMove.getTo());
                } else {
                    // If no legal moves and in check, it's checkmate
                    if (isInCheck(Piece.Color.BLACK)) {
                        gameState = GameState.CHECKMATE;
                        messageProperty.set("White wins by checkmate!");
                        gameStateProperty.set(gameState);
                    } else {
                        // If no legal moves and not in check, it's stalemate
                        gameState = GameState.STALEMATE;
                        messageProperty.set("Game ends in stalemate!");
                        gameStateProperty.set(gameState);
                    }
                }
            }
        });
    }

    private void updateGameState() {
        if (isInCheck(currentTurn.get())) {
            if (isCheckmate(currentTurn.get())) {
                gameState = GameState.CHECKMATE;
                messageProperty.set(currentTurn.get() == Piece.Color.WHITE ? "Black wins by checkmate!" : "White wins by checkmate!");
            } else {
                gameState = GameState.CHECK;
                messageProperty.set(currentTurn.get() == Piece.Color.WHITE ? "White is in check!" : "Black is in check!");
            }
        } else if (isStalemate(currentTurn.get())) {
            gameState = GameState.STALEMATE;
            messageProperty.set("Game ends in stalemate!");
        } else {
            gameState = GameState.IN_PROGRESS;
            messageProperty.set("");
        }
        gameStateProperty.set(gameState);
    }

    private void clearSelection() {
        selectedPosition = null;
        validMoves.clear();
    }

    public boolean canUndo() {
        return !moveHistory.isEmpty();
    }

    public void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.remove(moveHistory.size() - 1);
            
            if (lastMove.wasPromotion()) {
                // Create a new pawn at the original position
                Pawn originalPawn = new Pawn(lastMove.getPiece().getColor(), lastMove.getFrom());
                boardState.setPieceAt(lastMove.getFrom(), originalPawn);
                boardState.setPieceAt(lastMove.getTo(), null);
            } else {
                // Move piece back normally
                boardState.movePiece(lastMove.getTo(), lastMove.getFrom());
            }
            
            // If it's a pawn moving back to its starting rank, reset hasMoved
            if (lastMove.getPiece() instanceof Pawn || lastMove.wasPromotion()) {
                Piece piece = boardState.getPieceAt(lastMove.getFrom());
                if (piece instanceof Pawn) {
                    int startingRank = piece.getColor() == Piece.Color.WHITE ? 6 : 1;
                    if (lastMove.getFrom().getRow() == startingRank) {
                        ((Pawn) piece).setHasMoved(false);
                    }
                }
            }
            
            // Restore captured piece
            if (lastMove.getCapturedPiece() != null) {
                // For en passant captures, we need to restore the pawn in a different position
                if ((lastMove.getPiece() instanceof Pawn || lastMove.wasPromotion()) && 
                    lastMove.getTo().getCol() != lastMove.getFrom().getCol() && 
                    lastMove.getCapturedPiece() instanceof Pawn) {
                    // Restore the captured pawn one row forward from its capture position
                    Position capturedPawnPos = new Position(
                        lastMove.getTo().getRow() + (lastMove.getCapturedPiece().getColor() == Piece.Color.WHITE ? -1 : 1),
                        lastMove.getTo().getCol()
                    );
                    boardState.setPieceAt(capturedPawnPos, lastMove.getCapturedPiece());
                } else {
                    // Normal capture - restore piece to its original position
                    boardState.setPieceAt(lastMove.getTo(), lastMove.getCapturedPiece());
                }
            }
            
            // Switch turn back
            setCurrentTurn(getCurrentTurn() == Piece.Color.WHITE ? 
                         Piece.Color.BLACK : Piece.Color.WHITE);
            
            // Update game state
            updateGameState();
        }
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public Piece.Color getCurrentTurn() {
        return currentTurn.get();
    }

    public void setCurrentTurn(Piece.Color turn) {
        currentTurn.set(turn);
    }

    public ObjectProperty<Piece.Color> getCurrentTurnProperty() {
        return currentTurn;
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public List<Position> getValidMoves() {
        return validMoves;
    }

    public ListProperty<Move> getMoveHistory() {
        return moveHistory;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ObjectProperty<GameState> getGameStateProperty() {
        return gameStateProperty;
    }

    private boolean isInCheck(Piece.Color color) {
        Position kingPosition = findKingPosition(color);
        if (kingPosition == null) return false;

        // Check if any opponent piece can capture the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = boardState.getPieceAt(pos);
                
                if (piece != null && piece.getColor() != color) {
                    if (piece instanceof Pawn) {
                        // For pawns, only check diagonal attacks
                        int direction = piece.getColor() == Piece.Color.WHITE ? -1 : 1;
                        Position leftAttack = new Position(pos.getRow() + direction, pos.getCol() - 1);
                        Position rightAttack = new Position(pos.getRow() + direction, pos.getCol() + 1);
                        
                        if ((leftAttack.equals(kingPosition) || rightAttack.equals(kingPosition)) &&
                            leftAttack.isValid() && rightAttack.isValid()) {
                            return true;
                        }
                    } else {
                        // For other pieces, check all valid moves
                        List<Position> moves = piece.getValidMoves(boardState);
                        if (moves.contains(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(Piece.Color color) {
        if (!isInCheck(color)) return false;

        // Check if any piece can make a move that gets the king out of check
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = boardState.getPieceAt(pos);
                
                if (piece != null && piece.getColor() == color) {
                    List<Position> moves = getValidMovesForPiece(piece);
                    for (Position move : moves) {
                        // Save pieces before simulation
                        Piece capturedPiece = boardState.getPieceAt(move);
                        Position originalPos = piece.getPosition();
                        
                        // Simulate the move
                        boardState.setPieceAt(originalPos, null);
                        boardState.setPieceAt(move, piece);
                        piece.setPosition(move);
                        
                        boolean stillInCheck = isInCheck(color);
                        
                        // Restore the board state exactly as it was
                        boardState.setPieceAt(move, capturedPiece);
                        boardState.setPieceAt(originalPos, piece);
                        piece.setPosition(originalPos);
                        
                        if (!stillInCheck) {
                            return false; // Found a move that gets out of check
                        }
                    }
                }
            }
        }
        return true; // No moves found that get out of check
    }

    private Position findKingPosition(Piece.Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = boardState.getPieceAt(pos);
                if (piece != null && piece.getType() == Piece.Type.KING && piece.getColor() == color) {
                    return pos;
                }
            }
        }
        return null;
    }

    private void clearEnPassantFlags() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = boardState.getPieceAt(pos);
                if (piece instanceof Pawn) {
                    ((Pawn) piece).clearDoubleMovedFlag();
                }
            }
        }
    }

    public void promotePawn(Position position, Piece.Type promotionType) {
        Piece pawn = boardState.getPieceAt(position);
        if (pawn == null || !(pawn instanceof Pawn)) return;

        // Create new piece based on selection
        Piece promotedPiece;
        switch (promotionType) {
            case QUEEN:
                promotedPiece = new Queen(pawn.getColor(), position);
                break;
            case ROOK:
                promotedPiece = new Rook(pawn.getColor(), position);
                break;
            case BISHOP:
                promotedPiece = new Bishop(pawn.getColor(), position);
                break;
            case KNIGHT:
                promotedPiece = new Knight(pawn.getColor(), position);
                break;
            default:
                promotedPiece = new Queen(pawn.getColor(), position);
        }
        
        boardState.setPieceAt(position, promotedPiece);

        // Update the last move in history to reflect the promotion
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.get(moveHistory.size() - 1);
            // Create a new move with the promoted piece and mark it as a promotion
            Move updatedMove = new Move(lastMove.getFrom(), lastMove.getTo(), promotedPiece, lastMove.getCapturedPiece(), true);
            moveHistory.set(moveHistory.size() - 1, updatedMove);
        }
    }

    private boolean isStalemate(Piece.Color color) {
        // A stalemate occurs when the player is NOT in check but has no legal moves
        if (isInCheck(color)) return false;

        // Check if any piece can make a legal move
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = boardState.getPieceAt(pos);
                
                if (piece != null && piece.getColor() == color) {
                    List<Position> moves = getValidMovesForPiece(piece);
                    for (Position move : moves) {
                        // Save pieces before simulation
                        Piece capturedPiece = boardState.getPieceAt(move);
                        Position originalPos = piece.getPosition();
                        
                        // Simulate the move
                        boardState.setPieceAt(originalPos, null);
                        boardState.setPieceAt(move, piece);
                        piece.setPosition(move);
                        
                        boolean wouldBeInCheck = isInCheck(color);
                        
                        // Restore the board state exactly as it was
                        boardState.setPieceAt(move, capturedPiece);
                        boardState.setPieceAt(originalPos, piece);
                        piece.setPosition(originalPos);
                        
                        if (!wouldBeInCheck) {
                            return false; // Found a legal move
                        }
                    }
                }
            }
        }
        return true; // No legal moves found
    }
} 