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
 * Також тут обробляються основні стани гри:
 * гра триває, пауза або завершення сесії.
 */
public class GamePanel extends JPanel {

    private final InputHandler inputHandler;
    private Player player;
    private MeleeAttack meleeAttack;
    private EnemySpawner enemySpawner;
    private GameSession gameSession;
    private DifficultyManager difficultyManager;
    private ProceduralEventManager proceduralEventManager;
    private final List<Enemy> enemies;

    private final Timer gameTimer;

    // Поточний стан гри: RUNNING, PAUSED або GAME_OVER.
    private GameState gameState;

    // Службовий режим для тестування ігрових механік.
    private boolean godModeEnabled;

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
        gameSession = new GameSession();
        difficultyManager = new DifficultyManager(gameSession);
        proceduralEventManager = new ProceduralEventManager(gameSession);

        enemies = new ArrayList<>();
        createEnemies();

        enemySpawner = new EnemySpawner(enemies, player, difficultyManager, proceduralEventManager);

        gameState = GameState.RUNNING;
        godModeEnabled = false;

        gameTimer = new Timer(GameConstants.GAME_TIMER_DELAY, e -> {
            handleGlobalInput();

            if (gameState == GameState.RUNNING) {
                updateGame();
            }

            repaint();
        });

        // Запускаємо таймер, який оновлює гру приблизно 60 разів на секунду.
        gameTimer.start();
    }

    /**
     * Обробляє глобальні клавіші, які працюють незалежно від руху гравця.
     *
     * P — пауза або продовження гри.
     * G — увімкнення або вимкнення God Mode.
     * R — рестарт після Game Over.
     */
    private void handleGlobalInput() {
        if (inputHandler.consumePausePressed()) {
            togglePause();
        }

        if (inputHandler.consumeGodModePressed()) {
            toggleGodMode();
        }

        if (gameState == GameState.GAME_OVER && inputHandler.consumeRestartPressed()) {
            restartGame();
        }
    }

    /**
     * Перемикає гру між активним станом і паузою.
     *
     * Під час паузи ігрові об'єкти не оновлюються,
     * а таймер виживання тимчасово зупиняється.
     */
    private void togglePause() {
        if (gameState == GameState.RUNNING) {
            gameState = GameState.PAUSED;
            gameSession.pause();
            inputHandler.resetMovement();
        } else if (gameState == GameState.PAUSED) {
            gameState = GameState.RUNNING;
            gameSession.resume();
        }
    }

    /**
     * Вмикає або вимикає службовий режим God Mode.
     *
     * У цьому режимі гравець не отримує шкоду,
     * а атака знищує ворогів з одного удару.
     */
    private void toggleGodMode() {
        godModeEnabled = !godModeEnabled;
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
        proceduralEventManager.update();
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
     * Якщо God Mode вимкнено, вороги можуть завдавати шкоду гравцю.
     * Якщо God Mode увімкнено, гравець залишається невразливим.
     */
    private void checkPlayerEnemyCollisions() {
        if (godModeEnabled) {
            return;
        }

        for (Enemy enemy : enemies) {
            if (player.intersects(enemy)) {
                player.takeDamage(enemy.getDamage());
            }
        }
    }

    /**
     * Перевіряє, чи ближня атака гравця зачепила ворогів.
     *
     * У звичайному режимі вороги отримують стандартну шкоду.
     * У God Mode атака наносить дуже велику шкоду, щоб швидко тестувати гру.
     */
    private void checkAttackHits() {
        if (!meleeAttack.canDamage()) {
            return;
        }

        int attackDamage = meleeAttack.getDamage();

        if (godModeEnabled) {
            attackDamage = GameConstants.GOD_MODE_ATTACK_DAMAGE;
        }

        for (Enemy enemy : enemies) {
            if (meleeAttack.intersects(enemy)) {
                enemy.takeDamage(attackDamage);
            }
        }

        meleeAttack.markDamageApplied();

        // Рахуємо переможених ворогів перед видаленням зі списку.
        int defeatedEnemies = 0;

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                defeatedEnemies++;
            }
        }

        for (int i = 0; i < defeatedEnemies; i++) {
            gameSession.addKill();
        }

        // Видаляємо ворогів, здоров'я яких стало нульовим.
        enemies.removeIf(enemy -> !enemy.isAlive());
    }

    /**
     * Перевіряє, чи потрібно завершити гру.
     *
     * Якщо здоров'я гравця стало нульовим, гра переходить у стан Game Over,
     * а таймер сесії фіксує остаточний час виживання.
     */
    private void checkGameOver() {
        if (!player.isAlive()) {
            gameState = GameState.GAME_OVER;
            gameSession.finish();
            inputHandler.resetMovement();
        }
    }

    /**
     * Перезапускає ігрову сесію.
     *
     * Метод повертає гравця у стартову позицію, очищає старих ворогів,
     * створює нових ворогів і повертає гру у стан RUNNING.
     */
    private void restartGame() {
        inputHandler.resetMovement();

        player = new Player(380, 280, inputHandler);
        meleeAttack = new MeleeAttack(player);
        gameSession = new GameSession();
        difficultyManager = new DifficultyManager(gameSession);
        proceduralEventManager = new ProceduralEventManager(gameSession);

        enemies.clear();
        createEnemies();

        enemySpawner = new EnemySpawner(enemies, player, difficultyManager, proceduralEventManager);
        enemySpawner.reset();

        gameState = GameState.RUNNING;
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

        if (gameState == GameState.PAUSED) {
            drawPauseMenu(g);
        }

        if (gameState == GameState.GAME_OVER) {
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
     * здоров'я, кількість ворогів, час, складність і God Mode.
     */
    private void drawHud(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        String healthText = "HP: " + player.getCurrentHealth() + "/" + player.getMaxHealth();
        g.drawString(healthText, 20, 30);

        String enemiesText = "Enemies: " + enemies.size();
        g.drawString(enemiesText, 20, 55);

        String killsText = "Kills: " + gameSession.getKillCount();
        g.drawString(killsText, 20, 80);

        String timeText = "Time: " + gameSession.getFormattedSurvivalTime();
        g.drawString(timeText, 20, 105);

        String difficultyText = "Difficulty: " + difficultyManager.getDifficultyName();
        g.drawString(difficultyText, 20, 130);

        String godModeText = "God Mode: " + (godModeEnabled ? "ON" : "OFF");
        g.drawString(godModeText, 20, 155);

        String eventText = "Event: " + proceduralEventManager.getCurrentEventName();
        g.drawString(eventText, 20, 180);

        String controlsText = "P - Pause | G - God Mode";
        g.drawString(controlsText, 20, GameConstants.WINDOW_HEIGHT - 20);
    }

    /**
     * Малює повідомлення паузи.
     *
     * Під час паузи ігровий світ не оновлюється,
     * а час виживання тимчасово зупиняється.
     */
    private void drawPauseMenu(Graphics g) {
        g.setColor(Color.WHITE);

        String title = "PAUSED";
        g.setFont(new Font("Arial", Font.BOLD, 48));

        int titleWidth = g.getFontMetrics().stringWidth(title);
        int titleX = (GameConstants.WINDOW_WIDTH - titleWidth) / 2;
        int titleY = GameConstants.WINDOW_HEIGHT / 2;

        g.drawString(title, titleX, titleY);

        String continueText = "Press P to continue";
        g.setFont(new Font("Arial", Font.BOLD, 22));

        int continueWidth = g.getFontMetrics().stringWidth(continueText);
        int continueX = (GameConstants.WINDOW_WIDTH - continueWidth) / 2;
        int continueY = titleY + 45;

        g.drawString(continueText, continueX, continueY);
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