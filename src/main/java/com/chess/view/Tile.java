package com.chess.view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import com.chess.model.Piece;
import com.chess.util.PieceIconManager;
import com.chess.util.ThemeManager;
import javafx.geometry.Pos;

public class Tile extends StackPane {
    private final int row;
    private final int col;
    private final Rectangle background;
    private final Text pieceText;
    private final ImageView pieceIcon;
    private boolean isSelected = false;
    private Piece piece;
    private static final int TILE_SIZE = 60;

    public Tile(int row, int col, int size) {
        this.row = row;
        this.col = col;
        
        // Set up the StackPane
        setPrefSize(size, size);
        setMaxSize(size, size);
        setMinSize(size, size);
        setAlignment(Pos.CENTER);
        
        background = new Rectangle(size, size);
        updateBackgroundColor();
        
        pieceText = new Text();
        pieceText.setStyle("-fx-font-size: " + (size * 0.8) + "px;");
        
        pieceIcon = new ImageView();
        pieceIcon.setFitWidth(size * 0.8);
        pieceIcon.setFitHeight(size * 0.8);
        pieceIcon.setPreserveRatio(true);
        
        getChildren().addAll(background, pieceIcon, pieceText);
        
        // Set CSS style for border
        setStyle("-fx-border-color: black; -fx-border-width: 0.5px; -fx-padding: 0;");

        // Add click event handler
        setOnMouseClicked(event -> {
            System.out.printf("Clicked tile at row %d, col %d%n", row, col);
            toggleSelection();
        });

        // Listen for theme changes
        ThemeManager.getInstance().isDarkModeProperty().addListener((obs, oldVal, newVal) -> {
            updateBackgroundColor();
            if (piece != null) {
                setPiece(piece);
            }
        });
    }

    private void updateBackgroundColor() {
        ThemeManager themeManager = ThemeManager.getInstance();
        background.setFill(isLightSquare() ? 
            themeManager.getLightSquareColor() : 
            themeManager.getDarkSquareColor());
    }

    private boolean isLightSquare() {
        return (row + col) % 2 == 0;
    }

    public void highlight() {
        background.setStroke(Color.YELLOW);
        background.setStrokeWidth(2);
    }

    public void unhighlight() {
        background.setStroke(null);
    }

    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            highlight();
        } else {
            unhighlight();
        }
    }

    public void setValidMove(boolean valid) {
        if (valid) {
            background.setStroke(Color.GREEN);
            background.setStrokeWidth(2);
        } else {
            unhighlight();
        }
    }

    public void toggleSelection() {
        if (background.getStroke() == null) {
            highlight();
        } else {
            unhighlight();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece == null) {
            pieceText.setText("");
            pieceIcon.setImage(null);
            return;
        }

        // Try to load the icon first
        Image icon = PieceIconManager.getPieceIcon(piece);
        if (icon != null && !icon.isError()) {
            pieceIcon.setImage(icon);
            pieceText.setText("");
        } else {
            // Fallback to text if no icon is available
            pieceIcon.setImage(null);
            pieceText.setText(piece.getType().toString().substring(0, 1));
            ThemeManager themeManager = ThemeManager.getInstance();
            pieceText.setFill(piece.getColor() == Piece.Color.WHITE ? 
                themeManager.getLightTextColor() : 
                themeManager.getDarkTextColor());
        }
    }

    public Piece getPiece() {
        return piece;
    }
} 