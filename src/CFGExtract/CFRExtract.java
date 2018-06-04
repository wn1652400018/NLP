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
	   * 首先定义一个静态的双层Map
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
    				  break;//文件读取完毕后跳出循环
    			  }
        		  line1=new StringTokenizer(line,"/n");
        		  if(!line1.hasMoreTokens()) {
        			break;//一棵树读完后跳出
        		  }
        		  line2=line1.nextToken().toString();  
    		  }
    		  stringList.add(stb.toString());
    		  stb.delete(0,stb.length());
    		  in.readLine();//上一行为空
    		  line=in.readLine();//上一行为中文句子
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
        		  if(bracket.contains(("(")))//取出左括号
        		  {
        			  bracket=bracket.substring(1,bracket.length());
        	      }
    	    	  int count=0;//右括号的数量
    	    	  while(bracket.endsWith(")")) {//寻找包含右括号的字符串
    	    		  count++;
    	    		  bracket=bracket.substring(0,bracket.length()-1);
    	    	  }
				st.add(bracket);
				if(count>0) {
					count++;//添加一个右括号，方便后续从list转换为树
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
     * 由list生成树
     */
	public TreeNode getTree(ArrayList<String> list) {
			if (list.isEmpty()) return null;
			TreeNode rootNode = new TreeNode();
			Stack<TreeNode> words = new Stack<TreeNode>();
			
			int len = list.size();
			for(int i =0 ;i<len ;i++) {
					if (!(list.get(i).equals(")"))) {
						if (words.isEmpty()) {//无根节点
							TreeNode root = new TreeNode();
							root.setRoot(root);
							root.setParent(null);
							root.setChildren(new Vector<TreeNode>());
							root.setValue(list.get(i));
							words.push(root);
							rootNode=root;
						} else {//有跟节点，则添加儿子节点
							TreeNode child = new TreeNode();
							child.setRoot(words.peek().getRoot());//根节点不变
							child.setParent(words.peek());
							words.peek().getChildren().add(child);
							child.setChildren(new Vector<TreeNode>());
							child.setValue(list.get(i));
							words.push(child);
						}
					} else {////扫描到 )，出栈
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
	 * 生成规则库的映射表
	 */
    public  void  GetMap(String fileName,String enCoding) throws IOException{//������ݹ����
    	  TreeNode rootNode=null;
    	  CFRExtract writer=new CFRExtract();
    	  ArrayList<String> strList=writer.GetString(fileName,enCoding);
    	  for(String string:strList) {
    		  ArrayList<String> st= writer.GetArrayList2(string);//括表达式生成ArrayList
    		  rootNode= writer.getTree(st);//ArrayList生成树
    		  traverseTree(rootNode);//遍历树，并在遍历的过程中将得到的树添加至map中
    	  }
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
       * 根据某一左侧的规则得到右侧重写规则的集合
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
       *查看所有的规则
       */
      public static void ShowRules() {
    	  Set<String> set=map.keySet();
    	  Iterator<String> itr=set.iterator();
    	  while(itr.hasNext()) {
    		  Set<RewriteRule> ruleSet=map.get(itr.next()).keySet();
    		  for(RewriteRule rule :ruleSet) {
    			  System.out.println(rule);
    		  }
    	  }
      }
}
