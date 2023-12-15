import java.util.Stack;
/**
 * Dancing Links X Algorithm
 * Dancing links X is an algorithm to solve the complete coverage problem.
 * Pentominos of the same type can be reused.
 * This is originally designed for a 3D Knapsack problem, for example: you have different types of parcels, each with its own set of dimensions (length, width, and height) and values. You also have a truck with its own dimensions. The objective is to determine the optimal way to pack the parcels into the truck to maximize the total value while staying within the truck's size constraints.
 */
public class DancingLink {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        DancingLink dancingLink = new DancingLink();
        System.out.println(dancingLink.dancing());
        long endTime = System.currentTimeMillis();
        System.out.println("Running time： " + ((endTime - startTime) / 1000) + "s and " + (endTime - startTime) % 1000 + " ms.");
    }

    public DancingLinkList dancingLinkList;
    private Stack<Node> ans;
    public int[][] ansArray;
    static final int recLen = 5; // Length of filled rectangle
    static final int recWid = 5;// Width of filled rectangle
    public static UI ui = new UI(recLen, recWid, 50);

    public DancingLink() {
        dancingLinkList = new DancingLinkList();
        ans = new Stack<>();
        ansArray = new int[recLen][recWid];
        for (int i = 0; i < ansArray.length; i++) {
            for (int j = 0; j < ansArray[0].length; j++) {
                ansArray[i][j] = -1;
            }
        }
        dancingLinkList.show();
    }

    /**
     * X algorithm with Dancing Links
     * @return If the answer is found
     */
    public boolean dancing() {
        if (!canDelete())
            return false;

        Node colControl = dancingLinkList.head.right;

        Node rowControl = colControl;
        while (true) {
            rowControl = rowControl.down;
            if (rowControl == colControl)
                break;

            Stack<Node> deletedLines = new Stack<>();
            deleteRelevantLines(rowControl, deletedLines);
            ansToArray((Stack<Node>) ans.clone());
            ui.setState(ansArray);


            if (canDelete())
                dancing();

            if (!isEmpty()) {
                recoverRelevantLines(rowControl, deletedLines);
                ansToArray((Stack<Node>) ans.clone());
                ui.setState(ansArray);

            } else {
                return true;
            }
        }
        ui.setState(ansArray);
        return isEmpty();
    }

    /**
     * If the list is empty, which means their only 'head' in the whole list, the solution is found
     * @return if the link is empty
     */
    public boolean isEmpty() {
        return (dancingLinkList.head.right == dancingLinkList.head && dancingLinkList.head.left == dancingLinkList.head);
    }

    /**
     * Check if the current linked list could still do recursion
     * @return can do recursion or not
     */
    public boolean canDelete() {
        if (isEmpty())
            return false;

        Node temp = dancingLinkList.head;
        while (true) {
            temp = temp.right;
            if (temp == dancingLinkList.head)
                break;
            if (temp.down != temp || temp.up != temp)
                return true;
        }
        return false;
    }

    /**
     * Delete the chosen line, release the column heads and delete all the conflicting lines in recursion
     * @param node a node in chosen line
     * @param deletedLines the stack to store the conflicting lines
     */
    public void deleteRelevantLines(Node node, Stack<Node> deletedLines) {
        Node nodeInRow = node;
        while (true) {
            Node temp = nodeInRow.col;
            while (true) {
                temp = temp.down;
                if (temp == temp.col)
                    break;
                if (temp == nodeInRow)
                    continue;
                dancingLinkList.deleteLine(temp);
                deletedLines.push(temp);
            }
            ans.push(nodeInRow);
            //System.out.print("Pop in the stack： " + nodeInRow + " ");
            dancingLinkList.releaseLink(nodeInRow.col);

            if (nodeInRow.right == node)
                break;
            nodeInRow = nodeInRow.right;
        }
        //System.out.println();
    }

    /**
     * Reconnect the chosen line, the column heads and all the conflicting lines in back-tracking
     * @param rowControl a node in chosen row
     * @param deletedLines the stack to store the conflicting lines
     */
    public void recoverRelevantLines(Node rowControl, Stack<Node> deletedLines) {
        while (!deletedLines.empty()) {
            Node temp = deletedLines.pop();
            dancingLinkList.recoverLine(temp);
        }

        Node temp = rowControl;
        while (true) {
            Node popedNode = ans.pop();
            //System.out.print("Pop out of the stack： " + popedNode + " ");
            temp.col.right.left = temp.col;
            temp.col.left.right = temp.col;

            temp = temp.right;
            if (temp == rowControl)
                break;
        }
        //System.out.println();
    }

    /**
     * Turn the stack to array. This method will pop out all the elements in the Stack
     * @param ans the answer stack (better to be cloned)
     * @return  a 2D array of answer
     */
    public void ansToArray(Stack<Node> ans) {
        setAnsArray();
        int length = ansArray[0].length;
        while (!ans.empty()) {
            Node temp = ans.pop();
            int x = (temp.colNo - 1) % length;
            int y = (temp.colNo - 1) / length;
//            System.out.println("x: " + x + " y: " + y + " z: " + z);
            ansArray[y][x] = temp.id;
//            System.out.print("[ " + temp.rowNo +"," + temp.colNo + "]");
        }
    }

    public void setAnsArray() {
        for (int i = 0; i < ansArray.length; i++) {
            for (int j = 0; j < ansArray[0].length; j++) {
                ansArray[i][j] = -1;
            }
        }
    }

    /**
     * Dancing Links data structure
     * Cross circular doubly linked list
     * Column: x*y bricks
     * Row: All the possible placements in this rectangle
     */
    class DancingLinkList {
        private final int length = recWid;
        private final int width = recLen;
        private final int area = length * width;
        protected Node head = new Node(0, 0, null, -1);
        private int rowNo = 0;

        public DancingLinkList() {
            head.left = head;
            head.right = head;
            head.up = head;
            head.down = head;
            head.col = head;

            Node temp = head;
            for (int i = 0; i < area; i++) {
                Node col = new Node(0, area - i, null, -1);
                addHori(temp, col);

                col.up = col;
                col.down = col;

                temp = col;
                col.col = col;
            }

            DLXPentDB DLXPentDB = new DLXPentDB(length, width);
            int[][] objects = DLXPentDB.pieces;
            linkInit(objects);
        }

        public void linkInit(int[][] objects) {
            for (int i = 0; i < objects.length; i++) {
                int[] rowPointer = arrayCopy(objects[i]);
                int[] temp = arrayCopy(rowPointer);
                int breakCase = lineBreak(temp);

                while (breakCase != 2) {
                    breakCase = lineBreak(temp);
                    addLine(temp);
                    if (breakCase == 0) {
                        for (int j = 1; j < temp.length; j++) {
                            temp[j]++;
                        }
                    } else if (breakCase == 1) {
                        for (int j = 1; j < rowPointer.length; j++) {
                            rowPointer[j] += length;
                        }
                        temp = arrayCopy(rowPointer);
                    }
                }
            }
        }

        public void addLine(int[] parcel) {
            if (!canAdd(parcel))
                return;

            rowNo++;
            //System.out.println("Row" + rowNo + " Add successfully:" + Arrays.toString(parcel));

            Node col = head.right;
            // The first column node is head, and the first value in parcel[] is id
            // They all have no meaning about position.
            int count = 1; // The index value of column
            int j = 1; // The index value of parcel cube

            Node previous = null;
            while (j < parcel.length) {
                if (parcel[j] == count) {
                    Node newNode = new Node(rowNo, count, col, parcel[0]);

                    // Insert the node in the last position of this column
                    addVert(col, newNode);
                    if (j != 1) {
                        addHori(previous, newNode);
                    } else {
                        newNode.left = newNode;
                        newNode.right = newNode;
                        previous = newNode;
                    }
                    j++;
                }
                if (col.right == head)
                    break;
                col = col.right;
                count++;
            }
        }

        public boolean canAdd(int[] parcel) {
            for (int i = 1; i < parcel.length; i++) {
                if (parcel[i] > area)
                    return false;
                if (parcel[i] % length > length)
                    return false;
                if ((parcel[i] - 1) / length > width - 1)
                    return false;
            }
            return true;
        }

        public int lineBreak(int[] parcel) {
            boolean lengthSide = false;
            boolean widthSide = false;
            for (int i = 1; i < parcel.length; i++) {
                if (parcel[i] % length == 0)
                    lengthSide = true;
                if ((parcel[i] - 1) / length > width - 1)
                    widthSide = true;
            }

            if (lengthSide && widthSide) // 一面里的最角落，这种情况要换到下一面遍历
                return 2;
            else if (lengthSide) //一行的最右边，要换行
                return 1;
            else
                return 0;
        }

        /**
         * Add a node to the end of a column
         * @param colHead the head of column
         * @param newNode the node to be added
         */
        public void addVert(Node colHead, Node newNode) {
            newNode.up = colHead.up;
            newNode.down = colHead;
            colHead.up.down = newNode;
            colHead.up = newNode;
        }

        /**
         * Add a node to the end of a row
         * @param rowHead the head of column
         * @param newNode the node to be added
         */
        public void addHori(Node rowHead, Node newNode) {
            newNode.right = rowHead;
            newNode.left = rowHead.left;
            rowHead.left.right = newNode;
            rowHead.left = newNode;
        }

        /**
         * Release the link of column head
         * @param col the column head to be released
         */
        public void releaseLink(Node col) {
            col.left.right = col.right;
            col.right.left = col.left;
        }

        public void deleteLine(Node node) {
            Node temp = node;
        //System.out.println("Delete: " + node.rowNo);
            while (true) {
                temp.up.down = temp.down;
                temp.down.up = temp.up;
                temp = temp.right;
                if (temp == node)
                    break;
            }
        }

        /**
         * Reconnect the deleted line into the linked list
         * @param node a node in the line which is deleted
         */
        public void recoverLine(Node node) {
            Node temp = node;
            while (true) {
                temp.up.down = temp;
                temp.down.up = temp;
                temp = temp.right;
                if (temp == node)
                    break;
            }
        }

        public int[] arrayCopy(int[] arr) {
            int[] result = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                result[i] = arr[i];
            }
            return result;
        }

        public void show() {
            //System.out.println("Loop the list：");
            Node colTemp = head;
            while (true) {
                Node rowTemp = colTemp;
                while (true) {
                    //System.out.print(rowTemp);
                    if (rowTemp.down == colTemp)
                        break;
                    rowTemp = rowTemp.down;
                }
                //System.out.println();
                if (colTemp.right == head)
                    break;
                colTemp = colTemp.right;
            }
        }
    }

    /**
     * The node in Dancing Linked List
     */
    class Node {
        protected Node left, right, up, down, col;
        protected int rowNo, colNo;
        protected int id;

        public Node(int rowNo, int colNo, Node col, int id) {
            this.rowNo = rowNo;
            this.colNo = colNo;
            this.col = col;
            this.id = id;
        }

        @Override
        public String toString() {
            if (this.col == this)
                return "[col:" + colNo + "]";
            if (this.colNo == 0)
                return "[row:" + rowNo + "]";
            return "[row: " + rowNo + " col: " + colNo + " id:" + id + "] ";
        }
    }

}

