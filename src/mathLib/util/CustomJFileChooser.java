package mathLib.util;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class CustomJFileChooser {

	public JFileChooser fc ;
	public File selectedFile ;
	public static String path = System.getProperty("user.home") + File.separator +"Desktop" ;

	public CustomJFileChooser(){
		this.fc = new JFileChooser(path) ;
		this.fc.setDialogTitle("Select Path");
	}

	public void setPath(String path){
		fc = new JFileChooser(path) ;
	}

	public void openFile(){
		fc.showOpenDialog(fc) ;
		selectedFile = fc.getSelectedFile() ;
	}
	
	public void openFile(String... extensions){
		fc.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				StringBuilder st = new StringBuilder() ;
				for(int i=0; i<extensions.length-1; i++)
					st.append("."+extensions[i]+", ") ;
				st.append("."+extensions[extensions.length-1]) ;
				return st;
			}
			
			@Override
			public boolean accept(File f) {
				ArrayList<String> ext = new ArrayList<>() ;
				for(int i=0; i<extensions.length; i++) {
					ext.add(extensions[i]) ;

				}
				if(f.isDirectory())
					return true ;
				if(f != null && f.exists() && ext.contains(StringUtils.getFileExtension(f)))
					return true ;
				else
					return false ;
			}
		});
		fc.showOpenDialog(fc) ;
		selectedFile = fc.getSelectedFile() ;
	}

	public void openDirectory(){
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(fc) ;
		selectedFile = fc.getSelectedFile() ;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	}

	public void saveFile(){
		fc.showSaveDialog(fc) ;
		selectedFile = fc.getSelectedFile() ;
	}

	public File getSelectedFile(){
		return selectedFile ;
	}

	public String getSelectedDir(){
		return selectedFile.getPath() ;
	}

	public void showCurrentPath(){
		System.out.println(fc.getCurrentDirectory());
	}



	//**** for test *******
	public static void main(String[] args){
		CustomJFileChooser fc1 = new CustomJFileChooser() ;
		fc1.openFile("png", "txt");
		System.out.println(fc1.getSelectedDir());

	}
	//********************

}
