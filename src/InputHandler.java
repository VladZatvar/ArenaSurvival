import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Клас відповідає за обробку натискань клавіш.
 *
 * Він зберігає інформацію про те, які клавіші руху зараз натиснуті.
 * Завдяки цьому Player не працює напряму з клавіатурою, а просто питає
 * у InputHandler, у який бік потрібно рухатися.
 */
public class InputHandler implements KeyListener {

    // Прапорці напрямків руху. true означає, що клавіша зараз натиснута.
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    // Прапорець запиту на перезапуск гри.
    private boolean restartPressed;

    // Прапорець запиту на паузу або продовження гри.
    private boolean pausePressed;

    // Прапорець запиту на увімкнення або вимкнення God Mode.
    private boolean godModePressed;

    /**
     * Викликається, коли користувач натискає клавішу.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            moveUp = true;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            moveDown = true;
        }

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            moveLeft = true;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }

        if (key == KeyEvent.VK_R) {
            restartPressed = true;
        }

        if (key == KeyEvent.VK_P) {
            pausePressed = true;
        }

        if (key == KeyEvent.VK_G) {
            godModePressed = true;
        }

    }

    /**
     * Викликається, коли користувач відпускає клавішу.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            moveUp = false;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            moveDown = false;
        }

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // У цій грі не використовується, але метод потрібен для KeyListener.
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    /**
     * Перевіряє, чи була натиснута клавіша перезапуску гри.
     *
     * Метод працює як "одноразове читання":
     * якщо R була натиснута, метод повертає true і одразу скидає прапорець.
     */
    public boolean consumeRestartPressed() {
        if (restartPressed) {
            restartPressed = false;
            return true;
        }

        return false;
    }

    /**
     * Скидає всі клавіші руху.
     *
     * Це корисно під час перезапуску гри, щоб після рестарту
     * персонаж не почав одразу рухатися через старий стан клавіш.
     */
    public void resetMovement() {
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
    }

    /**
     * Перевіряє, чи була натиснута клавіша паузи.
     *
     * Метод працює як одноразове читання:
     * якщо P була натиснута, повертається true і прапорець скидається.
     */
    public boolean consumePausePressed() {
        if (pausePressed) {
            pausePressed = false;
            return true;
        }

        return false;
    }

    /**
     * Перевіряє, чи була натиснута клавіша God Mode.
     *
     * Метод працює як одноразове читання:
     * якщо G була натиснута, повертається true і прапорець скидається.
     */
    public boolean consumeGodModePressed() {
        if (godModePressed) {
            godModePressed = false;
            return true;
        }

        return false;
    }
}