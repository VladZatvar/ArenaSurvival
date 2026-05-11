/**
 * Клас зберігає статистику поточної ігрової сесії.
 *
 * GameSession відповідає за час виживання гравця
 * та кількість переможених ворогів.
 *
 * Це окремий клас, щоб не перевантажувати GamePanel зайвими змінними.
 */
public class GameSession {

    // Час початку поточної гри.
    private long startTime;

    // Кількість ворогів, яких переміг гравець.
    private int killCount;

    /**
     * Створює нову ігрову сесію.
     */
    public GameSession() {
        reset();
    }

    /**
     * Скидає статистику сесії до початкового стану.
     *
     * Використовується на старті гри та після рестарту.
     */
    public void reset() {
        startTime = System.currentTimeMillis();
        killCount = 0;
    }

    /**
     * Збільшує кількість переможених ворогів на 1.
     *
     * Викликається тоді, коли ворог втрачає все здоров'я
     * і видаляється з ігрової сцени.
     */
    public void addKill() {
        killCount++;
    }

    /**
     * Повертає кількість переможених ворогів.
     */
    public int getKillCount() {
        return killCount;
    }

    /**
     * Повертає час виживання у секундах.
     */
    public long getSurvivalTimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    /**
     * Повертає час виживання у форматі MM:SS.
     *
     * Наприклад: 01:25 означає 1 хвилину 25 секунд.
     */
    public String getFormattedSurvivalTime() {
        long totalSeconds = getSurvivalTimeSeconds();

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}