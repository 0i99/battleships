package pl.battleships.core.model;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

@RequiredArgsConstructor
public class TwoDimensionalBoard {
    private int MISSED_SHOT_MARKER = 0;
    private final int size;
    private final Integer[][] positions;

    public TwoDimensionalBoard(Integer size) {
        this.size = size;
        this.positions = new Integer[size][size];
    }

    public Integer[][] getPositions() {
        return positions;
    }

    public int getSize() {
        return size;
    }

    public Integer[][] setOnBoard(int x, int y, int type) {
        positions[x][y] = type;
        return positions;
    }

    public Integer[][] missedShot(int x, int y) {
        positions[x][y] = MISSED_SHOT_MARKER;
        return positions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < positions.length; ++i) {
            sb.append("|");
            for (int j = 0; j < positions[i].length; ++j) {
                if (positions[i][j] == null) {
                    sb.append("  ");
                } else {
                    sb.append(positions[i][j] >= 0 ? " " : "");
                    sb.append(positions[i][j]);
                }

                sb.append("|");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getHash() {
        return DigestUtils.sha256Hex(this.toString());
    }

}
