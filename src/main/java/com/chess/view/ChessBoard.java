package com.chess.view;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import com.chess.model.*;
import javafx.application.Platform;
import java.util.Optional;
import com.chess.view.*;

public class ChessBoard extends GridPane {
    private static final int TILE_SIZE = 60;
    private final Game game;
    private final Tile[][] tiles;
    private boolean isProcessingMove = false;
    private Position selectedPosition;

    public ChessBoard() {
        game = new Game();
        tiles = new Tile[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = new Tile(row, col, TILE_SIZE);
                tile.setOnMouseClicked(e -> handleTileClick(tile));
                tiles[row][col] = tile;
                add(tile, col, row);
            }
        }
        refresh();
    }

    private void handleTileClick(Tile clickedTile) {
        if (isProcessingMove) return;
        
        int row = GridPane.getRowIndex(clickedTile);
        int col = GridPane.getColumnIndex(clickedTile);
        Position clickedPos = new Position(row, col);

        isProcessingMove = true;
        try {
            // Use selectPosition which handles both piece selection and movement
            game.selectPosition(clickedPos);
            refresh();
        } finally {
            isProcessingMove = false;
        }
    }

    public void refresh() {
        Platform.runLater(() -> {
            // Clear all highlights
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    tiles[row][col].unhighlight();
                }
            }

            // Highlight selected piece and valid moves
            Position selectedPos = game.getSelectedPosition();
            if (selectedPos != null) {
                tiles[selectedPos.getRow()][selectedPos.getCol()].setHighlighted(true);
                for (Position validMove : game.getValidMoves()) {
                    tiles[validMove.getRow()][validMove.getCol()].setValidMove(true);
                }
            }

            // Update piece positions
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Position pos = new Position(row, col);
                    Piece piece = game.getBoardState().getPieceAt(pos);
                    tiles[row][col].setPiece(piece);
                }
            }
        });
    }

    public Game getGame() {
        return game;
    }
} 