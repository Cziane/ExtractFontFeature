package model;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Letter {

	private Mat image;
	private char letter;
	
	private double[] value;
	
	public char getLetter() {
		return letter;
	}

	public double[] getValue() {
		return value;
	}

	public Letter(Mat img, char c) {
		this.letter=c;
		this.image=img;
		this.value= new double[5];
		this.signature();
	}
	
	public Letter(Mat img) {
		this.image=img;
		this.value= new double[5];
		this.signature();
	}
	
	private void signature() {
		int width=this.image.cols();
		int height=this.image.rows();

		double sum_half_left_top = Core.sumElems(this.image.submat(0, height/2, 0, width/2)).val[0]/(width*height/4);
		double sum_half_right_top=Core.sumElems(this.image.submat(height/2, height, 0, width/2)).val[0]/(width*height/4);
		double sum_half_left_bottom = Core.sumElems(this.image.submat(0, height/2, width/2, width)).val[0]/(width*height/4);
		double sum_half_right_bottom=Core.sumElems(this.image.submat(height/2, height, width/2, width)).val[0]/(width*height/4);
		
		this.value[0]=((sum_half_left_top+sum_half_right_top) -(sum_half_left_bottom+sum_half_right_bottom));
		this.value[1]=((sum_half_left_top+sum_half_left_bottom) -(sum_half_right_bottom+sum_half_right_top));
		this.value[2]=((sum_half_left_top+sum_half_right_bottom) -(sum_half_right_top+sum_half_left_bottom));
		this.value[3]=((sum_half_left_top+sum_half_left_bottom));
		this.value[4]=((sum_half_right_top+sum_half_right_bottom));
	}
	
	public double distance(Letter lt) {
		double[] val_comp=lt.value;
		double dist=0;
		for(int i=0; i< val_comp.length;i++) {
			dist+=Math.pow(this.value[i]-val_comp[i],2);
		}
		return Math.sqrt(dist);
	}
	
}
