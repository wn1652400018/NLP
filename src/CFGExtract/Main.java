package CFGExtract;

import java.io.IOException;

public class Main {
	  public static void main(String args[]) throws IOException {
		  CFRExtract extract=new CFRExtract();
		  
		  //�鿴ȫ����д����
		  extract.GetMap("pd-tree-0001.txt","GBK");
		  
    	  //���ݲ鿴ĳ�����͵Ĺ��������ķ��ս�������ս��Ϊ��ѯ������ 
    	  System.out.println(extract.GetRuleList("NP"));
    	  
    	  //�����ķ�
    	  extract.updateRule("NP","CC","NP","WP");
    	  
    	  //�鿴����
    	  extract.ShowRules();
      }
}
