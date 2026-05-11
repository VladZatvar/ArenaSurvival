/**
 * Клас зберігає статистику поточної ігрової сесії.
 *
 * GameSession відповідає за час виживання гравця
 * та кількість переможених ворогів.
 *
 * Також клас підтримує паузу та завершення гри,
 * щоб таймер не продовжував рахувати час після Game Over або під час паузи.
 */
public class GameSession {

    // Час початку поточної гри.
    private long startTime;

    // Час завершення гри.
    // Якщо гра ще триває, значення дорівнює 0.
    private long endTime;

    // Загальний час, який гра провела на паузі.
    private long totalPausedTime;

    // Час початку поточної паузи.
    private long pauseStartTime;

    // Кількість ворогів, яких переміг гравець.
    private int killCount;

    // Прапорець, який показує, чи завершена сесія.
    private boolean finished;

    // Прапорець, який показує, чи сесія зараз на паузі.
    private boolean paused;

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
        endTime = 0;
        totalPausedTime = 0;
        pauseStartTime = 0;
        killCount = 0;
        finished = false;
        paused = false;
    }

    /**
     * Ставить ігрову сесію на паузу.
     *
     * Після цього таймер виживання тимчасово не збільшується.
     */
    public void pause() {
        if (!finished && !paused) {
            pauseStartTime = System.currentTimeMillis();
            paused = true;
        }
    }

    /**
     * Продовжує ігрову сесію після паузи.
     *
     * Час, проведений на паузі, не враховується у час виживання.
     */
    public void resume() {
        if (!finished && paused) {
            long pauseDuration = System.currentTimeMillis() - pauseStartTime;
            totalPausedTime += pauseDuration;

            pauseStartTime = 0;
            paused = false;
        }
    }

    /**
     * Завершує ігрову сесію та фіксує час завершення.
     *
     * Після виклику цього методу таймер виживання більше не збільшується.
     * Це потрібно для Game Over, щоб час не йшов далі після смерті гравця.
     */
    public void finish() {
        if (!finished) {
            if (paused) {
                resume();
            }

            endTime = System.currentTimeMillis();
            finished = true;
        }
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
     *
     * Якщо гра завершена — повертається зафіксований час завершення.
     * Якщо гра на паузі — повертається час на момент початку паузи.
     */
    public long getSurvivalTimeSeconds() {
        long currentTime;

        if (finished) {
            currentTime = endTime;
        } else if (paused) {
            currentTime = pauseStartTime;
        } else {
            currentTime = System.currentTimeMillis();
        }

        return (currentTime - startTime - totalPausedTime) / 1000;
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