import java.util.Arrays;
import java.util.Stack;

/**
 * Dancing Links X Algorithm
 * Dancing links X is an algorithm to solve the complete coverage problem.
 * Nested classes: Dancing Links List, Node
 */
public class DancingLinksX {

    // The data structure used in DLX algorithm
    private DancingLinksList dancingLinksList;
    // A stack to store answer (for recursion and backtracking)
    private Stack<Node> ans;
    // The 3D array to merge with GUI
    public int[][][] ansArray;
    // These variable are for calculating the max value of the objects in searching path
    // But once the algorithm find the answer to full filled the whole container, this will stop
    // So most of time it could not provide a good max value
    private Stack<Node> maxValueAns;
    public int maxValue = 0;
    public int curValue = 0;

    public DancingLinksX() {
        dancingLinksList = new DancingLinksList();
        ans = new Stack<>();
        ansArray = new int[8][5][33];
        System.out.println("If the answer is found? : " + dancing());
        ansArray = ansToArray(ans);
        showAnsArr();
    }

    /**
     * X algorithm with Dancing Links
     * @return If the answer is found
     */
    public boolean dancing() {
        if (!canDelete())
            return false;

        // Loop for only the first column
        // If the answer does not exist in first column, that means the answer for full filled the container does not exist
        Node colControl = dancingLinksList.head.right;

        // A stack to store the lines which is pruned (for pruning)
        // Pruning: when the recursion result of a line is false, it should be deleted in the future recursion
        Stack<Node> deleted = new Stack<>();
        Node rowControl = colControl; // Temporary pointer of a row
        while (true) {
            rowControl = rowControl.down; // Choose a line to be answer
            if (rowControl == colControl)
                break;

            // A stack to store the lines which conflicts with the chosen line
            Stack<Node> deletedLines = new Stack<>();
            // Delete the chosen line, release the column heads and delete all the conflicting lines
            deleteRelevantLines(rowControl, deletedLines);
            // Refresh the answer array
            ansArray = ansToArray((Stack<Node>) ans.clone());


            if (canDelete())
                dancing(); // Do recursion if the new list allows

            // Update the max value in searching path
            if (curValue > maxValue) {
                maxValueAns = (Stack<Node>) ans.clone();
                maxValue = curValue;
                System.out.println("The max value in searching path: " + maxValue);
                showMax();
                System.out.println("Still searching for answer...");
            }

            // End the recursion
            if (!isEmpty()) { // No solution
                recoverRelevantLines(rowControl, deletedLines); // Back-tracking
                // Refresh the answer array
                ansArray = ansToArray((Stack<Node>) ans.clone());
                // Pruning delete
                deleted.push(rowControl);
                dancingLinksList.deleteLine(rowControl);
            } else { // Find the solution. End recursion.
                return true;
            }
        }
        // Pruning back-tracking
        while (!deleted.empty()) {
            dancingLinksList.recoverLine(deleted.pop());
        }
        return isEmpty();
    }

    /**
     * If the list is empty, which means their only 'head' in the whole list, that means that the solution is found
     * @return if the link is empty
     */
    public boolean isEmpty() {
        return (dancingLinksList.head.right == dancingLinksList.head && dancingLinksList.head.left == dancingLinksList.head);
    }

    /**
     * Check if the current linked list could still do recursion
     * @return can do recursion or not
     */
    public boolean canDelete() {
        if (isEmpty())
            return false;

        Node temp = dancingLinksList.head;
        while (true) {
            temp = temp.right;
            if (temp == dancingLinksList.head)
                break;
            if (temp.down != temp || temp.up != temp) // Meaningful node exists
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
            while (true) { //Skip the chosen line
                temp = temp.down;
                if (temp == temp.col)
                    break;
                if (temp == nodeInRow)
                    continue;
                dancingLinksList.deleteLine(temp);
                deletedLines.push(temp);
            }
            ans.push(nodeInRow); // Push answer in answer stack
            dancingLinksList.releaseLink(nodeInRow.col);

            if (nodeInRow.right == node)
                break;
            nodeInRow = nodeInRow.right;
        }
        curValue += getValue(nodeInRow.right); // Update the current value
    }

    /**
     * Reconnect the chosen line, the column heads and all the conflicting lines in back-tracking
     * @param rowControl a node in chosen row
     * @param deletedLines the stack to store the conflicting lines
     */
    public void recoverRelevantLines(Node rowControl, Stack<Node> deletedLines) {
        while (!deletedLines.empty()) {
            Node temp = deletedLines.pop();
            dancingLinksList.recoverLine(temp);
        }

        Node temp = rowControl;
        while (true) {
            ans.pop(); // Pop answer in answer stack
            temp.col.right.left = temp.col;
            temp.col.left.right = temp.col;

            temp = temp.right;
            if (temp == rowControl)
                break;
        }
        curValue -= getValue(rowControl.right);// Update the current value
    }


    /**
     * Turn the stack to array. This method will pop out all the elements in the Stack
     * @param ans the answer stack (better to be cloned)
     * @return  a 3D array of answer
     */
    public int[][][] ansToArray(Stack<Node> ans) {
        if (ans == null)
            return null;

        int[][][] ansArray = new int[8][5][33];
        int length = ansArray[0][0].length;
        int area = length * ansArray[0].length;
        while (!ans.empty()) {
            Node temp = ans.pop();
            int x = (temp.colNo - 1) % length;
            int y = (temp.colNo - 1) % area / length;
            int z = (temp.colNo - 1) / area;
//            System.out.println("x: " + x + " y: " + y + " z: " + z);
            ansArray[z][y][x] = temp.id;
//            System.out.print("[ " + temp.rowNo +"," + temp.colNo + "]");
        }
        return ansArray;
    }

    /**
     * Print out the answer array
     */
    public void showAnsArr() {
        if (ansArray == null)
            return;
        System.out.println("Answer: ");
        for (int i = 0; i < ansArray.length; i++) {
            System.out.println(Arrays.deepToString(ansArray[i]));
        }
    }

    /**
     * Get value according to ID
     * @param node
     * @return
     */
    public int getValue(Node node) {
        if (node.id == 1)
            return 3;
        if (node.id == 2)
            return 4;
        if (node.id == 3)
            return 5;
        else
            return 0;
    }

    /**
     * Print out the placement of current Max Value
     */
    public void showMax() {
        int[][][] maxPlace = ansToArray(maxValueAns);
        if (maxPlace != null) {
            for (int i = 0; i < maxPlace.length; i++) {
                System.out.println(Arrays.deepToString(maxPlace[i]));
            }
        }
    }

    /**
     * Dancing Links data structure
     * Cross bidirectional circular linked list
     * Column: the 16.5*2 * 2.5*2 * 4*2 = 1320 cubes (0.5m*0.5m*0.5m)
     * Row: All the possible placements in this container
     */
    class DancingLinksList {
        private int length = 33;
        private int width = 5;
        private int height = 8;
        private int volume = length * width * height;
        // Initialize the head of this linked list
        protected Node head = new Node(0, 0, null, -1);
        // To record the number of rows
        private int rowNo = 0;



        public DancingLinksList() {
            head.left = head;
            head.right = head;
            head.up = head;
            head.down = head;
            head.col = head;

            // Initialize the column head
            Node temp = head;
            for (int i = 0; i < volume; i++) {
                Node col = new Node(0, volume - i, null, -1);
                addHori(temp, col);

                col.up = col;
                col.down = col;

                temp = col;
                col.col = col;
            }

            DLXParcelDB DLXParcelDB = new DLXParcelDB(length, width);
            int[][] objects = DLXParcelDB.parcels;
            linkInit(objects);
        }

        /**
         * Initialize the linked list
         * @param objects the objects need to be put in container
         */
        public void linkInit(int[][] objects) {
            // Loop for all the possible placements of each objects in the container
            for (int i = 0; i < objects.length; i++) {
                int[] layerPointer = arrayCopy(objects[i]); // Pointer to point the first position of a layer
                int[] rowPointer = arrayCopy(layerPointer); // Pointer to point the first position of a line
                int[] temp = arrayCopy(rowPointer); // A temporary variable for loop
                int breakCase = lineBreak(temp); // To check about the break situation  line -> line / layer -> layer

                while (canAdd(temp)) {
                    addLine(temp);
                    breakCase = lineBreak(temp);
                    if (breakCase == 0) { // Move to right for 1 cube
                        for (int j = 1; j < temp.length; j++) {
                            temp[j]++;
                        }
                    } else if (breakCase == 1) { // The pentomino reached the end of a row
                        for (int j = 1; j < rowPointer.length; j++) {
                            rowPointer[j] += length;
                        }
                        temp = arrayCopy(rowPointer);
                    } else if (breakCase == 2) { // The pentomino reached the end of a layer
                        for (int j = 1; j < layerPointer.length; j++) {
                            layerPointer[j] += length * width;
                        }
                        rowPointer = arrayCopy(layerPointer);
                        temp = arrayCopy(rowPointer);
                    } else
                        break;
                }
            }
        }

        /**
         * Add a line to the bottom of the list
         * @param object the placement of a parcel/pentomino
         */
        public void addLine(int[] object) {
            if (!canAdd(object))
                return;

            rowNo++;
//            System.out.println("Row" + rowNo + " Add successfully:" + Arrays.toString(object));

            Node col = head.right;
            // The first column node is head, and the first value in object[] is id
            // They all have no meaning about position.
            int count = 1; // The index value of column
            int j = 1; // The index value of object cube

            Node previous = null;
            while (j < object.length) {

                if (object[j] == count) {
                    Node newNode = new Node(rowNo, count, col, object[0]);

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

        /**
         * Check if this placement is available in the space
         * @param parcel the placement of a parcel/pentomino
         * @return if the current one can be added in the list
         */
        public boolean canAdd(int[] parcel) {
            for (int i = 1; i < parcel.length; i++) {
                if (parcel[i] > volume)
                    return false;
                if (parcel[i] % length > length)
                    return false;
                if (((parcel[i] - 1) % (length * width)) / length > width - 1)
                    return false;
                if ((parcel[i] - 1) / (length * width) > height - 1)
                    return false;
            }
            return true;
        }

        /**
         * To check the line changing/ layer changing situation
         * @param parcel the placement of a parcel/pentomino
         * @return 0: no need to change  1: turn to another line  2: turn to another layer  3:the end of loop
         */
        public int lineBreak(int[] parcel) {
            boolean lengthSide = false;
            boolean widthSide = false;
            boolean heightSide = false;
            for (int i = 1; i < parcel.length; i++) {
                if (parcel[i] % length == 0)
                    lengthSide = true;
                if ((parcel[i] - 1) % (length * width) / length == width - 1)
                    widthSide = true;
                if ((parcel[i] - 1) / (length * width) == height - 1)
                    heightSide = true;
            }

            if (lengthSide && widthSide && heightSide) // Reach the last layer, end of length, and also end of width
                return 3;
            else if (lengthSide && widthSide) // Reach the end of length, and also end of width
                return 2;
            else if (lengthSide) //Reach the end of width
                return 1;
            else // No need to change
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
         * Add a node next to the previous node
         * @param previous the previous node
         * @param newNode the node to be added
         */
        public void addHori(Node previous, Node newNode) {
            newNode.right = previous;
            newNode.left = previous.left;
            previous.left.right = newNode;
            previous.left = newNode;
        }

        /**
         * Release the link of column head
         * @param col the column head to be released
         */
        public void releaseLink(Node col) {
            col.left.right = col.right;
            col.right.left = col.left;
        }

        /**
         * Delete the whole line which the node located
         * @param node a node in the line which is to be deleted
         */
        public void deleteLine(Node node) {
            Node temp = node;
//        System.out.println("Delete: " + node.rowNo);
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

        /**
         * Copy array
         * @param arr the array need to be copied
         * @return a new array
         */
        public int[] arrayCopy(int[] arr) {
            int[] result = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                result[i] = arr[i];
            }
            return result;
        }

        /**
         * To show the linked list
         * Showing from column head to the bottom
         */
        public void show() {
            System.out.println("List：");
            Node colTemp = head;
            while (true) {
                Node rowTemp = colTemp;
                while (true) {
                    System.out.print(rowTemp);
                    if (rowTemp.down == colTemp)
                        break;
                    rowTemp = rowTemp.down;
                }
                System.out.println();
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

