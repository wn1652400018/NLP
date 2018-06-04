package CFGExtract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;


public class RewriteRule {

	private String form;
	private Vector<String> vector=new Vector<String>();
    
	public RewriteRule(String form,ArrayList<String> list) {
		this.form=form;
		for(String type: list) {
			this.vector.add(type);	
		}
	}
	public RewriteRule(String ...args) {
		this.form=args[0];
		for(int i=1;i<args.length;i++) {
			this.vector.add(args[i]);
		}
	}
	public RewriteRule(String form, Vector<TreeNode> children) {
		super();
        this.form=form;
        for(TreeNode node: children) {
        	this.vector.add(node.getValue());
        }
	}
	 public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public Vector<String> getVector() {
		return vector;
	}

	public void setVector(Vector<String> vector) {
		this.vector = vector;
	}

	 @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((form == null) ? 0 : form.hashCode());
		result = prime * result + ((vector == null) ? 0 : vector.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RewriteRule other = (RewriteRule) obj;
		if (form == null) {
			if (other.form != null)
				return false;
		} else if (!form.equals(other.form))
			return false;
		if (vector == null) {
			if (other.vector != null)
				return false;
		} else if (!vector.equals(other.vector))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder strb=new StringBuilder();
		strb.append("重写规则： " + form + " ->");
		for(String st: vector) {
			strb.append(st);
			strb.append(" ");
		}
		return strb.toString();
	}
}
