/**
 * The database of parcels
 * Numbered all the cube along to length, width then height.
 * Contains all the rotation in space
 */
public class DLXParcelDB {
    private int length;
    private int width;

    public int[][] parcels;

    public DLXParcelDB(int length, int width) {
        this.length = length;
        this.width = width;
        parcels = new int[][]{
                new int[]{3,
                        1, 2, 3, length + 1, length + 2, length + 3, length * 2 + 1, length * 2 + 2, length * 2 + 3, length * 3 + 1, length * 3 + 2, length * 3 + 3,
                        length * width + 1, length * width + 2, length * width + 3,
                        length * width + length + 1, length * width + length + 2, length * width + length + 3,
                        length * width + length * 2 + 1, length * width + length * 2 + 2, length * width + length * 2 + 3,
                        length * width + length * 3 + 1, length * width + length * 3 + 2, length * width + length * 3 + 3,
                },//3*4
                new int[]{3,
                        1, 2, length + 1, length + 2, 2 * length + 1, 2 * length + 2,
                        length * width + 1, length * width + 2, length * width + length + 1, length * width + length + 2, length * width + 2 * length + 1, length * width + 2 * length + 2,
                        2 * length * width + 1, 2 * length * width + 2, 2 * length * width + length + 1, 2 * length * width + length + 2, 2 * length * width + 2 * length + 1, 2 * length * width + 2 * length + 2,
                        3 * length * width + 1, 3 * length * width + 2, 3 * length * width + length + 1, 3 * length * width + length + 2, 3 * length * width + 2 * length + 1, 3 * length * width + 2 * length + 2,
                },//2*3
                new int[]{3,
                        1, 2, 3, 4, length + 1, length + 2, length + 3, length + 4,
                        length * width + 1, length * width + 2, length * width + 3, length * width + 4,
                        length * width + length + 1, length * width + length + 2, length * width + length + 3, length * width + length + 4,
                        2 * length * width + 1, 2 * length * width + 2, 2 * length * width + 3, 2 * length * width + 4,
                        2 * length * width + length + 1, 2 * length * width + length + 2, 2 * length * width + length + 3, 2 * length * width + length + 4,
                },//4*2
                new int[]{3,
                        1, 2, length + 1, length + 2, 2 * length + 1, 2 * length + 2, 3 * length + 1, 3 * length + 2,
                        length * width + 1, length * width + 2, length * width + length + 1, length * width + length + 2,
                        length * width + 2 * length + 1, length * width + 2 * length + 2, length * width + 3 * length + 1, length * width + 3 * length + 2,
                        2 * length * width + 1, 2 * length * width + 2, 2 * length * width + length + 1, 2 * length * width + length + 2,
                        2 * length * width + 2 * length + 1, 2 * length * width + 2 * length + 2, 2 * length * width + 3 * length + 1, 2 * length * width + 3 * length + 2,
                },//2*4
                new int[]{3,
                        1, 2, 3, length + 1, length + 2, length + 3,
                        length * width + 1, length * width + 2, length * width + 3, length * width + length + 1, length * width + length + 2, length * width + length + 3,
                        2 * length * width + 1, 2 * length * width + 2, 2 * length * width + 3, 2 * length * width + length + 1, 2 * length * width + length + 2, 2 * length * width + length + 3,
                        3 * length * width + 1, 3 * length * width + 2, 3 * length * width + 3, 3 * length * width + length + 1, 3 * length * width + length + 2, 3 * length * width + length + 3,
                },//3*2
                new int[]{1,
                        1, 2, 3, 4, length + 1, length + 2, length + 3, length + 4,
                        length * width + 1, length * width + 2, length * width + 3, length * width + 4,
                        length * width + length + 1, length * width + length + 2, length * width + length + 3, length * width + length + 4,
                },//4*2
                new int[]{1, //The first number should be the id of this parcel/pentomino
                        1, 2, length + 1, length + 2,
                        length * width + 1, length * width + 2, length * width + length + 1, length * width + length + 2,
                        2 * length * width + 1, 2 * length * width + 2, 2 * length * width + length + 1, 2 * length * width + length + 2,
                        3 * length * width + 1, 3 * length * width + 2, 3 * length * width + length + 1, 3 * length * width + length + 2,
                },//2*2
                new int[]{1,
                        1, 2, length + 1, length + 2, 2 * length + 1, 2 * length + 2, 3 * length + 1, 3 * length + 2,
                        length * width + 1, length * width + 2, length * width + length + 1, length * width + length + 2,
                        length * width + 2 * length + 1, length * width + 2 * length + 2, length * width + 3 * length + 1, length * width + 3 * length + 2,
                },//2*4
                new int[]{2,
                        1, 2, 3, length + 1, length + 2, length + 3, length * 2 + 1, length * 2 + 2, length * 2 + 3,
                        length * width + 1, length * width + 2, length * width + 3, length * width + length + 1, length * width + length + 2, length * width + length + 3, length * width + length * 2 + 1, length * width + length * 2 + 2, length * width + length * 2 + 3,
                        2 * length * width + 1, 2 * length * width + 2, 2 * length * width + 3, 2 * length * width + length + 1, 2 * length * width + length + 2, 2 * length * width + length + 3, 2 * length * width + length * 2 + 1, 2 * length * width + length * 2 + 2, 2 * length * width + length * 2 + 3,
                },//3*3
                new int[]{3,
                        1, 2, 3, 4, length + 1, length + 2, length + 3, length + 4, length * 2 + 1, length * 2 + 2, length * 2 + 3, length * 2 + 4,
                        length * width + 1, length * width + 2, length * width + 3, length * width + 4,
                        length * width + length + 1, length * width + length + 2, length * width + length + 3, length * width + length + 4,
                        length * width + length * 2 + 1, length * width + length * 2 + 2, length * width + length * 2 + 3, length * width + length * 2 + 4,
                },//4*3

        };
    }

}
