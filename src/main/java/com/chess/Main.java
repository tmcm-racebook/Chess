package com.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.chess.view.ChessBoard;
import com.chess.view.MoveHistory;
import com.chess.view.CapturedPiecesPanel;
import com.chess.model.Game;
import com.chess.model.Piece;
import com.chess.model.Game.GameState;
import com.chess.model.Move;
import com.chess.util.ThemeManager;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class Main extends Application {
    private ChessBoard chessBoard;
    private MoveHistory moveHistory;
    private CapturedPiecesPanel capturedPiecesPanel;
    private Label turnLabel;
    private Label statusLabel;
    private Label messageLabel;
    private Button undoButton;
    private Button themeToggleButton;
    private Button fullscreenButton;
    private VBox root;

    @Override
    public void start(Stage primaryStage) {
        chessBoard = new ChessBoard();
        moveHistory = new MoveHistory();
        capturedPiecesPanel = new CapturedPiecesPanel();
        
        // Create controls
        turnLabel = new Label("Current Turn: White");
        statusLabel = new Label();
        messageLabel = new Label();
        undoButton = new Button("Undo");
        themeToggleButton = new Button("Dark Mode");
        fullscreenButton = new Button("Fullscreen");
        
        // Style controls
        turnLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        messageLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        undoButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 10px;");
        themeToggleButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 10px;");
        fullscreenButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 10px;");
        
        // Create control panel
        HBox controls = new HBox(10);
        controls.getChildren().addAll(turnLabel, undoButton, themeToggleButton, fullscreenButton);
        controls.setStyle("-fx-padding: 10px; -fx-alignment: center;");
        
        // Create a container for the board and side panels
        HBox gameContainer = new HBox(20);
        gameContainer.setAlignment(Pos.CENTER);
        gameContainer.getChildren().addAll(moveHistory, chessBoard, capturedPiecesPanel);
        
        // Create root layout
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(controls, statusLabel, messageLabel, gameContainer);
        root.setStyle("-fx-padding: 10px; -fx-background-color: transparent;");
        
        // Create scene
        Scene scene = new Scene(root);
        
        // Set up event handlers
        undoButton.setOnAction(e -> handleUndo());
        themeToggleButton.setOnAction(e -> toggleTheme());
        fullscreenButton.setOnAction(e -> toggleFullscreen(primaryStage));
        
        // Listen for turn changes
        chessBoard.getGame().getCurrentTurnProperty().addListener((obs, oldVal, newVal) -> updateTurnLabel());
        
        // Update move history when a move is made
        chessBoard.getGame().getMoveHistory().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                Move move = newValue.get(newValue.size() - 1);
                moveHistory.addMove(move);
                if (move.getCapturedPiece() != null) {
                    capturedPiecesPanel.addCapturedPiece(move.getCapturedPiece());
                }
            }
        });

        // Update game status
        chessBoard.getGame().getGameStateProperty().addListener((obs, oldState, newState) -> 
            updateStatusLabel(newState));

        // Update message label when game state changes
        chessBoard.getGame().getMessageProperty().addListener((obs, oldVal, newVal) -> {
            messageLabel.setText(newVal);
        });
        
        // Set up stage
        primaryStage.setTitle("JavaFX Chess vs AI");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Update theme after scene is set up
        updateTheme();
        
        primaryStage.show();
    }

    private void handleUndo() {
        Game game = chessBoard.getGame();
        if (game.canUndo()) {
            game.undoLastMove();
            chessBoard.refresh();
            moveHistory.clear();
            // Re-add all moves except the last one
            for (com.chess.model.Move move : game.getMoveHistory()) {
                moveHistory.addMove(move);
            }
            updateTurnLabel();
        }
    }

    private void updateTurnLabel() {
        Piece.Color turn = chessBoard.getGame().getCurrentTurn();
        turnLabel.setText("Current Turn: " + (turn == Piece.Color.WHITE ? "White" : "Black"));
    }

    private void updateStatusLabel(GameState state) {
        switch (state) {
            case CHECK:
                statusLabel.setText("Check!");
                statusLabel.setStyle("-fx-text-fill: red;");
                break;
            case CHECKMATE:
                statusLabel.setText("Checkmate! Game Over.");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                break;
            case STALEMATE:
                statusLabel.setText("Stalemate! Game Over.");
                statusLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                break;
            default:
                statusLabel.setText("");
                statusLabel.setStyle("");
                break;
        }
    }

    private void toggleTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        themeManager.setDarkMode(!themeManager.isDarkMode());
        updateTheme();
    }

    private void updateTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        Color backgroundColor = themeManager.getBackgroundColor();
        Color textColor = themeManager.getLightTextColor();
        
        root.setStyle("-fx-background-color: " + toRGBCode(backgroundColor) + ";");
        turnLabel.setStyle("-fx-text-fill: " + toRGBCode(textColor) + "; -fx-font-size: 16px; -fx-padding: 10px;");
    }

    private void toggleFullscreen(Stage stage) {
        stage.setFullScreen(!stage.isFullScreen());
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
} 