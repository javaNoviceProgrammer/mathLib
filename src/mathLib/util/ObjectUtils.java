package mathLib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils {
	
	public static void writeObjectToFile(Object obj, File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos) ;
			oos.writeObject(obj);
			fos.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object readObjectFromFile(File file) {
		FileInputStream fis = null;
		Object readObject = null ;
		try {
			fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis) ;
			
			try {
				readObject = ois.readObject() ;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			fis.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return readObject ;
	}
	

}

