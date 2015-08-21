package com.algorithm;

/**
 * Created by chenyongfu on 15-8-21.
 */
class RBTree {

    public static Node NIL = new Node(null, null, null, -1, Color.BLACK);

    private Node root;

    public RBTree() {
        this.root = NIL;
    }

    public RBTree(Node root) {
        this.root = root;
    }

    public void insert(Integer value) {
        Node temp = root;
        Node pre = NIL;
        Node node = new Node(value);
        while (temp != NIL) {
            pre = temp;
            if (value < temp.getValue()) {
                temp = temp.getLeftChild();
            } else {
                temp = temp.getRightChild();
            }
        }
        node.setParent(pre);
        if (pre == NIL) {
            root = node;
        } else if (value < pre.getValue()) {
            pre.setLeftChild(node);
        } else {
            pre.setRightChild(node);
        }
        _insertFix(node);
    }

    private void _insertFix(Node node) {
        while (node.getParent().getColor() == Color.RED) {
            if (node.getParent() == node.getParent().getParent().getLeftChild()) {
                // 父节点是祖父节点的左儿子
                Node uncle = node.getParent().getParent().getRightChild();
                if (uncle.getColor() == Color.RED) {  // CASE 1
                    node.getParent().setColor(Color.BLACK);
                    uncle.setColor(Color.BLACK);
                    node.getParent().getParent().setColor(Color.RED);
                    node = node.getParent().getParent();
                } else {
                    if (node == node.getParent().getRightChild()) { // CASE 2
                        node = node.getParent();
                        _leftRotate(node);
                    } else {  // CASE 3
                        node.getParent().setColor(Color.BLACK);
                        node.getParent().getParent().setColor(Color.RED);
                        _rightRotate(node.getParent().getParent());
                    }
                }

            } else {
                // 父节点是祖父节点的右儿子
                Node uncle = node.getParent().getParent().getLeftChild();
                if (uncle.getColor() == Color.RED) {  // CASE 1
                    node.getParent().setColor(Color.BLACK);
                    uncle.setColor(Color.BLACK);
                    node.getParent().getParent().setColor(Color.RED);
                    node = node.getParent().getParent();
                } else {
                    if (node == node.getParent().getLeftChild()) { // CASE 2
                        node = node.getParent();
                        _rightRotate(node);
                    } else {  // CASE 3
                        node.getParent().setColor(Color.BLACK);
                        node.getParent().getParent().setColor(Color.RED);
                        _leftRotate(node.getParent().getParent());
                    }
                }

            }
        }
        root.setColor(Color.BLACK);
    }

    public Node delete(Integer value) {
        Node node = _search(value);
        if (node == null) {
            return null;
        }
        Node temp = NIL;
        Node child = NIL;
        if (node.getLeftChild() == NIL || node.getRightChild() == NIL) {
            temp = node;
        } else {
            temp = _successor(node);
        }
        if (temp.getLeftChild() != NIL) {
            child = temp.getLeftChild();
        } else {
            child = temp.getRightChild();
        }
        child.setParent(temp.getParent());
        if (temp.getParent() == NIL) {
            root = child;
        } else if (temp == temp.getParent().getLeftChild()) {
            temp.getParent().setLeftChild(child);
        } else {
            temp.getParent().setRightChild(child);
        }
        if (temp != node) {
            node.setValue(temp.getValue());
        }
        if (temp.getColor() == Color.BLACK) {
            _deleteFix(child);
        }
        return temp;

    }

    private void _deleteFix(Node node) {
        while (node.getParent() != NIL && node.getColor() == Color.BLACK) {
            if (node == node.getParent().getLeftChild()) {
                Node brother = node.getParent().getRightChild();
                if (brother.getColor() == Color.RED) {
                    // CASE 1
                    node.getParent().setColor(Color.RED);
                    brother.setColor(Color.BLACK);
                    _leftRotate(node.getParent());
                    brother = node.getParent().getRightChild();
                }
                if (brother.getLeftChild().getColor() == Color.BLACK && brother.getRightChild().getColor() == Color.BLACK) {
                    // CASE 2
                    brother.setColor(Color.RED);
                    node = node.getParent();
                } else if (brother.getRightChild().getColor() == Color.BLACK) {
                    // CASE 3
                    brother.setColor(Color.RED);
                    brother.getLeftChild().setColor(Color.BLACK);
                    _rightRotate(brother);
                } else {
                    // CASE 4
                    brother.setColor(node.getParent().getColor());
                    node.getParent().setColor(Color.BLACK);
                    brother.getRightChild().setColor(Color.BLACK);
                    _leftRotate(node.getParent());
                    node = root;
                }
            } else {
                Node brother = node.getParent().getLeftChild();
                if (brother.getColor() == Color.RED) {
                    // CASE 1
                    node.getParent().setColor(Color.RED);
                    brother.setColor(Color.BLACK);
                    _rightRotate(node.getParent());
                    brother = node.getParent().getLeftChild();
                }
                if (brother.getLeftChild().getColor() == Color.BLACK && brother.getRightChild().getColor() == Color.BLACK) {
                    // CASE 2
                    brother.setColor(Color.RED);
                    node = node.getParent();
                } else if (brother.getLeftChild().getColor() == Color.BLACK) {
                    // CASE 3
                    brother.setColor(Color.RED);
                    brother.getRightChild().setColor(Color.BLACK);
                    _leftRotate(brother);
                } else {
                    // CASE 4
                    brother.setColor(node.getParent().getColor());
                    node.getParent().setColor(Color.BLACK);
                    brother.getLeftChild().setColor(Color.BLACK);
                    _rightRotate(node.getParent());
                    node = root;
                }
            }
        }
        node.setColor(Color.BLACK);
    }

    private Node _successor(Node node) {
        if (node.getRightChild() != NIL) {
            Node temp = node.getRightChild();
            while (temp.getLeftChild() != NIL) {
                temp = temp.getLeftChild();
            }
            return temp;
        } else if (node.getParent() != NIL) {
            Node temp = node.getParent();
            while (temp != NIL && node != temp.getLeftChild()) {
                node = temp;
                temp = temp.getParent();
            }
            return temp;
        }
        return NIL;
    }

    private Node _search(Integer value) {
        Node temp = root;
        while (temp != NIL) {
            if (value == temp.getValue()) {
                return temp;
            } else if (value < temp.getValue()) {
                temp = temp.getLeftChild();
            } else {
                temp = temp.getRightChild();
            }
        }
        return null;
    }

    private void _leftRotate(Node node) {
        Node rightChild = node.getRightChild();
        node.setRightChild(rightChild.getLeftChild());
        if (rightChild.getLeftChild() != NIL) {
            rightChild.getLeftChild().setParent(node);
        }
        rightChild.setLeftChild(node);
        rightChild.setParent(node.getParent());
        if (node.getParent() == NIL) {
            root = rightChild;
        } else if (node == node.getParent().getLeftChild()) {
            node.getParent().setLeftChild(rightChild);
        } else {
            node.getParent().setRightChild(rightChild);
        }
        node.setParent(rightChild);

    }

    private void _rightRotate(Node node) {
        Node leftChild = node.getLeftChild();
        node.setLeftChild(leftChild.getRightChild());
        if (leftChild.getRightChild() != NIL) {
            leftChild.getRightChild().setParent(node);
        }
        leftChild.setRightChild(node);
        leftChild.setParent(node.getParent());
        if (node.getParent() == NIL) {
            root = leftChild;
        } else if (node == node.getParent().getLeftChild()) {
            node.getParent().setLeftChild(leftChild);
        } else {
            node.getParent().setRightChild(leftChild);
        }
        node.setParent(leftChild);

    }

    public void print() {
        _reshow(root);
    }

    private void _reshow(Node node) {
        if (node != NIL) {
            _reshow(node.getLeftChild());
            System.out.println("节点[" + node.getValue() + "]的颜色是:" + node.getColor());
            _reshow(node.getRightChild());
        }
    }

    public static void main(String[] args) {
        RBTree rbTree = new RBTree();
        Integer[] values = new Integer[]{4,8,5,12,24,6,7,14,2};
        for (Integer value : values) {
            rbTree.insert(value);
        }
        rbTree.delete(6);
        rbTree.delete(4);
        rbTree.delete(12);
        rbTree.delete(24);
        rbTree.print();
    }

}

class Node {

    private Node parent;

    private Node leftChild;

    private Node rightChild;

    private Integer value;

    private Color color;

    public Node() {}

    public Node(Integer value) {
        this(RBTree.NIL, RBTree.NIL, RBTree.NIL, value, Color.RED);
    }

    public Node(Node parent, Node leftChild, Node rightChild, Integer value, Color color) {
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.value = value;
        this.color = color;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeftChild() {
        return this.leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return this.rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}

enum Color {
    RED,BLACK
}
