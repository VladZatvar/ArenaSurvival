import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Клас описує ближню автоатаку гравця.
 *
 * Атака працює як короткий замах перед персонажем.
 * На екрані вона відображається як напівпрозора сіра область.
 *
 * Якщо ворог потрапляє в цю область під час активної атаки,
 * він отримує шкоду.
 */
public class MeleeAttack extends GameObject {

    private final Player player;

    // Час останнього запуску атаки.
    private long lastAttackTime;

    // Час початку поточного замаху.
    private long attackStartTime;

    // Чи активна зараз зона удару.
    private boolean active;

    // Чи вже була нанесена шкода під час цього замаху.
    private boolean damageApplied;

    /**
     * Створює ближню атаку, прив'язану до конкретного гравця.
     *
     * @param player гравець, навколо якого створюється зона удару
     */
    public MeleeAttack(Player player) {
        super(
                0,
                0,
                0,
                0,
                GameConstants.ATTACK_COLOR
        );

        this.player = player;
        this.lastAttackTime = 0;
        this.attackStartTime = 0;
        this.active = false;
        this.damageApplied = false;
    }

    /**
     * Оновлює стан атаки на кожному кадрі.
     *
     * Якщо cooldown минув — запускається новий замах.
     * Якщо тривалість замаху завершилася — атака вимикається.
     */
    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (!active && currentTime - lastAttackTime >= GameConstants.PLAYER_ATTACK_COOLDOWN) {
            startAttack(currentTime);
        }

        if (active && currentTime - attackStartTime >= GameConstants.PLAYER_ATTACK_DURATION) {
            active = false;
        }
    }

    /**
     * Запускає новий замах мечем.
     *
     * Позиція зони удару залежить від напрямку,
     * у який зараз дивиться гравець.
     */
    private void startAttack(long currentTime) {
        active = true;
        damageApplied = false;

        lastAttackTime = currentTime;
        attackStartTime = currentTime;

        updateAttackHitbox();
    }

    /**
     * Розраховує прямокутну зону удару перед гравцем.
     *
     * Поки атака зроблена простим прямокутником.
     * Пізніше її можна замінити на дугу, спрайт меча або анімацію.
     */
    private void updateAttackHitbox() {
        int range = GameConstants.PLAYER_ATTACK_RANGE;
        int attackWidth = GameConstants.PLAYER_ATTACK_WIDTH;

        Direction direction = player.getFacingDirection();

        if (direction == Direction.RIGHT) {
            x = player.getX() + player.getWidth();
            y = player.getCenterY() - attackWidth / 2;
            width = range;
            height = attackWidth;
        }

        if (direction == Direction.LEFT) {
            x = player.getX() - range;
            y = player.getCenterY() - attackWidth / 2;
            width = range;
            height = attackWidth;
        }

        if (direction == Direction.UP) {
            x = player.getCenterX() - attackWidth / 2;
            y = player.getY() - range;
            width = attackWidth;
            height = range;
        }

        if (direction == Direction.DOWN) {
            x = player.getCenterX() - attackWidth / 2;
            y = player.getY() + player.getHeight();
            width = attackWidth;
            height = range;
        }
    }

    /**
     * Малює активну зону атаки.
     *
     * Якщо атака неактивна, нічого не малюється.
     */
    @Override
    public void draw(Graphics g) {
        if (!active) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();

        // Напівпрозора сіра зона удару.
        g2d.setColor(GameConstants.ATTACK_COLOR);
        g2d.fillRect(x, y, width, height);

        // Світла рамка, щоб атаку було краще видно.
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, width, height);

        g2d.dispose();
    }

    /**
     * Перевіряє, чи атака зараз може завдати шкоди.
     *
     * За один замах шкода наноситься лише один раз,
     * щоб ворог не отримував урон кожен кадр.
     */
    public boolean canDamage() {
        return active && !damageApplied;
    }

    /**
     * Позначає, що шкода під час цього замаху вже була нанесена.
     */
    public void markDamageApplied() {
        damageApplied = true;
    }

    /**
     * Повертає кількість шкоди від атаки гравця.
     */
    public int getDamage() {
        return GameConstants.PLAYER_ATTACK_DAMAGE;
    }
}