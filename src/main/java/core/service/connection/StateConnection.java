package core.service.connection;

/**
 * Состояние подключения
 */
public enum StateConnection {
    off,
    blockConnecting,
    blockEstablished,
    localConnecting,
    localEstablished
}
