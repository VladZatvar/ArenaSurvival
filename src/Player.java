import java.awt.Graphics;

/**
 * Клас описує гравця на арені.
 *
 * Player наслідується від GameObject, тому має базові властивості:
 * координати, розмір, колір і можливість малюватися на екрані.
 *
 * Додатково цей клас відповідає за рух гравця відповідно до натиснутих клавіш.
 */
public class Player extends GameObject {

    private final InputHandler inputHandler;

    // Поточна швидкість гравця.
    // Вона бере базове значення, але в майбутньому може змінюватися бафами або прокачкою.
    private int speed;

    // Поточне та максимальне здоров'я гравця.
    private int maxHealth;
    private int currentHealth;

    // Час останнього отримання шкоди.
    // Потрібен, щоб вороги не завдавали шкоду кожен кадр.
    private long lastDamageTime;


    /**
     * Створює гравця у заданій позиції.
     *
     * @param x початкова координата X
     * @param y початкова координата Y
     * @param inputHandler об'єкт, який зберігає стан клавіш керування
     */
    public Player(int x, int y, InputHandler inputHandler) {
        super(
                x,
                y,
                GameConstants.PLAYER_WIDTH,
                GameConstants.PLAYER_HEIGHT,
                GameConstants.PLAYER_COLOR
        );

        this.inputHandler = inputHandler;
        this.speed = GameConstants.PLAYER_BASE_SPEED;
        this.maxHealth = GameConstants.PLAYER_BASE_MAX_HEALTH;
        this.currentHealth = maxHealth;
        this.lastDamageTime = 0;
    }

    /**
     * Оновлює стан гравця на кожному кадрі гри.
     *
     * Зараз тут реалізовано базовий рух по арені.
     */
    @Override
    public void update() {
        move();
        keepInsideArena();
    }

    /**
     * Змінює координати гравця залежно від натиснутих клавіш.
     */
    private void move() {
        if (inputHandler.isMoveUp()) {
            y -= speed;
        }

        if (inputHandler.isMoveDown()) {
            y += speed;
        }

        if (inputHandler.isMoveLeft()) {
            x -= speed;
        }

        if (inputHandler.isMoveRight()) {
            x += speed;
        }
    }

    /**
     * Не дозволяє гравцю вийти за межі ігрової арени.
     */
    private void keepInsideArena() {
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        if (x + width > GameConstants.WINDOW_WIDTH) {
            x = GameConstants.WINDOW_WIDTH - width;
        }

        if (y + height > GameConstants.WINDOW_HEIGHT) {
            y = GameConstants.WINDOW_HEIGHT - height;
        }
    }

    /**
     * Малює гравця.
     *
     * Поки використовується прямокутник, але метод залишено окремо,
     * щоб у майбутньому було простіше перейти на спрайт персонажа.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }

    /**
     * Завдає гравцю шкоду, якщо минув час невразливості після попереднього удару.
     *
     * Це потрібно, щоб при зіткненні з ворогом здоров'я не знімалося кожен кадр.
     *
     * @param damage кількість шкоди
     */
    public void takeDamage(int damage) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastDamageTime >= GameConstants.PLAYER_DAMAGE_COOLDOWN) {
            currentHealth -= damage;
            lastDamageTime = currentTime;

            if (currentHealth < 0) {
                currentHealth = 0;
            }

            System.out.println("Player HP: " + currentHealth + "/" + maxHealth);
        }
    }

    /**
     * Повертає поточне здоров'я гравця.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Повертає максимальне здоров'я гравця.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Перевіряє, чи гравець ще живий.
     *
     * Якщо здоров'я більше нуля — гра продовжується.
     * Якщо здоров'я дорівнює нулю — можна завершувати ігрову сесію.
     */
    public boolean isAlive() {
        return currentHealth > 0;
    }

}