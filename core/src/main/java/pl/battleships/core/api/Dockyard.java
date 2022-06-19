package pl.battleships.core.api;


import pl.battleships.core.model.Position;
import pl.battleships.core.model.Ship;

import java.util.ArrayList;
import java.util.List;

enum Dockyard {
    INSTANCE;

    Ship build(Ship.Type type) {
        return Ship.builder()
                .type(type.getValue())
                .location(new ArrayList<>())
                .build();
    }

    Ship build(Ship.Type type, List<Position> location) {
        return Ship.builder()
                .type(type.getValue())
                .location(location)
                .build();
    }

    /**
     * Build ship with specific size
     *
     * @param size
     * @return
     */
    public Ship build(Integer size) {
        return Ship.builder().type(size).location(new ArrayList<>()).build();
    }

}
