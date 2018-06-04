package CFGExtract;

import java.util.Vector;

public class TreeNode {

	private Vector<TreeNode> children;
	private TreeNode parent;
	private TreeNode root;
	private String value;
	public TreeNode() {
		
	}
	public Vector<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(Vector<TreeNode> children) {
		this.children = children;
	}
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public TreeNode getRoot() {
		return root;
	}
	public void setRoot(TreeNode root) {
		this.root = root;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
