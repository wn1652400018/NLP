package CFGExtract;

import java.io.IOException;

public class Main {
	  public static void main(String args[]) throws IOException {
		  CFRExtract extract=new CFRExtract();
		  
		  //查看全部重写规则
		  extract.GetMap("pd-tree-0001.txt","GBK");
		  
    	  //根据查看某种类型的规则（以左侧的非终结符或者终结符为查询变量） 
    	  System.out.println(extract.GetRuleList("NP"));
    	  
    	  //更新文法
    	  extract.updateRule("NP","CC","NP","WP");
    	  
    	  //查看规则
    	  extract.ShowRules();
      }
}
