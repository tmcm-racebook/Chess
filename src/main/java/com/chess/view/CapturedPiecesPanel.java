package com.chess.view;

import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.chess.model.Piece;
import com.chess.util.PieceIconManager;
import java.util.ArrayList;
import java.util.List;

public class CapturedPiecesPanel extends VBox {
    private final List<Piece> whiteCapturedPieces;
    private final List<Piece> blackCapturedPieces;
    private final FlowPane whitePiecesBox;
    private final FlowPane blackPiecesBox;
    private static final int ICON_SIZE = 30;
    private static final double PANEL_WIDTH = 200; // Fixed width for the panel

    public CapturedPiecesPanel() {
        whiteCapturedPieces = new ArrayList<>();
        blackCapturedPieces = new ArrayList<>();
        
        // Create labels and boxes for each player
        Label whiteLabel = new Label("White Captures:");
        Label blackLabel = new Label("Black Captures:");
        whitePiecesBox = new FlowPane(5, 5); // horizontal and vertical gaps
        blackPiecesBox = new FlowPane(5, 5);
        
        // Set fixed width and style for the FlowPanes
        whitePiecesBox.setPrefWidth(PANEL_WIDTH);
        blackPiecesBox.setPrefWidth(PANEL_WIDTH);
        whitePiecesBox.setStyle("-fx-padding: 5px; -fx-background-color: transparent;");
        blackPiecesBox.setStyle("-fx-padding: 5px; -fx-background-color: transparent;");
        
        // Add components to the panel
        getChildren().addAll(whiteLabel, whitePiecesBox, blackLabel, blackPiecesBox);
        setStyle("-fx-padding: 10px; -fx-spacing: 10px;");
        setPrefWidth(PANEL_WIDTH);
    }

    public void addCapturedPiece(Piece piece) {
        if (piece.getColor() == Piece.Color.WHITE) {
            whiteCapturedPieces.add(piece);
            updateWhitePieces();
        } else {
            blackCapturedPieces.add(piece);
            updateBlackPieces();
        }
    }

    public void clear() {
        whiteCapturedPieces.clear();
        blackCapturedPieces.clear();
        whitePiecesBox.getChildren().clear();
        blackPiecesBox.getChildren().clear();
    }

    private void updateWhitePieces() {
        whitePiecesBox.getChildren().clear();
        for (Piece piece : whiteCapturedPieces) {
            Image icon = PieceIconManager.getPieceIcon(piece, ICON_SIZE);
            if (icon != null && !icon.isError()) {
                ImageView iconView = new ImageView(icon);
                iconView.setFitWidth(ICON_SIZE);
                iconView.setFitHeight(ICON_SIZE);
                iconView.setPreserveRatio(true);
                whitePiecesBox.getChildren().add(iconView);
            } else {
                // Fallback to text if no icon is available
                Label textLabel = new Label(piece.getType().toString().substring(0, 1));
                textLabel.setStyle("-fx-font-size: " + ICON_SIZE + "px;");
                whitePiecesBox.getChildren().add(textLabel);
            }
        }
    }

    private void updateBlackPieces() {
        blackPiecesBox.getChildren().clear();
        for (Piece piece : blackCapturedPieces) {
            Image icon = PieceIconManager.getPieceIcon(piece, ICON_SIZE);
            if (icon != null && !icon.isError()) {
                ImageView iconView = new ImageView(icon);
                iconView.setFitWidth(ICON_SIZE);
                iconView.setFitHeight(ICON_SIZE);
                iconView.setPreserveRatio(true);
                blackPiecesBox.getChildren().add(iconView);
            } else {
                // Fallback to text if no icon is available
                Label textLabel = new Label(piece.getType().toString().substring(0, 1));
                textLabel.setStyle("-fx-font-size: " + ICON_SIZE + "px;");
                blackPiecesBox.getChildren().add(textLabel);
            }
        }
    }
} 