import java.util.List;
import java.util.Random;

/**
 * Клас відповідає за динамічне створення ворогів під час гри.
 *
 * EnemySpawner не малює об'єкти і не керує гравцем.
 * Його задача — через певний проміжок часу додавати нових ворогів
 * у список активних ворогів на арені.
 *
 * Це перший крок до процедурної появи ігрових подій.
 */
public class EnemySpawner {

    private final List<Enemy> enemies;
    private final Player player;
    private final DifficultyManager difficultyManager;
    private final ProceduralEventManager proceduralEventManager;
    private final Random random;

    // Час останнього створення ворога.
    private long lastSpawnTime;

    /**
     * Створює спавнер ворогів.
     *
     * @param enemies список активних ворогів на арені
     * @param player гравець, якого будуть переслідувати нові вороги
     * @param difficultyManager менеджер складності, який визначає базову частоту спавну
     * @param proceduralEventManager менеджер процедурних подій, який може тимчасово змінювати спавн
     */
    public EnemySpawner(
            List<Enemy> enemies,
            Player player,
            DifficultyManager difficultyManager,
            ProceduralEventManager proceduralEventManager
    ) {
        this.enemies = enemies;
        this.player = player;
        this.difficultyManager = difficultyManager;
        this.proceduralEventManager = proceduralEventManager;
        this.random = new Random();
        this.lastSpawnTime = System.currentTimeMillis();
    }

    /**
     * Оновлює спавнер на кожному кадрі гри.
     *
     * Якщо минув потрібний час, створюється новий ворог.
     */
    public void update() {
        long currentTime = System.currentTimeMillis();

        int spawnInterval = getCurrentSpawnInterval();

        if (currentTime - lastSpawnTime >= spawnInterval) {
            spawnEnemy();
            lastSpawnTime = currentTime;
        }
    }

    /**
     * Повертає поточний інтервал появи ворогів.
     *
     * У звичайному режимі інтервал залежить від складності.
     * Під час події Enemy Rush використовується окремий коротший інтервал.
     */
    private int getCurrentSpawnInterval() {
        if (proceduralEventManager.isEnemyRushActive()) {
            return GameConstants.ENEMY_RUSH_SPAWN_INTERVAL;
        }

        return difficultyManager.getCurrentEnemySpawnInterval();
    }

    /**
     * Створює нового ворога на одному з країв арени.
     *
     * Ворог не з'являється прямо в центрі карти, а приходить ніби "ззовні",
     * що краще відповідає survival arena логіці.
     */
    private void spawnEnemy() {
        int side = random.nextInt(4);

        int x = 0;
        int y = 0;

        if (side == 0) {
            // Верхній край.
            x = random.nextInt(GameConstants.WINDOW_WIDTH - GameConstants.ENEMY_SIZE);
            y = 0;
        } else if (side == 1) {
            // Нижній край.
            x = random.nextInt(GameConstants.WINDOW_WIDTH - GameConstants.ENEMY_SIZE);
            y = GameConstants.WINDOW_HEIGHT - GameConstants.ENEMY_SIZE;
        } else if (side == 2) {
            // Лівий край.
            x = 0;
            y = random.nextInt(GameConstants.WINDOW_HEIGHT - GameConstants.ENEMY_SIZE);
        } else {
            // Правий край.
            x = GameConstants.WINDOW_WIDTH - GameConstants.ENEMY_SIZE;
            y = random.nextInt(GameConstants.WINDOW_HEIGHT - GameConstants.ENEMY_SIZE);
        }

        enemies.add(new Enemy(x, y, player));
    }

    /**
     * Скидає таймер спавнера.
     *
     * Використовується при перезапуску гри, щоб вороги не створювалися
     * миттєво після рестарту через старий час.
     */
    public void reset() {
        lastSpawnTime = System.currentTimeMillis();
    }
}