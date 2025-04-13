package com.chess.view;

import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.Position;

public class MoveHistory extends VBox {
    private final ListView<String> moveList;
    private int moveNumber;

    public MoveHistory() {
        moveList = new ListView<>();
        moveNumber = 1;
        
        // Style the move list
        moveList.setPrefWidth(200);
        moveList.setPrefHeight(400);
        
        // Add title
        Text title = new Text("Move History");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        getChildren().addAll(title, moveList);
        setSpacing(10);
        setStyle("-fx-padding: 10px;");
    }

    public void addMove(Move move) {
        String moveText = String.format("%d. %s %s -> %s", 
            moveNumber,
            getPieceSymbol(move.getPiece()),
            formatPosition(move.getFrom()),
            formatPosition(move.getTo()));
        
        if (move.getCapturedPiece() != null) {
            moveText += " captures " + getPieceSymbol(move.getCapturedPiece());
        }
        
        moveList.getItems().add(moveText);
        moveNumber++;
    }

    public void clear() {
        moveList.getItems().clear();
        moveNumber = 1;
    }

    private String getPieceSymbol(Piece piece) {
        switch (piece.getType()) {
            case PAWN: return "P";
            case ROOK: return "R";
            case KNIGHT: return "N";
            case BISHOP: return "B";
            case QUEEN: return "Q";
            case KING: return "K";
            default: return "?";
        }
    }

    private String formatPosition(Position pos) {
        char col = (char) ('a' + pos.getCol());
        int row = 8 - pos.getRow();
        return String.format("%c%d", col, row);
    }
} 