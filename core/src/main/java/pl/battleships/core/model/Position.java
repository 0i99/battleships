package pl.battleships.core.model;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Position {
    private Integer x;
    private Integer y;
    private boolean hit;
}
