module com.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.base;
    requires java.prefs;
    
    opens com.chess to javafx.fxml;
    exports com.chess;
    exports com.chess.model;
    exports com.chess.view;
    exports com.chess.util;
} 