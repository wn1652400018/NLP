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
	   * �����ű��ʽת��ΪArrayList,ȥ�������ţ������ս���������һ������
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
    				  break;//���������ĵ�������ѭ��
    			  }
        		  line1=new StringTokenizer(line,"/n");
        		  if(!line1.hasMoreTokens()) {
        			break;//����һ����������whileѭ�� 
        		  }
        		  line2=line1.nextToken().toString();  
    		  }
    		  stringList.add(stb.toString());
    		  stb.delete(0,stb.length());
    		  in.readLine();//��һ���ǿհ׾�
    		  line=in.readLine();//����һ���Ǻ�����ӣ�ֱ�Ӷ�����һ��
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
        		  if(bracket.contains(("(")))//ȥ��������
        		  {
        			  bracket=bracket.substring(1,bracket.length());
        	      }
    	    	  int count=0;//�������������һ������Ϊ�ս������һ������
    	    	  while(bracket.endsWith(")")) {//�����������ı�����
    	    		  count++;
    	    		  bracket=bracket.substring(0,bracket.length()-1);
    	    	  }
				st.add(bracket);
				if(count>0) {
					count++;//���ս������һ�������ţ��Ա�������
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
     * ��ArrayList�õ���  
     */
	public TreeNode getTree(ArrayList<String> list) {
			if (list.isEmpty()) return null;
			TreeNode rootNode = new TreeNode();
			Stack<TreeNode> words = new Stack<TreeNode>();
			
			int len = list.size();
			for(int i =0 ;i<len ;i++) {//ɨ��list��
					if (!(list.get(i).equals(")"))) {//ɨ�赽word
						if (words.isEmpty()) {//ɨ�赽��һ��word����Ӹ��ڵ�
							TreeNode root = new TreeNode();
							root.setRoot(root);
							root.setParent(null);
							root.setChildren(new Vector<TreeNode>());
							root.setValue(list.get(i));
							words.push(root);
							rootNode=root;
						} else {//ɨ�赽�����word
							TreeNode child = new TreeNode();
							child.setRoot(words.peek().getRoot());//���ڵ㲻��
							child.setParent(words.peek());
							words.peek().getChildren().add(child);
							child.setChildren(new Vector<TreeNode>());
							child.setValue(list.get(i));
							words.push(child);
						}
					} else {//ɨ�赽 )
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
	 * �õ����������ӳ��
	 */
    public  HashMap<String,HashMap<RewriteRule,Integer>> GetMap(String fileName,String enCoding) throws IOException{//������ݹ����
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
      //������
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
       * �õ�������ĳһ������д����
       */
      public Set<RewriteRule> GetRuleList(String st){
    	  Set<RewriteRule> set=map.get(st).keySet();
    	  return set;
      }
      /*
       * ���¹����
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
       * �鿴���е���д���� 
       */
      public static void ShowRules() {
    	  Set<String> set=map.keySet();
    	  System.out.println("Key���ϣ�"+set);
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
    	  //���ݲ鿴ĳ�����͵Ĺ��������ķ��ս�������ս��Ϊ��ѯ������
    	  System.out.println(new CFRExtract().GetRuleList("NP"));
    	  //�����ķ�
    	  updateRule("NP","CC","NP","WP");
    	  //�鿴����
    	  ShowRules();
      }
}
