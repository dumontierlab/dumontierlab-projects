package com.dumontierlab.ontocreator.ui.client.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TreeNode<E> implements Iterable<TreeNode<E>>, Serializable {

	private TreeNode<E> parent;
	private Set<TreeNode<E>> children;
	private E value;

	protected TreeNode() {
		// for serialization
	}

	public TreeNode(E value) {
		children = new HashSet<TreeNode<E>>();
		this.value = value;
	}

	public TreeNode<E> getParent() {
		return parent;
	}

	public void setParent(TreeNode<E> parent) {
		this.parent = parent;
	}

	public E getValue() {
		return value;
	}

	public void setValue(E value) {
		this.value = value;
	}

	public void addChild(TreeNode<E> child) {
		children.add(child);
	}

	public void removeChild(TreeNode<E> child) {
		children.remove(child);
	}

	public int getChildCount() {
		return children.size();
	}

	public Iterator<TreeNode<E>> iterator() {
		return children.iterator();
	}

}
