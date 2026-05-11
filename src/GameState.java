/**
 * Перелік основних станів гри.
 *
 * GameState потрібен, щоб GamePanel не працював тільки з простим boolean gameOver,
 * а міг чітко розрізняти: гра триває, гра на паузі або сесія завершена.
 */
public enum GameState {
    RUNNING,
    PAUSED,
    GAME_OVER
}