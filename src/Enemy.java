/**
 * Клас описує ворога на ігровій арені.
 *
 * Enemy наслідується від GameObject, тому має координати, розмір,
 * колір і базовий метод відображення на екрані.
 *
 * На цьому етапі ворог уже вміє рухатися до гравця.
 * Це базова механіка для survival arena гри.
 */
public class Enemy extends GameObject {

    private final Player player;

    // Поточні характеристики ворога.
    // Вони не є константами, бо різні вороги можуть мати різні параметри.
    private int speed;
    private int damage;
    private int health;

    /**
     * Створює ворога у заданій позиції.
     *
     * @param x координата X ворога
     * @param y координата Y ворога
     * @param player посилання на гравця, до якого буде рухатися ворог
     */
    public Enemy(int x, int y, Player player) {
        super(
                x,
                y,
                GameConstants.ENEMY_SIZE,
                GameConstants.ENEMY_SIZE,
                GameConstants.ENEMY_COLOR
        );

        this.player = player;
        this.speed = GameConstants.ENEMY_BASE_SPEED;
        this.damage = GameConstants.ENEMY_BASE_DAMAGE;
        this.health = GameConstants.ENEMY_BASE_HEALTH;
    }

    /**
     * Оновлює стан ворога на кожному кадрі гри.
     *
     * Зараз основна логіка ворога — повільно рухатися до позиції гравця.
     */
    @Override
    public void update() {
        moveTowardsPlayer();
    }

    /**
     * Рухає ворога у напрямку до центру гравця.
     *
     * Для простоти ворог рухається окремо по осі X і по осі Y.
     * У майбутньому цей рух можна покращити, щоб він був більш плавним.
     */
    private void moveTowardsPlayer() {
        if (x < player.getCenterX()) {
            x += speed;
        }

        if (x > player.getCenterX()) {
            x -= speed;
        }

        if (y < player.getCenterY()) {
            y += speed;
        }

        if (y > player.getCenterY()) {
            y -= speed;
        }
    }

    /**
     * Повертає кількість шкоди, яку ворог завдає гравцю.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Повертає поточне здоров'я ворога.
     */
    public int getHealth() {
        return health;
    }
}