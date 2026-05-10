import java.awt.Color;
import java.awt.Graphics;

/**
 * Абстрактний базовий клас для всіх об'єктів гри.
 *
 * Його задача — зберігати спільні характеристики:
 * координати, розмір і колір об'єкта.
 *
 * Від цього класу будуть наслідуватися конкретні ігрові сутності:
 * гравець, вороги, снаряди, бонуси тощо.
 */
public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Color color;

    /**
     * Створює базовий ігровий об'єкт із заданими параметрами.
     *
     * @param x координата об'єкта по горизонталі
     * @param y координата об'єкта по вертикалі
     * @param width ширина об'єкта
     * @param height висота об'єкта
     * @param color колір об'єкта на екрані
     */
    public GameObject(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    /**
     * Оновлює стан об'єкта на кожному кадрі гри.
     *
     * Метод абстрактний, тому кожен нащадок сам визначає,
     * як саме він оновлюється.
     */
    public abstract void update();

    /**
     * Малює об'єкт на ігровій панелі.
     *
     * Зараз об'єкти відображаються як прості прямокутники.
     * У майбутньому цей метод можна замінити на малювання спрайтів.
     *
     * @param g графічний контекст для малювання
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
     * Повертає координату X об'єкта.
     */
    public int getX() {
        return x;
    }

    /**
     * Повертає координату Y об'єкта.
     */
    public int getY() {
        return y;
    }

    /**
     * Задає нову координату X об'єкта.
     *
     * Потрібно для простих систем корекції позиції,
     * наприклад, щоб вороги не накладалися один на одного.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Задає нову координату Y об'єкта.
     *
     * Потрібно для простих систем корекції позиції,
     * наприклад, щоб вороги не накладалися один на одного.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Повертає ширину об'єкта.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Повертає висоту об'єкта.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Повертає координату центру об'єкта по X.
     *
     * Це зручно для розрахунку руху ворога до центру гравця.
     */
    public int getCenterX() {
        return x + width / 2;
    }

    /**
     * Повертає координату центру об'єкта по Y.
     *
     * Це зручно для розрахунку руху ворога до центру гравця.
     */
    public int getCenterY() {
        return y + height / 2;
    }

    /**
     * Перевіряє, чи перетинається поточний об'єкт з іншим ігровим об'єктом.
     *
     * Такий метод потрібен для обробки зіткнень:
     * наприклад, гравець торкнувся ворога або снаряд влучив у ціль.
     *
     * @param other інший ігровий об'єкт
     * @return true, якщо об'єкти перетинаються
     */
    public boolean intersects(GameObject other) {
        return x < other.x + other.width
                && x + width > other.x
                && y < other.y + other.height
                && y + height > other.y;
    }
}