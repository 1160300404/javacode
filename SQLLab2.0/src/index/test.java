package index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			File file=new File("result.txt");
			FileInputStream fip = new FileInputStream(file);
			for(int i=0;i<100;i++) {
				byte[] as = new byte[4];
				byte[] bs = new byte[12];
				fip.read(as);
				fip.read(bs);
				int a = byte4ToInt(as);
				String b = new String(bs);
				System.out.println(a+" "+b);
			}
			fip.close();
		}
			
	public static byte[] intToByte4(int i) {  
        byte[] targets = new byte[4];  
        targets[3] = (byte) (i & 0xFF);  
        targets[2] = (byte) (i >> 8 & 0xFF);  
        targets[1] = (byte) (i >> 16 & 0xFF);  
        targets[0] = (byte) (i >> 24 & 0xFF);  
        return targets;  
    }

	public static int byte4ToInt(byte[] bytes) {  
        int b0 = bytes[0] & 0xFF;  
        int b1 = bytes[1] & 0xFF;  
        int b2 = bytes[2] & 0xFF;  
        int b3 = bytes[3] & 0xFF;  
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;  
    }


}
