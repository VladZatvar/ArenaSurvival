import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Головна ігрова панель.
 *
 * GamePanel відповідає за ігрове поле, запуск ігрового циклу,
 * оновлення об'єктів та їх відображення на екрані.
 *
 * Важливо: сама логіка гравця, ворогів і атаки винесена в окремі класи.
 * Це робить код більш зрозумілим і відповідає принципам ООП.
 */
public class GamePanel extends JPanel {

    private final InputHandler inputHandler;
    private Player player;
    private MeleeAttack meleeAttack;
    private EnemySpawner enemySpawner;
    private final List<Enemy> enemies;

    private final Timer gameTimer;

    // Прапорець, який показує, чи завершилася гра.
    private boolean gameOver;

    /**
     * Створює ігрову панель, гравця, ворогів, атаку та запускає ігровий цикл.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));
        setBackground(GameConstants.ARENA_COLOR);

        inputHandler = new InputHandler();

        // Дозволяємо панелі отримувати натискання клавіш.
        setFocusable(true);
        addKeyListener(inputHandler);

        player = new Player(380, 280, inputHandler);
        meleeAttack = new MeleeAttack(player);

        enemies = new ArrayList<>();
        createEnemies();

        enemySpawner = new EnemySpawner(enemies, player);

        gameOver = false;

        gameTimer = new Timer(GameConstants.GAME_TIMER_DELAY, e -> {
            if (!gameOver) {
                updateGame();
            } else {
                checkRestart();
            }

            repaint();
        });

        // Запускаємо таймер, який оновлює гру приблизно 60 разів на секунду.
        gameTimer.start();
    }

    /**
     * Створює початкових ворогів на арені.
     *
     * Поки координати задані вручну, але пізніше тут можна зробити
     * процедурне створення ворогів у випадкових точках карти.
     */
    private void createEnemies() {
        enemies.add(new Enemy(100, 100, player));
        enemies.add(new Enemy(650, 120, player));
        enemies.add(new Enemy(150, 450, player));
        enemies.add(new Enemy(600, 420, player));
    }

    /**
     * Оновлює стан гри на кожному кадрі.
     */
    private void updateGame() {
        player.update();
        meleeAttack.update();
        enemySpawner.update();

        for (Enemy enemy : enemies) {
            enemy.update();
        }

        separateEnemies();
        checkPlayerEnemyCollisions();
        checkAttackHits();
        checkGameOver();
    }

    /**
     * Не дозволяє ворогам повністю накладатися один на одного.
     *
     * Метод проходить по всіх парах ворогів і, якщо вони перетинаються,
     * трохи розсуває їх у різні боки.
     */
    private void separateEnemies() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy firstEnemy = enemies.get(i);

            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy secondEnemy = enemies.get(j);

                if (firstEnemy.intersects(secondEnemy)) {
                    pushEnemiesApart(firstEnemy, secondEnemy);
                }
            }
        }
    }

    /**
     * Розсуває двох ворогів, які перетнулися.
     *
     * Порівнюємо центри ворогів і зміщуємо їх у протилежні боки.
     */
    private void pushEnemiesApart(Enemy firstEnemy, Enemy secondEnemy) {
        int firstCenterX = firstEnemy.getCenterX();
        int firstCenterY = firstEnemy.getCenterY();

        int secondCenterX = secondEnemy.getCenterX();
        int secondCenterY = secondEnemy.getCenterY();

        int pushAmount = 2;

        if (firstCenterX < secondCenterX) {
            firstEnemy.setX(firstEnemy.getX() - pushAmount);
            secondEnemy.setX(secondEnemy.getX() + pushAmount);
        } else {
            firstEnemy.setX(firstEnemy.getX() + pushAmount);
            secondEnemy.setX(secondEnemy.getX() - pushAmount);
        }

        if (firstCenterY < secondCenterY) {
            firstEnemy.setY(firstEnemy.getY() - pushAmount);
            secondEnemy.setY(secondEnemy.getY() + pushAmount);
        } else {
            firstEnemy.setY(firstEnemy.getY() + pushAmount);
            secondEnemy.setY(secondEnemy.getY() - pushAmount);
        }
    }

    /**
     * Перевіряє зіткнення між гравцем і ворогами.
     *
     * Якщо ворог торкається гравця, гравець отримує шкоду.
     */
    private void checkPlayerEnemyCollisions() {
        for (Enemy enemy : enemies) {
            if (player.intersects(enemy)) {
                player.takeDamage(enemy.getDamage());
            }
        }
    }

    /**
     * Перевіряє, чи ближня атака гравця зачепила ворогів.
     *
     * За один замах шкода наноситься один раз.
     * Після цього мертві вороги видаляються зі списку.
     */
    private void checkAttackHits() {
        if (!meleeAttack.canDamage()) {
            return;
        }

        for (Enemy enemy : enemies) {
            if (meleeAttack.intersects(enemy)) {
                enemy.takeDamage(meleeAttack.getDamage());
            }
        }

        meleeAttack.markDamageApplied();

        // Видаляємо ворогів, здоров'я яких стало нульовим.
        enemies.removeIf(enemy -> !enemy.isAlive());
    }

    /**
     * Перевіряє, чи потрібно завершити гру.
     *
     * Якщо здоров'я гравця стало нульовим, гра переходить у стан Game Over.
     */
    private void checkGameOver() {
        if (!player.isAlive()) {
            gameOver = true;
        }
    }

    /**
     * Перевіряє, чи користувач хоче перезапустити гру після Game Over.
     *
     * Якщо гра завершена і натиснуто клавішу R,
     * створюється новий гравець, вороги та початковий стан гри.
     */
    private void checkRestart() {
        if (inputHandler.consumeRestartPressed()) {
            restartGame();
        }
    }

    /**
     * Перезапускає ігрову сесію.
     *
     * Метод повертає гравця у стартову позицію, очищає старих ворогів,
     * створює нових ворогів і знімає стан Game Over.
     */
    private void restartGame() {
        inputHandler.resetMovement();

        player = new Player(380, 280, inputHandler);
        meleeAttack = new MeleeAttack(player);

        enemies.clear();
        createEnemies();

        enemySpawner = new EnemySpawner(enemies, player);
        enemySpawner.reset();

        gameOver = false;
    }

    /**
     * Малює арену, гравця, ворогів, атаку та інтерфейс.
     *
     * Метод автоматично викликається Swing після repaint().
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawArena(g);
        drawGameObjects(g);
        drawHud(g);

        if (gameOver) {
            drawGameOver(g);
        }
    }

    /**
     * Малює фон ігрової арени.
     */
    private void drawArena(Graphics g) {
        g.setColor(GameConstants.ARENA_COLOR);
        g.fillRect(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
    }

    /**
     * Малює всі активні ігрові об'єкти.
     */
    private void drawGameObjects(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        player.draw(g);

        // Малюємо атаку поверх гравця і ворогів, щоб її було видно.
        meleeAttack.draw(g);
    }

    /**
     * Малює простий ігровий інтерфейс поверх арени.
     *
     * HUD показує важливу інформацію для гравця:
     * поточне здоров'я, максимальне здоров'я та кількість ворогів.
     */
    private void drawHud(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        String healthText = "HP: " + player.getCurrentHealth() + "/" + player.getMaxHealth();
        g.drawString(healthText, 20, 30);

        String enemiesText = "Enemies: " + enemies.size();
        g.drawString(enemiesText, 20, 55);
    }

    /**
     * Малює повідомлення про завершення гри.
     *
     * Поки це простий текст по центру екрана.
     * Також показується підказка для перезапуску гри.
     */
    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);

        String title = "GAME OVER";
        g.setFont(new Font("Arial", Font.BOLD, 48));

        int titleWidth = g.getFontMetrics().stringWidth(title);
        int titleX = (GameConstants.WINDOW_WIDTH - titleWidth) / 2;
        int titleY = GameConstants.WINDOW_HEIGHT / 2;

        g.drawString(title, titleX, titleY);

        String restartText = "Press R to restart";
        g.setFont(new Font("Arial", Font.BOLD, 22));

        int restartWidth = g.getFontMetrics().stringWidth(restartText);
        int restartX = (GameConstants.WINDOW_WIDTH - restartWidth) / 2;
        int restartY = titleY + 45;

        g.drawString(restartText, restartX, restartY);
    }
}