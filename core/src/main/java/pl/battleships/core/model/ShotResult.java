package pl.battleships.core.model;

public enum ShotResult {
    MISSED("missed"),

    HIT("hit"),

    DESTROYED("destroyed"),

    ALL_DESTROYED("all_destroyed");

    private final String value;

    ShotResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ShotResult fromValue(String value) {
        for (ShotResult b : ShotResult.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
