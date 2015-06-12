
	import java.awt.List;
	import java.awt.image.BufferedImage;
	import java.awt.image.DataBufferByte;
	import java.io.ByteArrayInputStream;
	import java.io.File;
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.ArrayList;

	import javax.imageio.ImageIO;
	import javax.swing.ImageIcon;
	import javax.swing.JFrame;
	import javax.swing.JLabel;

	import org.opencv.core.Core;
	import org.opencv.core.CvType;
	import org.opencv.core.Mat;
	import org.opencv.core.MatOfByte;
	import org.opencv.core.MatOfInt;
	import org.opencv.core.MatOfInt4;
	import org.opencv.core.MatOfPoint;
	import org.opencv.core.Point;
	import org.opencv.core.Scalar;
	import org.opencv.core.Size;
	import org.opencv.imgcodecs.Imgcodecs;
	import org.opencv.imgproc.Imgproc;
	import org.opencv.core.MatOfPoint;


	public class Main2 {



	public static void imshow(Mat img) {
	    Imgproc.resize(img, img, new Size(img.cols(),img.rows()));
	    MatOfByte matOfByte = new MatOfByte();
	    Imgcodecs.imencode(".jpg", img, matOfByte);
	    //Highgui.imencode(".jpg", img, matOfByte);
	    byte[] byteArray = matOfByte.toArray();
	    BufferedImage bufImage = null;
	    try {
	        InputStream in = new ByteArrayInputStream(byteArray);
	        bufImage = ImageIO.read(in);
	        JFrame frame = new JFrame();
	        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
	        frame.pack();
	        frame.setVisible(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	/// Global variables
	static Mat src, src_gray;
	static int thresh = 200;
	static int max_thresh = 255;

	static String source_window = "Source image";
	static String corners_window = "Corners detected";
	
	static void cornerHarris_demo( )
	{

	  Mat dst=new Mat();
	  Mat dst_norm=new Mat();
	  Mat dst_norm_scaled=new Mat();
	  dst = Mat.zeros( src.size(), CvType.CV_32FC1 );

	  /// Detector parameters
	  int blockSize = 2;
	  int apertureSize = 3;
	  double k = 0.04;

	  /// Detecting corners
	  Imgproc.cornerHarris( src_gray, dst, blockSize, apertureSize, k);//, Imgproc.BORDER_DEFAULT );

	  /// Normalizing
	  Core.normalize( dst, dst_norm, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat() );
	  Core.convertScaleAbs( dst_norm, dst_norm_scaled );

	  /// Drawing a circle around corners
	  for( int j = 0; j < dst_norm.rows() ; j++ ) {
		  for( int i = 0; i < dst_norm.cols(); i++ ) {
			    double[]get=dst_norm.get(j,i);
	            if( (int)get[0]>thresh) {//(int) dst_norm.get(j,i) > thresh ) {
	               Imgproc.circle( dst_norm_scaled, new Point( i, j ), 5,  new Scalar(0), 2, 8, 0 );
	            }
	          }
	      }
	  imshow(dst_norm_scaled);

	}
    public static void main(String[] args) {
    	 System.out.println("Welcome to OpenCV " + Core.VERSION);
         System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	 BufferedImage im=null;
         String filename="Harris_Detector_Original_Image.jpg";
         try {
             im = ImageIO.read( new File(filename) );

         }
         catch (Exception e) {
             System.out.println("Could not read image "+filename+" "+e.getMessage());
            // e.printStackTrace();
         }
    	BufferedImage image = im;
    	byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    	src = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
    	src.put(0, 0, data);
    	src_gray=new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
    	Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_RGB2GRAY);//Imgproc.CV_BGR2GRAY );
    	cornerHarris_demo( );

    }

}


