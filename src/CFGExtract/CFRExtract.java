package CFGExtract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Iterator;

public class CFRExtract {
	  /*
	   * 将括号表达式转换为ArrayList,去掉左括号，并在终结符左右添加一个括号
	   */
	 private static HashMap<String,HashMap<RewriteRule,Integer>> map=new HashMap<String,HashMap<RewriteRule,Integer>>();
	 
	 public ArrayList<String> GetString(String fileName,String enCoding) throws IOException{
		  InputStream file=new FileInputStream(new File(fileName));
    	  BufferedReader in=new BufferedReader(new InputStreamReader(file,enCoding));
    	  ArrayList<String> stringList=new ArrayList<String>();
    	  StringBuilder stb=new StringBuilder();
    	  in.readLine();
    	  String line=in.readLine();
    	  while(line!=null) {
    		  StringTokenizer line1=new StringTokenizer(line,"/n");
    		  String line2=line1.nextToken().toString();
    		  while(line2.contains("(")||line2.contains(")")) {
    			  stb.append(line2);
    			  line=in.readLine();
    			  if(line==null) {
    				  break;//读完整个文档后跳出循环
    			  }
        		  line1=new StringTokenizer(line,"/n");
        		  if(!line1.hasMoreTokens()) {
        			break;//读完一棵树后跳出while循环 
        		  }
        		  line2=line1.nextToken().toString();  
    		  }
    		  stringList.add(stb.toString());
    		  stb.delete(0,stb.length());
    		  in.readLine();//上一句是空白句
    		  line=in.readLine();//当上一句是汉语句子，直接读入下一句
    	  }
    	  return stringList;
	  }
      public ArrayList<String> GetArrayList2(String string) throws IOException {
    	  ArrayList<String> st=new ArrayList<String>();
    	  StringTokenizer stk=new StringTokenizer(string,"/n");
          while(stk.hasMoreTokens()) {     	  
        	  StringTokenizer str=new StringTokenizer(stk.nextToken().toString()," ");
        	  while(str.hasMoreTokens()) {
        		  String bracket=str.nextToken().toString();
        		  if(bracket.contains(("(")))//去除左括号
        		  {
        			  bracket=bracket.substring(1,bracket.length());
        	      }
    	    	  int count=0;//右括号数量多加一个，因为终结符少了一个括号
    	    	  while(bracket.endsWith(")")) {//将右括号与文本分离
    	    		  count++;
    	    		  bracket=bracket.substring(0,bracket.length()-1);
    	    	  }
				st.add(bracket);
				if(count>0) {
					count++;//在终结符后多加一个右括号，以便生成树
				while(count>0) {
					st.add(")");
					count--;
				}
        	  }
        	  }
          }
          return st;
      }
    /*
     * 由ArrayList得到树  
     */
	public TreeNode getTree(ArrayList<String> list) {
			if (list.isEmpty()) return null;
			TreeNode rootNode = new TreeNode();
			Stack<TreeNode> words = new Stack<TreeNode>();
			
			int len = list.size();
			for(int i =0 ;i<len ;i++) {//扫描list，
					if (!(list.get(i).equals(")"))) {//扫描到word
						if (words.isEmpty()) {//扫描到第一个word，添加根节点
							TreeNode root = new TreeNode();
							root.setRoot(root);
							root.setParent(null);
							root.setChildren(new Vector<TreeNode>());
							root.setValue(list.get(i));
							words.push(root);
							rootNode=root;
						} else {//扫描到后面的word
							TreeNode child = new TreeNode();
							child.setRoot(words.peek().getRoot());//根节点不变
							child.setParent(words.peek());
							words.peek().getChildren().add(child);
							child.setChildren(new Vector<TreeNode>());
							child.setValue(list.get(i));
							words.push(child);
						}
					} else {//扫描到 )
						if( ! words.isEmpty() ) {
							words.pop();
						}
					}					
			}
			if(rootNode!=null)
			{
				return rootNode;
			}
            return null;
		}
	/*
	 * 得到规则的整体映射
	 */
    public  HashMap<String,HashMap<RewriteRule,Integer>> GetMap(String fileName,String enCoding) throws IOException{//多叉树递归遍历
    	  TreeNode rootNode=null;
    	  CFRExtract writer=new CFRExtract();
    	  ArrayList<String> strList=writer.GetString(fileName,enCoding);
    	  for(String string:strList) {
    		  ArrayList<String> st= writer.GetArrayList2(string);
    		  rootNode= writer.getTree(st);
    		  traverseTree(rootNode);
    	  }
          return map;
      }
      //遍历树
      public void traverseTree(TreeNode node) {
    	  if(node.getChildren()==null) {
    		  return;
    	  }
    	  if(node.getChildren()!=null&&node.getChildren().size()>0) {
			  RewriteRule rule=new RewriteRule(node.getValue(),node.getChildren());
    		  if(map.containsKey(node.getValue())) {
    			  HashMap<RewriteRule,Integer> childrenMap= map.get(node.getValue());
    			  if(childrenMap.containsKey(rule)){
    				  childrenMap.put(rule, childrenMap.get(rule)+1);
    			  }else {
    				  childrenMap.put(rule,1);
    			  }
 				  map.put(node.getValue(),childrenMap);
 			  }else {
 				 HashMap<RewriteRule,Integer> childrenMap=new HashMap<RewriteRule,Integer>();
 				 childrenMap.put(rule,1);
 				 map.put(node.getValue(),childrenMap);
 			  }
    		 for(TreeNode node1:node.getChildren()) {
    			 traverseTree(node1);
    		 }
    	  }
    	  
      }
      /*
       * 得到集合中某一符号重写规则
       */
      public Set<RewriteRule> GetRuleList(String st){
    	  Set<RewriteRule> set=map.get(st).keySet();
    	  return set;
      }
      /*
       * 更新规则库
       */
      public static void updateRule(String ...args ) {
    	  RewriteRule rule=new RewriteRule(args);
    	  if(map.containsKey(rule.getForm())) {
			  HashMap<RewriteRule,Integer> childrenMap= map.get(rule.getForm());
			  if(childrenMap.containsKey(rule)){
				  childrenMap.put(rule, childrenMap.get(rule)+1);
			  }else {
				  childrenMap.put(rule,1);
			  }
				  map.put(rule.getForm(),childrenMap);
			  }else {
				 HashMap<RewriteRule,Integer> childrenMap=new HashMap<RewriteRule,Integer>();
				 childrenMap.put(rule,1);
				 map.put(rule.getForm(),childrenMap);
			  }
      }
      /*
       * 查看所有的重写规则 
       */
      public static void ShowRules() {
    	  Set<String> set=map.keySet();
    	  System.out.println("Key集合："+set);
    	  Iterator<String> itr=set.iterator();
    	  while(itr.hasNext()) {
    		  Set<RewriteRule> ruleSet=map.get(itr.next()).keySet();
    		  for(RewriteRule rule :ruleSet) {
    			  System.out.println(rule);
    		  }
    	  }
      }
  public static void main(String args[]) throws IOException {
    	  map=new CFRExtract().GetMap("pd-tree-0001.txt","GBK");
    	  //根据查看某种类型的规则（以左侧的非终结符或者终结符为查询变量）
    	  System.out.println(new CFRExtract().GetRuleList("NP"));
    	  //更新文法
    	  updateRule("NP","CC","NP","WP");
    	  //查看规则
    	  ShowRules();
      }
}
