package com.chess.util;

import javafx.scene.paint.Color;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ThemeManager {
    private static final ThemeManager instance = new ThemeManager();
    private final BooleanProperty isDarkMode = new SimpleBooleanProperty(false);

    // Light theme colors
    private static final Color LIGHT_SQUARE = Color.WHEAT;
    private static final Color DARK_SQUARE = Color.SADDLEBROWN;
    private static final Color LIGHT_PIECE = Color.WHITE;
    private static final Color DARK_PIECE = Color.BLACK;
    private static final Color LIGHT_TEXT = Color.BLACK;
    private static final Color DARK_TEXT = Color.WHITE;
    private static final Color LIGHT_BACKGROUND = Color.WHITE;
    private static final Color DARK_BACKGROUND = Color.DARKGRAY;

    // Dark theme colors
    private static final Color DARK_THEME_LIGHT_SQUARE = Color.DARKGRAY;
    private static final Color DARK_THEME_DARK_SQUARE = Color.DIMGREY;
    private static final Color DARK_THEME_LIGHT_PIECE = Color.WHITE;
    private static final Color DARK_THEME_DARK_PIECE = Color.LIGHTGRAY;
    private static final Color DARK_THEME_LIGHT_TEXT = Color.WHITE;
    private static final Color DARK_THEME_DARK_TEXT = Color.WHITE;
    private static final Color DARK_THEME_BACKGROUND = Color.BLACK;

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        return instance;
    }

    public BooleanProperty isDarkModeProperty() {
        return isDarkMode;
    }

    public boolean isDarkMode() {
        return isDarkMode.get();
    }

    public void setDarkMode(boolean darkMode) {
        isDarkMode.set(darkMode);
    }

    public Color getLightSquareColor() {
        return isDarkMode.get() ? DARK_THEME_LIGHT_SQUARE : LIGHT_SQUARE;
    }

    public Color getDarkSquareColor() {
        return isDarkMode.get() ? DARK_THEME_DARK_SQUARE : DARK_SQUARE;
    }

    public Color getLightPieceColor() {
        return isDarkMode.get() ? DARK_THEME_LIGHT_PIECE : LIGHT_PIECE;
    }

    public Color getDarkPieceColor() {
        return isDarkMode.get() ? DARK_THEME_DARK_PIECE : DARK_PIECE;
    }

    public Color getLightTextColor() {
        return isDarkMode.get() ? DARK_THEME_LIGHT_TEXT : LIGHT_TEXT;
    }

    public Color getDarkTextColor() {
        return isDarkMode.get() ? DARK_THEME_DARK_TEXT : DARK_TEXT;
    }

    public Color getBackgroundColor() {
        return isDarkMode.get() ? DARK_THEME_BACKGROUND : LIGHT_BACKGROUND;
    }
} 