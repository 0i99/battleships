package pl.battleships.javaspringship.service;

import pl.battleships.api.model.Position;
import pl.battleships.api.model.Ship;

import java.util.ArrayList;
import java.util.List;

public enum Dockyard {
    INSTANCE;

    /**
     * Build ship with type {@link Ship.TypeEnum}
     *
     * @param type
     * @return
     */
    public Ship build(Ship.TypeEnum type) {
        return new Ship().type(type).locaction(new ArrayList<>());
    }

    public Ship build(Ship.TypeEnum type, List<Position> location) {
        return new Ship().type(type).locaction(location);
    }

    /**
     * Build ship with specific size
     *
     * @param size
     * @return
     */
    public Ship build(Integer size) {
        return new Ship().type(Ship.TypeEnum.fromValue(size)).locaction(new ArrayList<>());
    }

}
