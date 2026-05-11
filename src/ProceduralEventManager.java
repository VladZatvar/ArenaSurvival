/**
 * Клас керує процедурними ігровими подіями.
 *
 * ProceduralEventManager аналізує час виживання гравця
 * і автоматично запускає тимчасові події під час сесії.
 *
 * На цьому етапі реалізована подія Enemy Rush:
 * протягом короткого часу вороги з'являються частіше.
 */
public class ProceduralEventManager {

    private final GameSession gameSession;

    // Поточна активна подія.
    private GameEventType currentEvent;

    // Час початку активної події.
    private long eventStartTime;

    // Час останнього запуску події.
    private long lastEventTime;

    /**
     * Створює менеджер процедурних подій.
     *
     * @param gameSession поточна ігрова сесія, з якої береться час виживання
     */
    public ProceduralEventManager(GameSession gameSession) {
        this.gameSession = gameSession;
        this.currentEvent = GameEventType.NONE;
        this.eventStartTime = 0;
        this.lastEventTime = System.currentTimeMillis();
    }

    /**
     * Оновлює стан процедурних подій.
     *
     * Якщо подія неактивна і минув потрібний інтервал,
     * запускається нова подія.
     *
     * Якщо подія активна і її тривалість завершилася,
     * подія вимикається.
     */
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (currentEvent == GameEventType.NONE) {
            tryStartEnemyRush(currentTime);
        } else {
            checkEventEnd(currentTime);
        }
    }

    /**
     * Перевіряє, чи можна запустити подію Enemy Rush.
     *
     * Подія запускається не одразу на старті гри,
     * а після певного часу виживання та з інтервалом між подіями.
     */
    private void tryStartEnemyRush(long currentTime) {
        long survivalTime = gameSession.getSurvivalTimeSeconds();

        if (survivalTime < GameConstants.EVENT_FIRST_START_TIME) {
            return;
        }

        if (currentTime - lastEventTime >= GameConstants.EVENT_INTERVAL) {
            currentEvent = GameEventType.ENEMY_RUSH;
            eventStartTime = currentTime;
            lastEventTime = currentTime;

            System.out.println("Event started: ENEMY_RUSH");
        }
    }

    /**
     * Перевіряє, чи активна подія вже має завершитися.
     */
    private void checkEventEnd(long currentTime) {
        if (currentTime - eventStartTime >= GameConstants.EVENT_DURATION) {
            System.out.println("Event ended: " + currentEvent);

            currentEvent = GameEventType.NONE;
            eventStartTime = 0;
        }
    }

    /**
     * Повертає поточний тип активної події.
     */
    public GameEventType getCurrentEvent() {
        return currentEvent;
    }

    /**
     * Перевіряє, чи зараз активна подія Enemy Rush.
     */
    public boolean isEnemyRushActive() {
        return currentEvent == GameEventType.ENEMY_RUSH;
    }

    /**
     * Повертає назву активної події для HUD.
     */
    public String getCurrentEventName() {
        if (currentEvent == GameEventType.ENEMY_RUSH) {
            return "Enemy Rush";
        }

        return "None";
    }
}