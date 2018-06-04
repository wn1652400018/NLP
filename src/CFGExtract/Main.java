package CFGExtract;

import java.io.IOException;

public class Main {
	  public static void main(String args[]) throws IOException {
		  CFRExtract extract=new CFRExtract();
		  
		  //读取文件获得文法(重写规则)Map
		  extract.GetMap("pd-tree-0002(新).txt","GBK");
		 
    	  //根据文法左侧的字符获取所有可重写的规则
    	  System.out.println("NP: "+extract.GetRuleList("NP"));
    	  
    	  //添加/更新文法
    	  extract.updateRule("NP","CC","NP","WP");
    	  //extract.updateRule(st,list);
    	  
    	  //展示规则
    	  extract.ShowRules();
      }
}
