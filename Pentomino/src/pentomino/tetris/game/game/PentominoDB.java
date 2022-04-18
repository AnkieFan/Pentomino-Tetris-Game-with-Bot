package game;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PentominoDB {

    /**
     * When you want to draw a pentomino:
     * E.g.: the first grid: draw at (points[0].x, points[0],y) in panel
     */
    private static final Point[][] PENT_DATA = {
            new Point[]{                  // L
                    new Point(1, 1),
                    new Point(0, 0),
                    new Point(0, 1),
                    new Point(2, 1),
                    new Point(3, 1)
            },
            new Point[]{                  // F
                    new Point(2, 1),
                    new Point(2, 0),
                    new Point(3, 0),
                    new Point(1, 1),
                    new Point(2, 2)
            },

            new Point[]{                  // X
                    new Point(2, 1),
                    new Point(2, 0),
                    new Point(1, 1),
                    new Point(3, 1),
                    new Point(2, 2)
            },
            new Point[]{                  // T
                    new Point(2, 1),
                    new Point(1, 0),
                    new Point(2, 0),
                    new Point(3, 0),
                    new Point(2, 2)
            },
            new Point[]{                  // I
                    new Point(2, 0),
                    new Point(1, 0),
                    new Point(3, 0),
                    new Point(4, 0),
                    new Point(0, 0)
            },
            new Point[]{                 // V
                    new Point(1, 2),
                    new Point(1, 0),
                    new Point(1, 1),
                    new Point(2, 2),
                    new Point(3, 2)
            },
            new Point[]{                  // U
                    new Point(2, 0),
                    new Point(1, 0),
                    new Point(3, 0),
                    new Point(1, 1),
                    new Point(3, 1)
            },
            new Point[]{                  // N
                    new Point(1, 1),
                    new Point(0, 0),
                    new Point(1, 0),
                    new Point(2, 1),
                    new Point(3, 1)
            },
            new Point[]{                  // W
                    new Point(2, 1),
                    new Point(1, 0),
                    new Point(1, 1),
                    new Point(2, 2),
                    new Point(3, 2)
            },
            new Point[]{                  // P
                    new Point(2, 1),
                    new Point(1, 0),
                    new Point(1, 1),
                    new Point(2, 0),
                    new Point(1, 2)
            },
            new Point[]{                  // Z
                    new Point(2, 1),
                    new Point(1, 0),
                    new Point(2, 0),
                    new Point(2, 2),
                    new Point(3, 2)
            },
            new Point[]{                  // Y
                    new Point(2, 1),
                    new Point(2, 0),
                    new Point(2, 2),
                    new Point(1, 1),
                    new Point(2, 3)
            },
    };

    /**
     * Turn array to be a list, store 12 pentominos
     */
    protected static List<Point[]> PENT_LIST = Arrays.asList(PENT_DATA);

    /**
     * Once this PentominoDB is loaded in JVM, this static code block will disrupt the order of these 12 pentominos.
     * In this way, the order of these 12 pentominos will become random.
     * In the game, we just need to make it drop from the first to the twelfth, and it will appear randomly and not repeat.
     */
    static {
        Collections.shuffle(PENT_LIST);
    }

    /**
     * Do the shuffle (for 12-bag rule)
     */
    public static void shufflePent() {
        Collections.shuffle(PENT_LIST);
    }

}
