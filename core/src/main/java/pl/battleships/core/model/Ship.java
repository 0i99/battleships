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
        Carrier(5),

        Battleship(4),

        Cruiser(3),

        Submarine(2),

        Destroyer(1);

        private Integer value;

        Type(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static Type fromValue(Integer value) {
            for (Type b : Type.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
}
