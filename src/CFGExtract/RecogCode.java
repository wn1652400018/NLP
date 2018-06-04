package CFGExtract;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class RecogCode {
	/*
	  * 识别编码规则
	  */
	 public static String getCharset(String fileName) throws IOException {

			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
			int p = (bin.read() << 8) + bin.read();

			String code = null;

			switch (p) {
			case 0xefbb:
				code = "UTF-8";
				break;
			case 0xfffe:
				code = "Unicode";
				break;
			case 0xfeff:
				code = "UTF-16BE";
				break;
			default:
				code = "GBK";
			}
			return code;
		}
}
