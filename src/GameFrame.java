import javax.swing.JFrame;

/**
 * Клас головного вікна гри.
 *
 * GameFrame відповідає за створення окремого вікна застосунку,
 * у якому відображається ігрова панель GamePanel.
 *
 * У цьому класі не зберігається логіка гри. Він лише налаштовує вікно:
 * назву, розмір, поведінку при закритті та розміщення на екрані.
 */
public class GameFrame extends JFrame {

    /**
     * Створює головне вікно гри та додає до нього ігрову панель.
     */
    public GameFrame() {
        setTitle("Arena Survival Prototype");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // GamePanel містить саму ігрову сцену: арену, гравця, ворогів і цикл гри.
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        // Автоматично підганяє розмір вікна під розмір GamePanel.
        pack();

        // Розміщує вікно по центру екрана.
        setLocationRelativeTo(null);
    }

    /**
     * Робить вікно видимим для користувача.
     */
    public void start() {
        setVisible(true);
    }
}