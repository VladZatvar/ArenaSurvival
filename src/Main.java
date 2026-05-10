/**
 * Головний клас програми.
 *
 * Саме з цього класу починається запуск гри.
 * Тут створюється головне вікно GameFrame і запускається відображення прототипу.
 */

public class Main {

    /**
     * Точка входу в програму.
     *
     * Метод main автоматично викликається Java під час запуску застосунку.
     */
    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.start();
    }
}