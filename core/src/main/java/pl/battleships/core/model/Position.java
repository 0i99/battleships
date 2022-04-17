package pl.battleships.core.model;


import lombok.*;


@Builder
@Getter
@Setter
public class Position {
    private Integer x;
    private Integer y;
    private boolean hit;
}
