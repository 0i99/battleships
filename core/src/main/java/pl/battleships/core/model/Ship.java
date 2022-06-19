package pl.battleships.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Ship {
    private boolean destroyed;
    private List<Position> location;
    private int type;

    public enum Type {
        CARRIER(5),

        BATTLESHIP(4),

        CRUISER(3),

        SUBMARINE(2),

        DESTROYER(1);

        private Integer value;

        Type(Integer value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static Type fromValue(int value) {
            for (Type b : Type.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
}
