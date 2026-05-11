/**
 * Клас відповідає за поступове ускладнення гри.
 *
 * DifficultyManager аналізує час виживання гравця
 * і на його основі змінює параметри ігрової сесії.
 *
 * На цьому етапі складність впливає на частоту появи ворогів.
 * Чим довше гравець виживає, тим швидше з'являються нові вороги.
 */
public class DifficultyManager {

    private final GameSession gameSession;

    /**
     * Створює менеджер складності для поточної ігрової сесії.
     *
     * @param gameSession об'єкт, який зберігає час виживання гравця
     */
    public DifficultyManager(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    /**
     * Повертає поточний інтервал появи ворогів.
     *
     * Інтервал вимірюється у мілісекундах.
     * Менше значення означає, що вороги з'являються частіше.
     */
    public int getCurrentEnemySpawnInterval() {
        long survivalTime = gameSession.getSurvivalTimeSeconds();

        if (survivalTime >= 60) {
            return 1200;
        }

        if (survivalTime >= 30) {
            return 1800;
        }

        return GameConstants.ENEMY_BASE_SPAWN_INTERVAL;
    }

    /**
     * Повертає текстовий рівень складності для HUD.
     *
     * Це зручно для демонстрації, щоб було видно,
     * як гра переходить між етапами складності.
     */
    public String getDifficultyName() {
        long survivalTime = gameSession.getSurvivalTimeSeconds();

        if (survivalTime >= 60) {
            return "Hard";
        }

        if (survivalTime >= 30) {
            return "Medium";
        }

        return "Easy";
    }
}