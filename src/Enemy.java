/**
 * Клас описує ворога на ігровій арені.
 *
 * Enemy наслідується від GameObject, тому має координати, розмір,
 * колір і базовий метод відображення на екрані.
 *
 * Ворог рухається до гравця, може завдавати шкоду
 * і має власне здоров'я.
 */
public class Enemy extends GameObject {

    private final Player player;

    // Поточні характеристики ворога.
    // Вони не є константами, бо різні типи ворогів можуть мати різні параметри.
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
     * Завдає шкоду ворогу.
     *
     * Якщо здоров'я падає до нуля або нижче,
     * ворог вважається переможеним і буде видалений зі сцени.
     *
     * @param damageAmount кількість отриманої шкоди
     */
    public void takeDamage(int damageAmount) {
        health -= damageAmount;

        if (health < 0) {
            health = 0;
        }

        System.out.println("Enemy HP: " + health);
    }

    /**
     * Перевіряє, чи ворог ще живий.
     */
    public boolean isAlive() {
        return health > 0;
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