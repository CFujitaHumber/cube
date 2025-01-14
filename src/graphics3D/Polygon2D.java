  package graphics3D;
  
   import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
   
   /**
    * This class represents a polygon, a closed, two-dimensional region in a
   * coordinate space. The region is bounded by an arbitrary number of line
    * segments, between (x,y) coordinate vertices. The polygon has even-odd
    * winding, meaning that a point is inside the shape if it crosses the
    * boundary an odd number of times on the way to infinity.
    *
   * <p>There are some public fields; if you mess with them in an inconsistent
    * manner, it is your own fault when you get NullPointerException,
    * ArrayIndexOutOfBoundsException, or invalid results. Also, this class is
    * not threadsafe.
    *
   * @author Aaron M. Renn (arenn@urbanophile.com)
    * @author Eric Blake (ebb9@email.byu.edu)
    * @since 1.0
    * @status updated to 1.4
    */
  public class Polygon2D extends Polygon implements Shape, Serializable
   {
     /**
      * Compatible with JDK 1.0+.
      */
    private static final long serialVersionUID = -6460061437900069969L;
   
     /**
      * This total number of endpoints.
      *
     * @serial the number of endpoints, possibly less than the array sizes
      */
     public int npoints;
   
     /**
     * The array of X coordinates of endpoints. This should not be null.
      *
      * @see #addPoint(double, double)
      * @serial the x coordinates
      */
    public double[] xpoints;
   
     /**
      * The array of Y coordinates of endpoints. This should not be null.
      *
     * @see #addPoint(double, double)
      * @serial the y coordinates
      */
     public double[] ypoints;
   
    /**
      * The bounding box of this polygon. This is lazily created and cached, so
      * it must be invalidated after changing points.
      *
      * @see #getBounds()
    * @serial the bounding box, or null
     */
    protected Rectangle2D.Double bounds;
  
    /** A big number, but not so big it can't survive a few float operations */
   private static final double BIG_VALUE = java.lang.Double.MAX_VALUE / 10.0;
  
    /**
     * Initializes an empty polygon.
     */
   public Polygon2D()
    {
      // Leave room for growth.
      xpoints = new double[4];
      ypoints = new double[4];
   }
  
    /**
     * Create a new polygon with the specified endpoints. The arrays are copied,
     * so that future modifications to the parameters do not affect the polygon.
    *
     * @param xpoints the array of X coordinates for this polygon
     * @param ypoints the array of Y coordinates for this polygon
     * @param npoints the total number of endpoints in this polygon
     * @throws NegativeArraySizeException if npoints is negative
    * @throws IndexOutOfBoundsException if npoints exceeds either array
     * @throws NullPointerException if xpoints or ypoints is null
     */
    public Polygon2D(double[] xpoints, double[] ypoints, int npoints)
    {
     this.xpoints = new double[npoints];
      this.ypoints = new double[npoints];
      System.arraycopy(xpoints, 0, this.xpoints, 0, npoints);
      System.arraycopy(ypoints, 0, this.ypoints, 0, npoints);
      this.npoints = npoints;
   }
  
    /**
     * Reset the polygon to be empty. The arrays are left alone, to avoid object
     * allocation, but the number of points is set to 0, and all cached data
    * is discarded. If you are discarding a huge number of points, it may be
     * more efficient to just create a new Polygon.
     *
     * @see #invalidate()
     * @since 1.4
    */
    public void reset()
    {
      npoints = 0;
      invalidate();
   }
  
    /**
     * Invalidate or flush all cached data. After direct manipulation of the
     * public member fields, this is necessary to avoid inconsistent results
    * in methods like <code>contains</code>.
     *
     * @see #getBounds()
     * @since 1.4
     */
   public void invalidate()
    {
      bounds = null;
    }
  
   /**
     * Translates the polygon by adding the specified values to all X and Y
     * coordinates. This updates the bounding box, if it has been calculated.
     *
     * @param dx the amount to add to all X coordinates
    * @param dy the amount to add to all Y coordinates
     * @since 1.1
     */
    public void translate(int dx, int dy)
    {
     int i = npoints;
      while (--i >= 0)
        {
      xpoints[i] += dx;
      ypoints[i] += dy;
       }
      if (bounds != null)
        {
      bounds.x += dx;
      bounds.y += dy;
       }
    }
  
    /**
     * Adds the specified endpoint to the polygon. This updates the bounding
    * box, if it has been created.
     *
     * @param x the X coordinate of the point to add
     * @param y the Y coordiante of the point to add
     */
   public void addPoint(double x, double y)
    {
      if (npoints + 1 > xpoints.length)
        {
      double[] newx = new double[npoints + 1];
     System.arraycopy(xpoints, 0, newx, 0, npoints);
      xpoints = newx;
        }
      if (npoints + 1 > ypoints.length)
        {
    	  double[] newy = new double[npoints + 1];
      System.arraycopy(ypoints, 0, newy, 0, npoints);
      ypoints = newy;
        }
      xpoints[npoints] = x;
     ypoints[npoints] = y;
      npoints++;
      if (bounds != null)
        {
      if (npoints == 1)
       {
          bounds.x = x;
          bounds.y = y;
        }
      else
       {
          if (x < bounds.x)
            {
          bounds.width += bounds.x - x;
          bounds.x = x;
           }
          else if (x > bounds.x + bounds.width)
            bounds.width = x - bounds.x;
          if (y < bounds.y)
            {
         bounds.height += bounds.y - y;
          bounds.y = y;
            }
          else if (y > bounds.y + bounds.height)
            bounds.height = y - bounds.y;
       }
        }
    }
 
   
   
   
   
   /**
    * Returns the bounding box of this polygon. This is the smallest
     * rectangle with sides parallel to the X axis that will contain this
     * polygon.
     *
     * @return the bounding box for this polygon
    * @see #getBounds2D()
     * @since 1.1
     */
   public Rectangle getBounds()
   {
    return bounds.getBounds();
   }
  
    /**
     * Returns the bounding box of this polygon. This is the smallest
    * rectangle with sides parallel to the X axis that will contain this
     * polygon.
     *
     * @return the bounding box for this polygon
     * @see #getBounds2D()
    * @deprecated use {@link #getBounds()} instead
     */
    public Rectangle getBoundingBox()
    {
      if (bounds == null)
       {
      if (npoints == 0) 
      {
        bounds = new Rectangle2D.Double();
        return bounds.getBounds();
      }
      int i = npoints - 1;
      double minx = xpoints[i];
     double maxx = minx;
      double miny = ypoints[i];
      double maxy = miny;
      while (--i >= 0)
        {
         double x = xpoints[i];
          double y = ypoints[i];
          if (x < minx)
            minx = x;
          else if (x > maxx)
           maxx = x;
          if (y < miny)
            miny = y;
          else if (y > maxy)
            maxy = y;
       }
      bounds = new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny);
        }
      return bounds.getBounds();
    }
    
 
    /**
     * Tests whether or not the specified point is inside this polygon.
     *
     * @param p the point to test
    * @return true if the point is inside this polygon
     * @throws NullPointerException if p is null
     * @see #contains(double, double)
     */
    public boolean contains(Point p)
   {
      return contains(p.getX(), p.getY());
    }
  
    /**
    * Tests whether or not the specified point is inside this polygon.
     *
     * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return true if the point is inside this polygon
    * @see #contains(double, double)
     * @since 1.1
     */
    public boolean contains(int x, int y)
    {
     return contains((double) x, (double) y);
    }
  
    /**
     * Tests whether or not the specified point is inside this polygon.
    *
     * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return true if the point is inside this polygon
     * @see #contains(double, double)
    * @deprecated use {@link #contains(int, int)} instead
     */
    public boolean inside(int x, int y)
    {
      return contains((double) x, (double) y);
   }
  
    /**
     * Returns a high-precision bounding box of this polygon. This is the
     * smallest rectangle with sides parallel to the X axis that will contain
    * this polygon.
     *
     * @return the bounding box for this polygon
     * @see #getBounds()
     * @since 1.2
    */
    public Rectangle2D getBounds2D()
    {
      // For polygons, the integer version is exact!
      return getBounds();
   }
  
    /**
     * Tests whether or not the specified point is inside this polygon.
     *
    * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return true if the point is inside this polygon
     * @since 1.2
     */
   public boolean contains(double x, double y)
    {
      return ((evaluateCrossings(x, y, false, BIG_VALUE) & 1) != 0);
    }
  
   /**
     * Tests whether or not the specified point is inside this polygon.
     *
     * @param p the point to test
     * @return true if the point is inside this polygon
    * @throws NullPointerException if p is null
     * @see #contains(double, double)
     * @since 1.2
     */
    public boolean contains(Point2D p)
   {
      return contains(p.getX(), p.getY());
    }
  
    /**
    * Test if a high-precision rectangle intersects the shape. This is true
     * if any point in the rectangle is in the shape. This implementation is
     * precise.
     *
     * @param x the x coordinate of the rectangle
    * @param y the y coordinate of the rectangle
     * @param w the width of the rectangle, treated as point if negative
     * @param h the height of the rectangle, treated as point if negative
     * @return true if the rectangle intersects this shape
     * @since 1.2
    */
    public boolean intersects(double x, double y, double w, double h)
    {
      /* Does any edge intersect? */
      if (evaluateCrossings(x, y, false, w) != 0 /* top */
         || evaluateCrossings(x, y + h, false, w) != 0 /* bottom */
          || evaluateCrossings(x + w, y, true, h) != 0 /* right */
          || evaluateCrossings(x, y, true, h) != 0) /* left */
        return true;
  
     /* No intersections, is any point inside? */
      if ((evaluateCrossings(x, y, false, BIG_VALUE) & 1) != 0)
        return true;
  
      return false;
   }
  
    /**
     * Test if a high-precision rectangle intersects the shape. This is true
     * if any point in the rectangle is in the shape. This implementation is
    * precise.
     *
     * @param r the rectangle
     * @return true if the rectangle intersects this shape
     * @throws NullPointerException if r is null
    * @see #intersects(double, double, double, double)
     * @since 1.2
     */
    public boolean intersects(Rectangle2D r)
    {
     return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
  
    /**
     * Test if a high-precision rectangle lies completely in the shape. This is
    * true if all points in the rectangle are in the shape. This implementation
     * is precise.
     *
     * @param x the x coordinate of the rectangle
     * @param y the y coordinate of the rectangle
    * @param w the width of the rectangle, treated as point if negative
     * @param h the height of the rectangle, treated as point if negative
     * @return true if the rectangle is contained in this shape
     * @since 1.2
     */
   public boolean contains(double x, double y, double w, double h)
    {
      if (! getBounds2D().intersects(x, y, w, h))
        return false;
  
     /* Does any edge intersect? */
      if (evaluateCrossings(x, y, false, w) != 0 /* top */
          || evaluateCrossings(x, y + h, false, w) != 0 /* bottom */
          || evaluateCrossings(x + w, y, true, h) != 0 /* right */
          || evaluateCrossings(x, y, true, h) != 0) /* left */
       return false;
  
      /* No intersections, is any point inside? */
      if ((evaluateCrossings(x, y, false, BIG_VALUE) & 1) != 0)
        return true;
 
      return false;
    }
  
    /**
    * Test if a high-precision rectangle lies completely in the shape. This is
     * true if all points in the rectangle are in the shape. This implementation
     * is precise.
     *
     * @param r the rectangle
    * @return true if the rectangle is contained in this shape
     * @throws NullPointerException if r is null
     * @see #contains(double, double, double, double)
     * @since 1.2
     */
   public boolean contains(Rectangle2D r)
    {
      return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
  
   /**
     * Return an iterator along the shape boundary. If the optional transform
     * is provided, the iterator is transformed accordingly. Each call returns
     * a new object, independent from others in use. This class is not
     * threadsafe to begin with, so the path iterator is not either.
    *
     * @param transform an optional transform to apply to the iterator
     * @return a new iterator over the boundary
     * @since 1.2
     */
   public PathIterator getPathIterator(final AffineTransform transform)
    {
      return new PathIterator()
        {
      /** The current vertex of iteration. */
     private int vertex;
  
      public int getWindingRule()
      {
        return WIND_EVEN_ODD;
     }
  
      public boolean isDone()
      {
        return vertex > npoints;
     }
  
      public void next()
      {
        vertex++;
     }
  
      public int currentSegment(float[] coords)
      {
        if (vertex >= npoints)
         return SEG_CLOSE;
        coords[0] = (float) xpoints[vertex];
        coords[1] = (float) ypoints[vertex];
        if (transform != null)
          transform.transform(coords, 0, coords, 0, 1);
       return vertex == 0 ? SEG_MOVETO : SEG_LINETO;
      }
  
      public int currentSegment(double[] coords)
      {
       if (vertex >= npoints)
          return SEG_CLOSE;
        coords[0] = xpoints[vertex];
        coords[1] = ypoints[vertex];
        if (transform != null)
         transform.transform(coords, 0, coords, 0, 1);
        return vertex == 0 ? SEG_MOVETO : SEG_LINETO;
      }
        };
    }
 
    /**
     * Return an iterator along the flattened version of the shape boundary.
     * Since polygons are already flat, the flatness parameter is ignored, and
     * the resulting iterator only has SEG_MOVETO, SEG_LINETO and SEG_CLOSE
    * points. If the optional transform is provided, the iterator is
     * transformed accordingly. Each call returns a new object, independent
     * from others in use. This class is not threadsafe to begin with, so the
     * path iterator is not either.
     *
    * @param transform an optional transform to apply to the iterator
     * @param flatness the maximum distance for deviation from the real boundary
     * @return a new iterator over the boundary
     * @since 1.2
     */
   public PathIterator getPathIterator(AffineTransform transform,
                                        double flatness)
    {
      return getPathIterator(transform);
    }
 
    /**
     * Helper for contains, intersects, calculates the number of intersections
     * between the polygon and a line extending from the point (x, y) along
     * the positive X, or Y axis, within a given interval.
    *
     * @return the winding number.
     * @see #contains(double, double)
     */
    private int evaluateCrossings(double x, double y, boolean useYaxis,
                                 double distance)
    {
      double x0;
      double x1;
      double y0;
     double y1;
      double epsilon = 0.0;
      int crossings = 0;
      double[] xp;
      double[] yp;
 
      if (useYaxis)
        {
      xp = ypoints;
      yp = xpoints;
     double swap;
      swap = y;
      y = x;
      x = swap;
        }
     else
        {
      xp = xpoints;
      yp = ypoints;
        }
 
      /* Get a value which is small but not insignificant relative the path. */
      epsilon = 1E-7;
  
      x0 = xp[0] - x;
     y0 = yp[0] - y;
      for (int i = 1; i < npoints; i++)
        {
      x1 = xp[i] - x;
      y1 = yp[i] - y;
 
      if (y0 == 0.0)
        y0 -= epsilon;
      if (y1 == 0.0)
        y1 -= epsilon;
     if (y0 * y1 < 0)
        if (Line2D.Double.linesIntersect(x0, y0, x1, y1, epsilon, 0.0, distance, 0.0))
         ++crossings;
 
     x0 = xp[i] - x;
     y0 = yp[i] - y;
        }
 
     // end segment
     x1 = xp[0] - x;
     y1 = yp[0] - y;
     if (y0 == 0.0)
       y0 -= epsilon;
     if (y1 == 0.0)
       y1 -= epsilon;
     if (y0 * y1 < 0)
       if (Line2D.Double.linesIntersect(x0, y0, x1, y1, epsilon, 0.0, distance, 0.0))
     ++crossings;
 
     return crossings;
   }
  } // class Polygon
