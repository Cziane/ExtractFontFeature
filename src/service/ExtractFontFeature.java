package service;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import model.Letter;


public class ExtractFontFeature {
	
	private Mat fontImage;
	private Mat srcImage;
	private ArrayList<Rect> letters = new ArrayList<>();
	private ArrayList<Letter> letters_list=new ArrayList<>();
	private char[] charactersList= {
			'A',
			'B',
			'C',
			'D',
			'E',
			'F',
			'G',
			'H',
			'I',
			'J',
			'K',
			'L',
			'M',
			'N',
			'O',
			'P',
			'Q',
			'R',
			'S',
			'T',
			'U',
			'V',
			'W',
			'X',
			'Y',
			'Z',
			'a',
			'b',
			'c',
			'd',
			'e',
			'f',
			'g',
			'h',
			'i',
			'j',
			'k',
			'l',
			'm',
			'n',
			'o',
			'p',
			'q',
			'r',
			's',
			't',
			'u',
			'v',
			'w',
			'x',
			'y',
			'z',
			'0',
			'1',
			'2',
			'3',
			'4',
			'5',
			'6',
			'7',
			'8',
			'9',
			'.',
			':',
			',',
			';',
			'(',
			'?',
			'!',
			'?',
			')',
			'?',
			'$',
			'?',
			'&',
			'-',
			'+',
			'?'		
	};
	public ExtractFontFeature(String fontPath) throws Exception {
		this.fontImage=new Mat();
		BufferedImage src= ImageIO.read(new File(fontPath));
		Mat srcImage=this.BufftoMat(src);
		
		Imgproc.cvtColor(srcImage, this.fontImage, Imgproc.COLOR_BGR2GRAY);
		//Imgproc.equalizeHist(this.fontImage, this.fontImage);
		Imgproc.threshold( this.fontImage, this.fontImage, 150,255,Imgproc.THRESH_BINARY );
		LinkedList<Rect> lines=this.ExtractLine(this.fontImage);
		for(Rect r : lines) {
			ExtractCol(r,this.fontImage,this.letters);
		}
		
		
		int indexChar=0;
		for(Rect r : this.letters) {
			Mat line=fontImage.submat(r);
			letters_list.add(new Letter(line,this.charactersList[indexChar]));
			this.displayImage(this.Mat2BufferedImage(this.fontImage.submat(r)));
			indexChar++;
		}
		
		/*String result="";
		for (int i=0; i< letters_list.size();i++) {
			int[] distances=new int[this.letters_list.size()];
			for (int j=0; j < letters_list.size();j++) {
				distances[j]=letters_list.get(i).distance(letters_list.get(j));
				System.out.println(letters_list.get(j).getLetter()+" : "+ distances[j]);
			}
			result+=this.letters_list.get(findMinIdx(distances)).getLetter();
		}
		System.out.println(result);*/
	}
	
	public void TestImage(String path) throws IOException {
		Mat testImage=new Mat();
		Mat imageOr=new Mat();
		Rect roi=new Rect(14, 14, 206, 68);
		BufferedImage src= ImageIO.read(new File(path));
		testImage=this.BufftoMat(src).submat(roi);
		imageOr=testImage.clone();
		//this.displayImage(this.Mat2BufferedImage(imageOr));
		Imgproc.cvtColor(testImage, testImage, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold( testImage, testImage, 150,255,Imgproc.THRESH_BINARY );
		this.displayImage(this.Mat2BufferedImage(testImage));
		LinkedList<Rect> lines=this.ExtractLine(testImage);
		ArrayList<Rect> lets=new ArrayList<>();
		ArrayList<Letter> letters_list=new ArrayList<>();
		for(Rect r : lines) {
			ExtractCol(r,testImage,lets);
		}
		String result="";
		for(Rect rec : lets) {
			this.displayImage(this.Mat2BufferedImage(testImage.submat(rec)));
			Letter target=new Letter(testImage.submat(rec));
			double[] distances=new double[this.letters_list.size()];
			for(int i=0;i<this.letters_list.size();i++) {
				distances[i]=target.distance(this.letters_list.get(i));
				System.out.println(this.letters_list.get(i).getLetter() + " dist = " + distances[i]);
			}
			//System.out.println(findMinIdx(distances));
			result+=this.letters_list.get(findMinIdx(distances)).getLetter();
			System.out.println("---");
		}
			System.out.println(result);
		
		
	}
	 public static int findMinIdx(double[] numbers) {
	        int idx=0;
	        double value=numbers[0];
	        for(int i=1;i< numbers.length;i++) {
	        	if(numbers[i]<value) {
	        		idx=i;
	        		value=numbers[i];
	        	}
	        }
	        return idx;
	    }
	
	private void ExtractCol(Rect r,Mat img, List<Rect> myList) {
		int ch = img.channels();
		Mat image=img.submat(r);
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
					myList.add(new Rect(x,y,width,height));
					foundFirstLine=false;
					width=0;
					x=0;
				}
			}
		}
	}
	
	
	private LinkedList<Rect> ExtractLine(Mat img) {
		LinkedList<Rect> res=new LinkedList<>();
		int ch = img.channels();
		boolean foundFirstLine=false;
		int x=0;
		int y=0;
		int width=img.cols();
		int height=0;
		//go through rows
		for(int i = 0; i< img.rows();i++) {
			boolean isEmpty=true;
			
			//go through cols
			for (int j=0; j< img.cols();j++) {
				double[]temp= img.get(i, j);
				
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
