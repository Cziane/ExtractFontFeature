package controller;

import service.ExtractFontFeature;

public class EFF {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.load("C:\\opencv\\build\\java\\x64\\opencv_java341.dll");
		ExtractFontFeature extractor=new ExtractFontFeature(args[0]);
		//extractor.ExtractLetter();
	}

}
