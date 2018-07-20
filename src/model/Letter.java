package model;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Letter {

	private Mat image;
	private char letter;
	
	private int[] value;
	
	public char getLetter() {
		return letter;
	}

	public int[] getValue() {
		return value;
	}

	public Letter(Mat img, char c) {
		this.letter=c;
		this.image=img;
		this.value= new int[5];
		this.signature();
	}
	
	public Letter(Mat img) {
		this.image=img;
		this.value= new int[5];
		this.signature();
	}
	
	private void signature() {
		int width=this.image.cols();
		int height=this.image.rows();

		double sum_half_left_top = Core.sumElems(this.image.submat(0, height/2, 0, width/2)).val[0];
		double sum_half_right_top=Core.sumElems(this.image.submat(height/2, height, 0, width/2)).val[0];
		double sum_half_left_bottom = Core.sumElems(this.image.submat(0, height/2, width/2, width)).val[0];
		double sum_half_right_bottom=Core.sumElems(this.image.submat(height/2, height, width/2, width)).val[0];
		
		this.value[0]=(int)((sum_half_left_top+sum_half_right_top) -(sum_half_left_bottom+sum_half_right_bottom));
		this.value[1]=(int)((sum_half_left_top+sum_half_left_bottom) -(sum_half_right_bottom+sum_half_right_top));
		this.value[2]=(int)((sum_half_left_top+sum_half_right_bottom) -(sum_half_right_top+sum_half_left_bottom));
		this.value[3]=(int)((sum_half_left_top+sum_half_left_bottom));
		this.value[4]=(int)((sum_half_right_top+sum_half_right_bottom));
	}
	
	public int distance(Letter lt) {
		int[] val_comp=lt.value;
		int dist=0;
		for(int i=0; i< val_comp.length;i++) {
			dist+=Math.pow(this.value[i]-val_comp[i],2);
		}
		return (int)Math.sqrt(dist);
	}
	
}
