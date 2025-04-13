package com.chess.ai;

import com.chess.model.Game;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.Position;
import java.util.List;
import java.util.Random;

public class RandomBot {
    private final Game game;
    private final Random random;
    private boolean isEnabled;

    public RandomBot(Game game) {
        this.game = game;
        this.random = new Random();
        this.isEnabled = false;
    }

    public void makeMove() {
        if (!isEnabled) return;

        // Get all pieces of the current turn's color
        List<Piece> pieces = game.getBoardState().getPieces(game.getCurrentTurn());
        if (pieces.isEmpty()) return;

        // Try to find a valid move
        for (int attempts = 0; attempts < 100; attempts++) {
            // Pick a random piece
            Piece piece = pieces.get(random.nextInt(pieces.size()));
            List<Position> validMoves = game.getValidMovesForPiece(piece);

            // Filter out moves that would leave the king in check
            validMoves.removeIf(move -> {
                // Simulate the move
                Position from = piece.getPosition();
                Piece capturedPiece = game.getBoardState().getPieceAt(move);
                game.getBoardState().movePiece(from, move);
                boolean inCheck = game.getBoardState().isInCheck(piece.getColor());
                // Undo the move
                game.getBoardState().movePiece(move, from);
                if (capturedPiece != null) {
                    game.getBoardState().setPieceAt(move, capturedPiece);
                }
                return inCheck;
            });

            if (!validMoves.isEmpty()) {
                // Pick a random valid move
                Position to = validMoves.get(random.nextInt(validMoves.size()));
                game.selectPosition(piece.getPosition());
                game.selectPosition(to);
                return;
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
} 