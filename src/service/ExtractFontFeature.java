package service;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;


public class ExtractFontFeature {
	
	private Mat fontImage;
	private Mat srcImage;
	private LinkedList<Rect> letters = new LinkedList<>();
	
	public ExtractFontFeature(String fontPath) throws Exception {
		this.fontImage=new Mat();
		BufferedImage src= ImageIO.read(new File(fontPath));
		Mat srcImage=this.BufftoMat(src);
		
		Imgproc.cvtColor(srcImage, this.fontImage, Imgproc.COLOR_BGR2GRAY);
		//Imgproc.equalizeHist(this.fontImage, this.fontImage);
		Imgproc.threshold( this.fontImage, this.fontImage, 150,255,Imgproc.THRESH_BINARY );
		LinkedList<Rect> lines=this.ExtractLine();
		for(Rect r : lines) {
			ExtractCol(r);
		}
		
		for(Rect r : this.letters) {
			Mat line=srcImage.submat(r);
			this.displayImage(this.Mat2BufferedImage(line));
		}
	}
	
	private void ExtractCol(Rect r) {
		int ch = fontImage.channels();
		Mat image=fontImage.submat(r);
		boolean foundFirstLine=false;
		int x=0;
		int y=r.y;
		int width=0;
		int height=r.height;
		//go through rows
		for(int i = 0; i< image.cols();i++) {
			boolean isEmpty=true;
			
			//go through cols
			for (int j=0; j< image.rows();j++) {
				double[]temp= image.get(j, i);
				
				//go through channel 
				for(int k =0 ; k< ch;k++) {
					
					//if pixel is not empty
					if(temp[k]==0) {
						isEmpty=false;
					}
					else {
						
					}
					
				}
				
			}
			
			if(!isEmpty) {
			  if(!foundFirstLine) {
				  x=i;
				  foundFirstLine=true;
			  }
			  width++;
			}
			else {
				if(foundFirstLine) {
					this.letters.add(new Rect(x,y,width,height));
					foundFirstLine=false;
					width=0;
					x=0;
				}
			}
		}
	}
	
	
	private LinkedList<Rect> ExtractLine() {
		LinkedList<Rect> res=new LinkedList<>();
		int ch = fontImage.channels();
		boolean foundFirstLine=false;
		int x=0;
		int y=0;
		int width=fontImage.cols();
		int height=0;
		//go through rows
		for(int i = 0; i< this.fontImage.rows();i++) {
			boolean isEmpty=true;
			
			//go through cols
			for (int j=0; j< this.fontImage.cols();j++) {
				double[]temp= fontImage.get(i, j);
				
				//go through channel 
				for(int k =0 ; k< ch;k++) {
					
					//if pixel is not empty
					if(temp[k]==0) {
						isEmpty=false;
					}
					else {
						
					}
					
				}
				
			}
			
			if(!isEmpty) {
			  if(!foundFirstLine) {
				  y=i;
				  foundFirstLine=true;
			  }
			  height++;
			}
			else {
				if(foundFirstLine) {
					res.add(new Rect(x,y,width,height));
					foundFirstLine=false;
					height=0;
					y=0;
				}
			}
		}
		
		return res;
	}
	
	private Mat BufftoMat(BufferedImage im) {
		 byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer())
		            .getData();

		    // Create a Matrix the same size of image
		    Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_8UC3);
		    // Fill Matrix with image values
		    image.put(0, 0, pixels);

		    return image;
	}
	
	private BufferedImage Mat2BufferedImage(Mat m) {
		    // Fastest code
		    // output can be assigned either to a BufferedImage or to an Image

		    int type = BufferedImage.TYPE_BYTE_GRAY;
		    if ( m.channels() > 1 ) {
		        type = BufferedImage.TYPE_3BYTE_BGR;
		    }
		    int bufferSize = m.channels()*m.cols()*m.rows();
		    byte [] b = new byte[bufferSize];
		    m.get(0,0,b); // get all the pixels
		    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		    System.arraycopy(b, 0, targetPixels, 0, b.length);  
		    return image;
		}
	
	
	private void displayImage(Image img2) {

		//BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
	    ImageIcon icon=new ImageIcon(img2);
	    JFrame frame=new JFrame();
	    frame.setLayout(new FlowLayout());        
	    frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);     
	    JLabel lbl=new JLabel();
	    lbl.setIcon(icon);
	    frame.add(lbl);
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
