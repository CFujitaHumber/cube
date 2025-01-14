package graphics3D;

import java.awt.Color;
import java.awt.Graphics2D;

public class Shape3D {
	
	/**
	 * The boolean responsible for indicating if the vertices of the shape/<code> Shape3D </code>object is visible to the user. This should not be null.
	 * @serial the visibility of every vertex. 
	 */
	public boolean nodeVisible = true;
	
	/**
	 * The array of all vertices containing points in the 3rd dimension.<small> (X,Y,Z)</small> <br> this should not be null.
	 * @serial the vertices of the shape.
	 */
	double[][] vertices;
	
	/**
	 * The size/diameter of the nodes when printed onto a canvas.
	 * @see #nodeVisible
	 * @serial diameter of the nodes.
	 */
	int nodeSize = 10; //TODO allow changes to nodesize
	
	/**
	 * an array containing the node in the shape with the closest Z position to negative infinity in and the node with the closest Z position to positive infinity.  
	 * @see #getMinMaxZ()
	 * @serial the nodes with the closest and deepest z position to the screen.
	 */
	private double[] minMaxZ;
	
	//faces for rectangle or square.
	
	/**
	 * contains the nodes with their X Y Z values of every face in the shape.<br> where: <br>X is [n][0].<br> Y is [n][1].<br> Z is [n][2]
	 * @serial contains the order of nodes in every face of the shape
	 */
	double[][] faces;
	
	/**
	 * contains the boolean value for every face of the shape that determines the visibility of the corrosponging face
	 * @see #faces
	 * @serial The visibility of each face.
	 */
	boolean[] showFaces;

	/**
	 * The color for each face in the shape
	 * @see #faces
	 * @serial color of every face.
	 */
	Color[] faceColors = 
	{
	    new Color(200, 0, 0), //front  RED
	    new Color(0, 200, 0), //top  GREEN
	    new Color(0, 0, 200), //left BLUE
	    new Color(250, 220, 0), //right  yellow
	    new Color(0, 200, 200), //bottom cyan
	    new Color(224, 88, 245), //back purple
	    new Color(25,25,25), //grey
	    new Color (65,65,65), //darker grey
	    new Color (115,115,115), //even darker grey
	}; //The colors of each face.
	
	/**
	 * Creates a new Shape3D object
	 * 
	 *
	 */
	protected Shape3D() 
	{
	}
	
	
	
	
	
	/**
	 * Creates a Shape3D object with the dimentions and position defined.
	 * @param x The X position.
	 * @param y The y position.
	 * @param z The Z position.
	 * @param w The width.
	 * @param h The height.
	 * @param d The depth.
	 */
	protected Shape3D(double x, double y, double z, double w, double h, double d) {
		
		double[][] nodes = {{x, y, z }, {x, y, z+d}, {x, y+h, z }, {x, y+h, z+d}, {x+w, y, z }, {x+w, y, z+d}, {x+w, y+h, z }, {x+w, y+h, z+d}};
		
		this.vertices = nodes;
		
		this.rotateZ3D(30);
		this.rotateY3D(30);
		this.minMaxZ = this.rotateX3D(30);
	};
	
	
	/**
	 * Creates a rectangular based prism shape.
	 * @param x The X position.
	 * @param y The Y position.
	 * @param z The Z position.
	 * @param w The width.
	 * @param h The height.
	 * @param d The depth.
	 * @return The rectangular based prism shape.
	 */
	public static Shape3D createRecPrism(double x, double y, double z, double w, double h, double d) 
	{
		Shape3D output = new Shape3D();
		
		double[] right =   {0,1,3,2}; // The nodes with lowest X coordinates
		double[] left =  {5,4,2,3}; // The nodes with highest X coordinates
		double[] top = {1,0,4,5}; // The nodes with highest Y coordinates
		double[] back =   {0,2,4}; // The nodes with lowest Z coordinates
		double[] front =  {1,3,5}; // The nodes with highest Z coordinates
		
		/*
		double[] right =   {0,1,3,2}; // The nodes with lowest X coordinates
		double[] left =  {5,4,2,3}; // The nodes with highest X coordinates
		double[] top = {1,0,4,5}; // The nodes with highest Y coordinates
		double[] back =   {0,2,4}; // The nodes with lowest Z coordinates
		double[] front =  {1,3,5}; // The nodes with highest Z coordinates
		*/
		
		double[][] faces = {front, top, left, right, back };
		
		//double[][] nodes = {{x,y,z},{x+w,y,z},{x,y,z+d},{x+w,y,z+d},{x+(w/2),y+h, z},{x+(w/2), y+h, z+d}};
		
		double[][] nodes = {{x,y,z},{x,y,z+d},{x+w/2,y+h,z},{x+w/2, y+h,z+d},{x+w, y, z},{x+w, y, z+d}};


		boolean[] visibleFaces = {true, true, true, true, true};
		output.showFaces = visibleFaces;
		output.vertices = nodes;
		output.faces = faces;
		
		output.minMaxZ = output.getMinMaxZ();
		return output;

	}
	
	/**
	 * Creates a simple cuboid with a rectangular prism on top in an attempt to make a simple house shape.
	 * @param x The X position.
	 * @param y The Y position.
	 * @param z The Z position.
	 * @param w The width.
	 * @param h The height.
	 * @param d The depth.
	 * @return a simple house shape.
	 */
	public static Shape3D createHouse(double x, double y, double z, double w, double h, double d) 
	{
		Shape3D output = new Shape3D();

		
		double[][] faces = {{0,2,8,6}, {2,8,4}, {2,0,1,3}, {3,2,4,5}, {3,5,9},{4,5,8,9},{6,7,8,9},{1,9,3,7},{6,0,1,7}};
		
	
		
		double[][] nodes = {{x,y,z},{x,y,z+d},{x,y+h/2,z},{x, y+h/2,z+d},{x+w/2, y+h, z},{x+ w/2, y+h, z+d},{x+w,y,z},{x+w,y, z+d},{x+w, y+h/2, z+d},{x+w, y+h/2, z}};


		boolean[] visibleFaces = {true, true, true, true, true, true, true, true, true};
		output.showFaces = visibleFaces;
		output.vertices = nodes;
		output.faces = faces;
		
		output.minMaxZ = output.getMinMaxZ();
		return output;

	}
	
	/**
	 * Creates a cuboid.
	 * @param x The X position.
	 * @param y The Y position.
	 * @param z The Z position.
	 * @param w The width.
	 * @param h The height.
	 * @param d The depth.
	 * @return the cuboid.
	 */
	public static Shape3D createCuboid(double x, double y, double z, double w, double h, double d) 
	{
		Shape3D output = new Shape3D();
		
		double[] left =   {0, 1, 3, 2}; // The nodes with negative X coordinates
		double[] right =  {4, 5, 7, 6}; // The nodes with positive X coordinates
		double[] top =    {0, 1, 5, 4}; // The nodes with negative Y coordinates
		double[] bottom = {2, 3, 7, 6}; // The nodes with positive Y coordinates
		double[] back =   {0, 2, 6, 4}; // The nodes with negative Z coordinates
		double[] front =  {1, 3, 7, 5}; // The nodes with positive Z coordinates
		
		double[][] faces = {front, top, left,right, bottom, back }; //faces for a cube
		double[][] nodes = {{x, y, z }, {x, y, z+d}, {x, y+h, z }, {x, y+h, z+d}, {x+w, y, z }, {x+w, y, z+d}, {x+w, y+h, z }, {x+w, y+h, z+d}};
		boolean[] visibleFaces = {true, true, true, true, true, true};
		output.showFaces = visibleFaces;
		output.vertices = nodes;
		output.faces = faces;

		output.minMaxZ = output.getMinMaxZ();
		return output;
	}
	
	
	/**
	 * returns the vertices in the shape.
	 * @return  the vertices in the shape.
	 * @see #vertices
	 */
	public double[][] getVertices()//returns the vertex double[][]
	{
		return this.vertices;
	}
	
	
	/**
	 * returns the faces in the shape.
	 * @return the faces in the shape.
	 * @see #faces
	 */
	public double[][] getFaces()//returns the faces...
	{
		return this.faces;
	}
	
	/**
	 * sets the color of each face.
	 * 
	 * @param front the color of the front face
	 * @param top the color of the top face
	 * @param left the color of the left face
	 * @param right the color of the right face
	 * @param bottom the color of the bottom face
	 * @param back the color of the back face
	 * @see #faceColors
	 */
	public void setFaceColors(Color front, Color top, Color left, Color right, Color bottom, Color back) 
	{
		setFaceColor(0,front);
		setFaceColor(1,top);
		setFaceColor(2,left);
		setFaceColor(3,right);
		setFaceColor(4,bottom);
		setFaceColor(5,back);
	}
	
	
	/**
	 * sets the color of an single face.
	 * @param index The index in faceColors. In this order: Front, Top, Left, Right, Bottom, Back.
	 * @param color The color of the face.
	 * @see #faceColors
	 * **/
	public void setFaceColor(int index, Color color) 
	{
		this.faceColors[index] = color;
	}
	
	/**
	 * Returns the colors of every face.
	 * @return the color of every face. 
	 */
	public Color[] getFaceColors() 
	{
		return this.faceColors;
	}
	
	
	/**
	 * an experimental method 
	 * @param persp the perspective
	 * @throws NullPointerException Vertices may not be defined
	 */
	void update(double persp) throws NullPointerException
	{
		for(int i = 0; i < this.vertices.length; i++) //for every node
		{
			double p = persp/(vertices[i][2] + persp); // perspective/(vertices Z + perspective)
			//multiply vertex by perspective
			vertices[i][0] = vertices[i][0] * p;  
			vertices[i][1] = vertices[i][1] * p;
		}
	}
	
	
	/**
	 * moves location of shape by parameters.

	 * @param z Z axis increase
	 */
	public void move(double x, double y, double z) 
	{
		for (int n = 0; n < this.vertices.length; n++) {
			this.vertices[n][0] += x;
			this.vertices[n][1] += y;
			this.vertices[n][2] += z;
		}
	}
	
	
	
	
	//************************Drawing Functions***************************//
	
	
	/**
	 * Draw the polygon face with fill color.
	 * @param g {@link Graphics2D} object to draw onto
	 * @param face the face
	 * @param color the color of the face
	 */
	void drawFace(Graphics2D g, double[] face, Color color) {
		//System.out.print("draw");
	    //TODO set Stroke to edgeColor
		//TODO set Stroke Weight
		g.setColor(color);//fill stroke color
		
	    Polygon2D polygon = new Polygon2D();
		//beginShape();
	    
	    for (int n = 0; n < face.length; n++) {
	    	double[] node = this.vertices[(int) face[n]];
	        polygon.addPoint(node[0], node[1]);
	    }
	    
	    g.fill(polygon);
	};
	
	public void draw(Graphics2D g) 
	{    
	    // Draw only the visible faces.
		
		System.out.println("\n\t\tPRINTING NEW SHAPE");
		System.out.println("Amount of faces: "+ faces.length);
		
		System.out.println("\tBeginning face drawing sequence");
	    for (int f = 0; f < faces.length; f++) {
	        if (faceIsVisible(faces[f]) && this.showFaces[f]) {
	            drawFace(g, faces[f], faceColors[f]);
	        }
	    }
	    
	    // Draw only the visible nodes.
	    //TODO strokeWeight(1);
	    //TODO fill(nodeColour);
	    //Draw nodes
	    if(this.nodeVisible) {
	    double r= nodeSize/2;
	    for (int n = 0; n < this.vertices.length; n++) {
	        if (this.vertices[n][2] > this.minMaxZ[0] || this.vertices[n][2] < this.minMaxZ[1]) {
	            g.drawOval((int)Math.round(this.vertices[n][0]-r),(int) Math.round(this.vertices[n][1]-r), nodeSize, nodeSize);
	        }
	    }
	    }
	};
	
	/**
	 * Return true iff the cube face is visible.
	 * In the case of a cube, at least three (invisible)
	 * faces are obscured by the visible faces. The 
	 * invisible faces shouldn't be drawn.
	 * 
	 * All the invisible faces share something in common,
	 * namely a node with the most negative Z coordinate.
	 * @param face the face to check visibility 
	 * @return boolean value of true if visible and false if not.
	 */
	boolean faceIsVisible(double[] face) {
		System.out.println("\n \n \nPrinting new face\n");
		System.out.println("Amount of Nodes: " + face.length);

		System.out.println("[0]: " + this.minMaxZ[0]);
		System.out.println("[1]: " + this.minMaxZ[1]);
		
		int matches = 0;
		int visiblenodes = 0;
		
	    for (int n = 0; n < face.length; n++) 
	    {
	    	//System.out.println((int) face[n]);
	    	double[] node = this.vertices[(int) face[n]];
	    	System.out.println("Vertex = (" + node[0] + "," + node[1] + "," + node[2] + ")");
	    	if(node[2] == this.minMaxZ[0] ) 
	    	{
	    		
	    		matches++;
	    	}
	    	if(node[2] == this.minMaxZ[1] )
	    	{
	    		visiblenodes++;
	    	}
	       
	        
	    }
	    
	    System.out.println("Matches: " + matches);
	    System.out.println("visiblenodes: " + visiblenodes);
	    
	    
	    if(visiblenodes >= 1) 
	    {
		    System.out.println("Face is clear to print");
	    	return true;
	    }
	    else 
	    {
	    	System.out.println(" [0] Face is not clear to print");
	    	return false;
	    }
	    
	};
	
	
	
	/**TRANSLATIONS**/
	/**
	 * Calculates the minimum and maximum Z points in the entire shape. <br>Note that the rotate functions automatically do this.
	 * @return an array containing the minimum Z and maximum Z points in the entire shape. 
	 * @see #minMaxZ
	 */
	private double[] getMinMaxZ()
	{

	    double minZ = Double.POSITIVE_INFINITY; //Deepest
	    double maxZ = Double.NEGATIVE_INFINITY; //closest
	    for (int n = 0; n < this.vertices.length; n++) {
	        minZ = Math.min(minZ, this.vertices[n][2]);
	        maxZ = Math.max(maxZ, this.vertices[n][2]);
	    }
	    double[] output ={minZ,maxZ};
	    return output;
	}

	
	/**
	 * rotates the 3D shape.
	 * @param theta the amount rotated
	 * @param xIdx  identifies if X, Y, or Z is translated for the rotation 
	 * @param yIdx identifies if X, Y, or Z is translated for the rotation
	 * @return an array containing the minimum Z and maximum Z points in the entire shape. 
	 */
	private double[] rotate3D(double theta,int xIdx,int yIdx) 
	{
	    double sinTheta = Math.sin(theta);
	    double cosTheta = Math.cos(theta);
	    double minZ = Double.POSITIVE_INFINITY; //Deepest
	    double maxZ = Double.NEGATIVE_INFINITY; //closest
	    for (int n = 0; n < this.vertices.length; n++) {
	        double x = this.vertices[n][xIdx];
	        double y = this.vertices[n][yIdx];
	        this.vertices[n][xIdx] = x * cosTheta - y * sinTheta;
	        this.vertices[n][yIdx] = y * cosTheta + x * sinTheta;
	        minZ = Math.min(minZ, this.vertices[n][2]);
	        maxZ = Math.max(maxZ, this.vertices[n][2]);
	    }
	    
	    double[] output ={minZ,maxZ};
	    return output;
	};
	
	
	/**
	 *  Rotate shape around the z-axis
	 * @param theta amount to rotate
	 * @return an array containing the minimum Z and maximum Z points in the entire shape. 
	 */
	public double[] rotateZ3D(double theta) {
	    return this.minMaxZ = rotate3D(theta, 0, 1);
	};

	/**
	 *  (Correctly) Rotate shape around the y-axis.
	 * @param theta amount to rotate.
	 * @return an array containing the minimum Z and maximum Z points in the entire shape. 
	 */
	public double[] rotateY3D(double theta) {
	    return this.minMaxZ = rotate3D(theta, 2, 0);
	};

	/**
	 *  Rotate shape around the x-axis.
	 * @param theta amount to rotate.
	 * @return an array containing the minimum Z and maximum Z points in the entire shape. 
	 */
	public double[] rotateX3D(double theta) {
	    return this.minMaxZ = rotate3D(theta, 1, 2);
	};
	
	/**
	 * returns the minimum z position in the entire shape based on the last rotation function used.
	 * @return the minimum z position in the entire shape.
	 */
	public double getMinimumZ() 
	{
		return this.minMaxZ[0];
	}
	
	/**
	 * returns the maximum/greatest value Z position in the entire shape.
	 * @return the maximum Z position in the entire shape.
	 */
	public double getMaximumZ() 
	{
		return this.minMaxZ[1];
	}
	
	
	/**
	 * hides the face by the index of the faces variable 
	 * @param face the face to hide
	 * @see #faces
	 */
	public void hideFace(int face) 
	{
		this.showFaces[face] = false;
	}
	
	
	
}

