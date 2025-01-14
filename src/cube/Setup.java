package cube;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import graphics3D.Shape3D;
public class Setup extends JFrame  implements Runnable{
	
	private static final long serialVersionUID = -7342437600070108583L;
	
	/** Graphics Variables **/
	Thread runner; //Runs the animation
	Graphics2D bufferGraphics; // Double-Buffer graphics
	BufferedImage offscreen; // Offscreen image for double-buffer
	RenderingHints rh; //rendering hints to maintain the same render in double buffer
    Dimension canvas = new Dimension(400,400);
    
	//Shapes
	Shape3D shape1 =  Shape3D.createCuboid(-120, -20, -20, 240, 40, 40);
	Shape3D shape2 =  Shape3D.createCuboid(-120, -50, -30, -20, 100, 60);
	Shape3D shape3 =  Shape3D.createCuboid( 120, -50, -30,  20, 100, 60);
	Shape3D[] shapes = {shape1, shape2, shape3};

	private double mouseIncr = 0; //Value to increase speed of rotating boxes
	
public Setup (){
	super ("Carson's Demonstration");   // Set the frame's name
	setSize (canvas.width, canvas.height);     // Set the frame's size
	offscreen = new BufferedImage(canvas.width, canvas.height, BufferedImage.TRANSLUCENT); //Set image
	bufferGraphics = (Graphics2D) offscreen.createGraphics(); //set buffer
	
	rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
	bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	bufferGraphics.translate(200, 200);
	
	shape1.nodeVisible = false;
	shape2.nodeVisible = false;
	shape3.nodeVisible = false; 
	
	this.addMouseWheelListener(mouseWheelListener);
	this.addMouseListener(mouseListener);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);
} // Constructor

public void start() {
	if(runner == null) {
		runner = new Thread(this);
		runner.start();
		setVisible (true);                // Show the frame
	}
	

}

public void stop() {
	if(runner != null) {
		runner.interrupt();
		runner = null;
	}
	
}
 

public void run() {
	while(true) {
		repaint();
		
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	
 public void paint (Graphics g) {
	 Graphics2D g2 = (Graphics2D) g;
	 g2.addRenderingHints(rh);

	bufferGraphics.clearRect(-200, -200, canvas.width, canvas.height);


	
	
	sort();		 
	draw(bufferGraphics,shapes);

	
	shape1.rotateY3D(0.1);
	shape1.rotateX3D(0.1);
	shape2.rotateZ3D(mouseIncr +0.02);
	shape2.rotateY3D(mouseIncr  +0.1);
	shape3.rotateZ3D(mouseIncr +0.02);
	shape3.rotateY3D(mouseIncr +0.1);
	
	
	
	g2.drawImage(offscreen, 0, 0, this);
 } // paint method
 
 public void update(Graphics g) {
		paint(g);
}
	 
 
private void draw(Graphics2D g, Shape3D[] shapes2) {
	for(int index = 0; index < shapes2.length; index++) {
		shapes2[index].draw(g);
	}
}

private void sort() {
	this.quickSort(0, shapes.length-1);
}

public void quickSort(int low, int high) {
	if(low < high) {
		int partitioningIndex;
		
		partitioningIndex = getPartitioningIndex( low, high);
		
		quickSort(low, partitioningIndex-1);
		quickSort(partitioningIndex+1, high);
	
	}
}

 int getPartitioningIndex(int low, int high) {
	//pivot
	Shape3D pivot = this.shapes[high];
	
	int i = low - 1;
	for(int j = low; j<= high-1; j++) {
		if(this.shapes[j].getMinimumZ() < pivot.getMinimumZ()) {
			i++;
			swap(i ,j);
		}
	}
	swap(i+1, high);
	return i+1;
}



private void swap( int i, int j) {
	Shape3D placeholder = this.shapes[i];
	this.shapes[i] = this.shapes[j];
	this.shapes[j] = placeholder;
}


 boolean mousePressed = false;

 MouseListener mouseListener = new MouseListener() 
{


	@Override
	public void mouseClicked(MouseEvent e) {

		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseIncr=0.1;
		System.out.print(mouseIncr);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseIncr= 0;
		System.out.print(mouseIncr);
	}
};

/**
 * 
 */
MouseWheelListener mouseWheelListener = new MouseWheelListener() {

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
        if (notches < 0) { //scroll up
        } else {
        }
	}
	
};

public static void main (String[] args) {
	Setup setup = new Setup();
	setup.start();
 } // main method





} // Executable class






