package fr.limsi.rorqual.core.utils;

import java.util.ArrayList;

// from https://github.com/jblough/Android-Pdf-Viewer-Library/blob/master/src/androswing/tree/DefaultMutableTreeNode.java
public class DefaultMutableTreeNode {

    private DefaultMutableTreeNode parent;
    private Object userObject;
    private ArrayList<DefaultMutableTreeNode> children;

    protected DefaultMutableTreeNode(){
        parent = null;
        userObject = null;
        children = new ArrayList<DefaultMutableTreeNode>();
    }

    public DefaultMutableTreeNode(Object object){
        parent = null;
        userObject = object;
        children = new ArrayList<DefaultMutableTreeNode>();
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public void add(DefaultMutableTreeNode newChild) {
        newChild.parent = this;
        children.add(newChild);
    }

    public DefaultMutableTreeNode getParent() {
        return parent;
    }

    public int getChildCount() {
        return children.size();
    }

    public DefaultMutableTreeNode getChildAt(int i) {
        return children.get(i);
    }
}