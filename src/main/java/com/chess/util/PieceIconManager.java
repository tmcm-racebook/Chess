package com.chess.util;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import com.chess.model.Piece;
import java.util.HashMap;
import java.util.Map;

public class PieceIconManager {
    private static final Map<String, Image> iconCache = new HashMap<>();
    private static final int DEFAULT_SIZE = 60;

    public static Image getPieceIcon(Piece piece, int size) {
        if (piece == null) return null;
        
        String color = piece.getColor() == Piece.Color.WHITE ? "white" : "black";
        String type = piece.getType().toString().toLowerCase();
        String cacheKey = String.format("%s_%s_%d", color, type, size);
        
        // Check cache first
        if (iconCache.containsKey(cacheKey)) {
            return iconCache.get(cacheKey);
        }
        
        // Try to load the image from resources
        try {
            String imagePath = String.format("/images/%s_%s.png", type, color);
            System.out.println("Loading image from: " + imagePath); // Debug print
            Image image = new Image(PieceIconManager.class.getResourceAsStream(imagePath));
            if (!image.isError()) {
                iconCache.put(cacheKey, image);
                return image;
            }
        } catch (Exception e) {
            System.out.println("Exception loading image: " + e.getMessage()); // Debug print
        }
        
        // If image loading failed, create a fallback icon
        Image fallbackIcon = createFallbackIcon(piece, size);
        iconCache.put(cacheKey, fallbackIcon);
        return fallbackIcon;
    }

    public static Image getPieceIcon(Piece piece) {
        return getPieceIcon(piece, DEFAULT_SIZE);
    }

    private static Image createFallbackIcon(Piece piece, int size) {
        Canvas canvas = new Canvas(size, size);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Set colors based on piece color
        Color pieceColor = piece.getColor() == Piece.Color.WHITE ? Color.WHITE : Color.BLACK;
        Color strokeColor = piece.getColor() == Piece.Color.WHITE ? Color.BLACK : Color.WHITE;
        
        gc.setFill(pieceColor);
        gc.setStroke(strokeColor);
        gc.setLineWidth(2);
        
        // Draw basic shapes based on piece type
        switch (piece.getType()) {
            case PAWN:
                drawPawn(gc, size);
                break;
            case ROOK:
                drawRook(gc, size);
                break;
            case KNIGHT:
                drawKnight(gc, size);
                break;
            case BISHOP:
                drawBishop(gc, size);
                break;
            case QUEEN:
                drawQueen(gc, size);
                break;
            case KING:
                drawKing(gc, size);
                break;
        }
        
        return canvas.snapshot(null, null);
    }

    private static void drawPawn(GraphicsContext gc, int size) {
        double centerX = size / 2.0;
        double centerY = size / 2.0;
        double radius = size / 4.0;
        
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    private static void drawRook(GraphicsContext gc, int size) {
        double margin = size / 8.0;
        gc.strokeRect(margin, margin, size - 2 * margin, size - 2 * margin);
        gc.fillRect(margin, margin, size - 2 * margin, size - 2 * margin);
    }

    private static void drawKnight(GraphicsContext gc, int size) {
        double[] xPoints = {size/4.0, size*3/4.0, size*3/4.0, size/4.0};
        double[] yPoints = {size/4.0, size/4.0, size*3/4.0, size*3/4.0};
        gc.strokePolygon(xPoints, yPoints, 4);
        gc.fillPolygon(xPoints, yPoints, 4);
    }

    private static void drawBishop(GraphicsContext gc, int size) {
        double centerX = size / 2.0;
        double centerY = size / 2.0;
        double radius = size / 3.0;
        
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        gc.strokeLine(centerX, centerY - radius, centerX, centerY + radius);
    }

    private static void drawQueen(GraphicsContext gc, int size) {
        double margin = size / 8.0;
        double[] xPoints = {margin, size/2.0, size - margin};
        double[] yPoints = {size - margin, margin, size - margin};
        gc.strokePolygon(xPoints, yPoints, 3);
        gc.fillPolygon(xPoints, yPoints, 3);
    }

    private static void drawKing(GraphicsContext gc, int size) {
        double centerX = size / 2.0;
        double centerY = size / 2.0;
        double radius = size / 3.0;
        
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        gc.strokeLine(centerX - radius, centerY, centerX + radius, centerY);
        gc.strokeLine(centerX, centerY - radius, centerX, centerY + radius);
    }
} 