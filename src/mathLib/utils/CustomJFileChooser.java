package mathLib.utils;

import java.io.File;

import javax.swing.JFileChooser;

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
		fc1.openDirectory();
		System.out.println(fc1.getSelectedDir());

	}
	//********************

}
