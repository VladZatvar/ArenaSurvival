import java.awt.Color;

/**
 * Клас зберігає спільні налаштування гри.
 *
 * Тут знаходяться розміри вікна, базові параметри гравця,
 * ворогів, атаки, швидкість оновлення гри та основні кольори.
 *
 * Такий підхід зручний тим, що важливі числові значення не розкидані
 * по різних класах, а зібрані в одному місці.
 */
public class GameConstants {

    // Розмір ігрового вікна.
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    // Базові параметри гравця.
    public static final int PLAYER_WIDTH = 40;
    public static final int PLAYER_HEIGHT = 40;
    public static final int PLAYER_BASE_SPEED = 5;
    public static final int PLAYER_BASE_MAX_HEALTH = 100;
    public static final int PLAYER_DAMAGE_COOLDOWN = 1000;

    // Параметри ближньої автоатаки гравця.
    public static final int PLAYER_ATTACK_DAMAGE = 15;
    public static final int PLAYER_ATTACK_COOLDOWN = 700;
    public static final int PLAYER_ATTACK_DURATION = 160;
    public static final int PLAYER_ATTACK_RANGE = 70;
    public static final int PLAYER_ATTACK_WIDTH = 55;

    // Базові параметри ворогів.
    public static final int ENEMY_SIZE = 35;
    public static final int ENEMY_BASE_SPEED = 2;
    public static final int ENEMY_BASE_DAMAGE = 10;
    public static final int ENEMY_BASE_HEALTH = 30;
    // Інтервал появи нового ворога у мілісекундах.
    public static final int ENEMY_SPAWN_INTERVAL = 2500;

    // Затримка таймера у мілісекундах.
    // Значення 16 приблизно відповідає 60 оновленням на секунду.
    public static final int GAME_TIMER_DELAY = 16;

    // Основні кольори прототипу.
    public static final Color ARENA_COLOR = new Color(40, 120, 40);
    public static final Color PLAYER_COLOR = Color.BLUE;
    public static final Color ENEMY_COLOR = Color.RED;

    // Колір атаки має прозорість, щоб було видно арену під зоною удару.
    public static final Color ATTACK_COLOR = new Color(180, 180, 180, 120);

    /**
     * Приватний конструктор забороняє створювати об'єкти цього класу.
     *
     * GameConstants використовується тільки як набір спільних констант.
     */
    private GameConstants() {
        // Цей клас не повинен створюватися як звичайний об'єкт.
    }
}