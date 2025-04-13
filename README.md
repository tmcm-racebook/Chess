# Chess Game

A Java-based chess game with a graphical user interface built using JavaFX. The game features standard chess rules, including special moves like castling, en passant, and pawn promotion. It also includes a basic AI opponent for single-player gameplay.

## Features

- Standard chess rules implementation
- Special moves support (castling, en passant, pawn promotion)
- Graphical user interface with piece dragging
- Basic AI opponent
- Game state tracking (check, checkmate, stalemate)
- Move history
- Game controls (new game, undo/redo, resign)

## Requirements

- Java 17 or later
- Maven 3.6 or later

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/chess-game.git
   cd chess-game
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the game:
   ```bash
   mvn javafx:run
   ```

## Usage

### Starting a New Game
- Click the "New Game" button to start a fresh game
- The game starts with white pieces moving first

### Making Moves
- Click and drag pieces to move them
- Valid moves are highlighted when a piece is selected
- The game enforces standard chess rules

### Special Moves
- **Castling**: Move the king two squares toward a rook, and the rook moves to the square the king passed over
- **En Passant**: Capture an opponent's pawn that has just moved two squares forward
- **Pawn Promotion**: When a pawn reaches the opposite end of the board, it is automatically promoted to a queen

### Game Controls
- **New Game**: Start a new game
- **Undo**: Take back the last move
- **Redo**: Redo a move that was undone
- **Resign**: End the current game

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Guidelines
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Chess piece images sourced from [Chess.com](https://www.chess.com)
- JavaFX for the GUI framework
- JUnit for testing framework

## Project Structure

The project follows a standard MVC (Model-View-Controller) architecture:

```
src/main/java/com/chess/
├── model/          # Game logic and data models
│   └── Piece.java  # Base class for chess pieces
├── view/           # UI components
│   └── ChessBoard.java  # Chess board visualization
├── controller/     # Game control and event handling
│   └── GameController.java  # Main game controller
├── util/          # Utility classes
│   └── MoveValidator.java  # Move validation logic
└── Main.java      # Application entry point
```

Each package has a specific responsibility:
- `model`: Contains the game's data structures and business logic
- `view`: Handles the visual representation of the game
- `controller`: Manages game flow and user interactions
- `util`: Houses utility classes and helper functions
