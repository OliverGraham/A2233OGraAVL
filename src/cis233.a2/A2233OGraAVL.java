/*
    Oliver Graham / olivergraham916@gmail.com
    CIS 233 / Scovil
    Assignment 2
    Due: Monday, May 5th, 2020
 */

package cis233.a2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class A2233OGraAVL<AnyType extends Comparable<? super AnyType>> {

    public A2233OGraAVL() {
        root = null;
    }

    public void insert(AnyType x) {
        root = insert(x, root);
    }

    private void remove(AnyType x) {
        removeAll(x, root);
    }

    public void removeAll(AnyType x) {
        removeAll(x, root);
    }

    private void removeAll(AnyType x, A2233OGraAvlNode<AnyType> t) {

        if (t != null) {

            int compareResult = x.compareTo(t.element);

            if (compareResult < 0)
                removeAll(x, t.left);
            else if (compareResult > 0)
                removeAll(x, t.right);
            else {
                t.isActive = false;
                for (A2233OGraDupeNode el: t.dupeList)
                    el.isActive = false;
            }
        }
    }

    public LinkedList<AnyType> findAll(AnyType x) {
       return findAll(x, root);
    }

    private LinkedList<AnyType> findAll(AnyType x, A2233OGraAvlNode<AnyType> t) {

        while (t != null) {

            int compareResult = x.compareTo(t.element);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else {
                LinkedList<AnyType> list = new LinkedList<>();

                if (t.isActive)
                    list.add(t.element);

                for (A2233OGraDupeNode el: t.dupeList) {
                    if (el.isActive)
                        list.add(t.element);
                }

                return list;
            }
        }
        return new LinkedList<>();
    }


    public AnyType findMin() {
         if (isEmpty())
            throw new UnderflowException("Tree is empty");
        return findMin(root).element;
    }

    public AnyType findMax() {
        if (isEmpty())
          throw new UnderflowException("Tree is empty");
        return findMax(root).element;
    }

    public boolean contains(AnyType x) {
        return contains(x, root);
    }

    public void makeEmpty() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private static final int ALLOWED_IMBALANCE = 1;

    private A2233OGraAvlNode<AnyType> balance(A2233OGraAvlNode<AnyType> t) {

        if (t == null)
            return t;

        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE)
            if (height(t.left.left) >= height(t.left.right))
                t = rotateWithLeftChild(t);
            else
                t = doubleWithLeftChild(t);
        else
        if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE)
            if (height(t.right.right) >= height(t.right.left))
                t = rotateWithRightChild(t);
            else
                t = doubleWithRightChild(t);

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(A2233OGraAvlNode<AnyType> t) {

        if (t == null)
            return -1;

        if (t != null) {

            int hl = checkBalance(t.left);
            int hr = checkBalance(t.right);
            if (Math.abs(height(t.left) - height(t.right)) > 1 ||
                    height(t.left) != hl || height(t.right) != hr)
                System.out.println("OOPS!!");
        }

        return height(t);
    }

    private A2233OGraAvlNode<AnyType> insert(AnyType x, A2233OGraAvlNode<AnyType> t) {

        if (t == null)
            return new A2233OGraAvlNode<>(x, null, null);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = insert(x, t.left);
        else if (compareResult > 0)
            t.right = insert(x, t.right);
        else
            t.dupeList.add(new A2233OGraDupeNode(t.element));

        return balance(t);
    }

    private A2233OGraAvlNode<AnyType> findMin(A2233OGraAvlNode<AnyType> t) {

        if (t == null)
            return t;

        A2233OGraAvlNode<AnyType> tmp = findMin(t.left);

        if (tmp != null)
            return tmp;

        if (t.isActive)
            return t;
        else {
            for (A2233OGraDupeNode el: t.dupeList) {
                if (el.isActive)
                    return t;
            }
        }

        return findMin(t.right);
    }

    private A2233OGraAvlNode<AnyType> findMax(A2233OGraAvlNode<AnyType> t) {

        if (t == null)
            return t;

        A2233OGraAvlNode<AnyType> tmp = findMax(t.right);

        if (tmp != null)
            return tmp;

        if (t.isActive)
            return t;
        else {
            for (A2233OGraDupeNode el: t.dupeList) {
                if (el.isActive)
                    return t;
            }
        }

        return findMax(t.left);
    }

    private boolean contains(AnyType x, A2233OGraAvlNode<AnyType> t) {

        while (t != null)
        {
            int compareResult = x.compareTo(t.element);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else
                return true;
        }
        return false;
    }


    public void printTree() {
        if (isEmpty())
            System.out.println("Tree is currently empty.");
        else
            printTree(root);
    }

    private void printTree(A2233OGraAvlNode<AnyType> t) {
        if (t != null) {
            printTree(t.left);

            if (t.isActive)
                System.out.println(t.element);

            for (A2233OGraDupeNode el: t.dupeList)
                if (el.isActive)
                    System.out.println(el.element);

            printTree(t.right);
        }
    }

    public void printBalTree(boolean ascOrDesc) {
        if (isEmpty())
            System.out.println("Tree is currently empty.");
        else
            printBalTree(root, ascOrDesc);
    }

    private void printBalTree(A2233OGraAvlNode<AnyType> t, boolean ascOrDesc) {

        if (t != null) {

            if (ascOrDesc)
                printBalTree(t.left, ascOrDesc);
            else
                printBalTree(t.right, ascOrDesc);

            System.out.print(formatNodeInfo(t));

            if (ascOrDesc)
                printBalTree(t.right, ascOrDesc);
            else
                printBalTree(t.left, ascOrDesc);

        }
    }

    public void writeBalTree(boolean ascOrDesc) {
        try {
            FileWriter writer = new FileWriter("A2233OGraAVLout.txt");
            StringBuilder builder = new StringBuilder();

            String text = writeBalTree(root, ascOrDesc, builder);

            writer.write(text);

            writer.close();

        } catch (IOException e) {
            System.out.println("Error occurred " + e);
        }
    }

    private String writeBalTree(A2233OGraAvlNode<AnyType> t, boolean ascOrDesc, StringBuilder text) {

            if (t != null) {

                if (ascOrDesc)
                    writeBalTree(t.left, ascOrDesc, text);
                else
                    writeBalTree(t.right, ascOrDesc, text);

                text.append(formatNodeInfo(t));

                if (ascOrDesc)
                    writeBalTree(t.right, ascOrDesc, text);
                else
                    writeBalTree(t.left, ascOrDesc, text);
            }

            return text.toString();
    }

    private String formatNodeInfo(A2233OGraAvlNode<AnyType> t) {

        int numOfActiveDupes = 0;
        int totalSize = t.dupeList.size();

        for (A2233OGraDupeNode el: t.dupeList)
            if (el.isActive)
                numOfActiveDupes++;

        return String.format("%s %-12s %s %-4s %s %-4s %s %-12s %s %2s %-7s %s %-8s %s %s %s %6s %s %5s %s",
                "Data:", t.element, "Height:", t.height, "Balance:", height(t.right) - height(t.left),
                "Status:", (t.isActive ? "Active" : "Inactive"),
                "Duplicate Data:", totalSize, "Total;", numOfActiveDupes, "Active,",
                totalSize - numOfActiveDupes, "Inactive",
                "\n\tLeft:", t.left == null ? "null" : t.left.element,
                "\n\tRight:", t.right == null ? "null" : t.right.element, "\n\n");
    }

    public static String author() {
        return "Oliver Graham";
    }

    public Result getMode() {
        return getMode(root, new A2233OGraResult());
    }

    private A2233OGraResult getMode(A2233OGraAvlNode<AnyType> t, A2233OGraResult res) {

        if (t != null) {
            res = getMode(t.left, res);
            res = getMode(t.right, res);

            int count = t.isActive ? 1 : 0;

            for (A2233OGraDupeNode el: t.dupeList)
                if (el.isActive)
                    count++;


            if (count > res.count) {
                res.count = count;
                res.mode = t.element;
            }
        }
        return res;
    }

    private static class A2233OGraResult<AnyType> implements Result {

        public AnyType mode() { return mode; }

        public int count() { return count; }

        private AnyType mode;
        private int count;
    }

    private int height(A2233OGraAvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    private A2233OGraAvlNode<AnyType> rotateWithLeftChild( A2233OGraAvlNode<AnyType> k2) {
        A2233OGraAvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    private A2233OGraAvlNode<AnyType> rotateWithRightChild(A2233OGraAvlNode<AnyType> k1) {
        A2233OGraAvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    private A2233OGraAvlNode<AnyType> doubleWithLeftChild(A2233OGraAvlNode<AnyType> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    private A2233OGraAvlNode<AnyType> doubleWithRightChild(A2233OGraAvlNode<AnyType> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    private static class A2233OGraDupeNode<AnyType> {
       private A2233OGraDupeNode(AnyType theElement) {
            element = theElement;
            isActive = true;
        }
        private boolean isActive;
        private AnyType element;
    }

    private static class A2233OGraAvlNode<AnyType> {

        A2233OGraAvlNode(AnyType theElement) {
            this(theElement,null, null);
        }

        A2233OGraAvlNode(AnyType theElement, A2233OGraAvlNode<AnyType> lt, A2233OGraAvlNode<AnyType> rt) {
            element  = theElement;
            left     = lt;
            right    = rt;
            height   = 0;
            isActive = true;
            dupeList = new LinkedList<>();
        }

        AnyType            element;
        A2233OGraAvlNode<AnyType>   left;
        A2233OGraAvlNode<AnyType>   right;
        int                height;
        boolean            isActive;
        LinkedList<A2233OGraDupeNode> dupeList;
    }

    private A2233OGraAvlNode<AnyType> root;
}
