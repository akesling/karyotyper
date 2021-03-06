package basic_objects;

/*
 * cluster.java
 *
 * Created on December 14, 2004, 7:09 PM
 */
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * @author andrew
 * 
 */
public class Cluster {
	private boolean[][] clusterX;
	private Point clusterSize;
	private Point imageLocation;
	private Point firstPixel;
	private String title;
	private Cluster next;
	private int pixelCount;

	private boolean removeThisCluster;

	public Cluster() {
		clusterX = new boolean[50][50];
		clusterSize = new Point(50, 50);
		initCluster();
	}

	public Cluster(Cluster makeNew) {
		// System.out.println("ClusterSize: "+makeNew.ClusterSize.x+","+makeNew.ClusterSize.y);
		clusterX = new boolean[makeNew.clusterSize.x][makeNew.clusterSize.y];
		initCluster();
		copyCluster(makeNew);
	}

	public Cluster(Point size) {
		clusterX = new boolean[size.x][size.y];
		initCluster();
		this.clusterSize=size;
	}

	public Cluster(short[][] map, int xPoint, int yPoint, int clusterColorID,Point canvasStart) {
		clusterX = new boolean[map.length][map[0].length];
		initCluster();
		setCluster(map, new Point(xPoint, yPoint), clusterColorID,canvasStart);
	}

	private void initCluster() {
		imageLocation = new Point(-1, -1);
		firstPixel = new Point(-1, -1);
		title = "";
		next = null;
		clusterSize = new Point(0, 0);
		pixelCount = 0;
		removeThisCluster = true;

		initArray();

	}

	private void initArray() {
		for (int j = 0; j < clusterX[0].length; j++)
			for (int i = 0; i < clusterX.length; i++)
				clusterX[i][j] = false;

	}

	/**
	 * this creates a cluster from a 2d array of short integers and records the clusters location in
	 * the image
	 * 
	 * @param map
	 *            2d array of short integers that represent a cluster of pixels
	 * @param xPoint
	 *            the x cordinate of where a certain pixel in this cluster is located in the image
	 * @param yPoint
	 *            the y cordinate of where a certain pixel in this cluster is located in the image
	 * @param clusterColorID
	 *            the number marked all over the 2d array of shorts that represents the cluster
	 */
	public void setCluster(short[][] map, Point imgPoint, int clusterColorID,Point canvasStart) {
		int firstCol = -1;
		int firstRow = -1;
		int bottomRow = -1;
		int farthestRight = -1;
		initCluster();
		int yRefPoint = imgPoint.y;
		int xRefPoint = imgPoint.x;
		if (clusterColorID == 0) {
			yRefPoint -= canvasStart.y;
			xRefPoint -= canvasStart.x;
		}
		// find the first column and row that contain anything
		for (int i = 0; i < map[0].length; i++) {
			for (int k = 0; k < map.length; k++) {
				if (map[k][i] == clusterColorID) {
					pixelCount++;
					if (bottomRow < i) {
						bottomRow = i;
					}
					if (farthestRight < k) {
						farthestRight = k;
					}

					if (firstCol > k||firstCol==-1) {
						firstCol = k;
						setImageLocation(new Point(xRefPoint + k, imageLocation.y));
					}
					if (firstRow == -1) {
						firstCol = k;
						firstPixel.setLocation(k, i);
						firstRow = i;
						setImageLocation(new Point(imageLocation.x, yRefPoint + i));
					}
				}

			}
		}
		this.clusterX=new boolean [farthestRight+1-firstCol][bottomRow+1-firstRow];
		this.clusterSize=new Point(farthestRight+1-firstCol,bottomRow+1-firstRow);
		for (int i = firstRow; i <= bottomRow; i++) { // put map into Cluster
			for (int k = firstCol; k <= farthestRight; k++) {
				if (map[k][i] == clusterColorID) {
					clusterX[k - firstCol][i - firstRow] = true;
				}

			}
		}
		// firstPixel.setLocation(firstPixel.x-firstCol,firstPixel.y-firstRow);
		//this.clusterSize = new Point(calcSize());

	}

	/**
	 * checks to see if this cluster is the same as another cluster
	 * 
	 * @param isCluster
	 *            a cluster to be compared to this one
	 * @return true if the clusters are the same
	 */
	public boolean isSame(Cluster isCluster) {
		for (int j = 0; j < clusterX[0].length; j++)
			for (int i = 0; i < clusterX.length; i++)
				if (clusterX[i][j] != isCluster.getPos(i, j))
					return false;
		return true;
	}

	public int getPixelCount() {
		return pixelCount;
	}

	public boolean getPos(int x, int y) {
		return clusterX[x][y];
	}

	public boolean getPos(Point posX) {
		return clusterX[posX.x][posX.y];
	}

	public boolean getValue(int x, int y) {
		return clusterX[x][y];
	}

	public boolean getValue(Point posX) {
		return clusterX[posX.x][posX.y];
	}

	/**
	 * sets a position in the square area of the cluster to be part of the cluster or to not be and
	 * increments or decrements the pixel count of the cluster if needed
	 * 
	 * @param xy
	 *            the location in the cluster to add or remove
	 * @param newValue
	 *            true to add false to remove
	 */
	public void setPixel(Point xy, boolean newValue) {
		if (!newValue && clusterX[xy.x][xy.y]) {
			this.pixelCount--;
		} else if (newValue && !clusterX[xy.x][xy.y]) {
			this.pixelCount++;
		}
		clusterX[xy.x][xy.y] = newValue;

	}

	public Point getImageLocation() {
		return imageLocation;
	}

	public Point getImageLocationcenter() {
		int x = imageLocation.x + ((int) Math.floor(this.clusterSize.x / 2));
		int y = imageLocation.y + ((int) Math.floor(this.clusterSize.y / 2));
		return new Point(x, y);
	}

	public void setImageLocation(Point xy) {
		if (xy.x < 0)
			xy.x = 0;
		if (xy.y < 0)
			xy.y = 0;
		this.imageLocation.setLocation(xy.x, xy.y);
	}

	/**
	 * returns the width and height of the smallest box that contains the cluster as a point with
	 * the x as width and y as height
	 * 
	 * @return point that x is the width and y is the height
	 */
	public Point getSize() {
		return clusterSize;
	}

	public void setSize(int x, int y) {
		clusterSize.x = x;
		clusterSize.y = y;
	}
	public void outCanvas(short[][] canvas){
		for (int j = 0; j < canvas[0].length; j++) {
			for (int i = 0; i < canvas.length; i++) {
				System.out.print(canvas[i][j]);
			}
			System.out.println();
		}
			

	}

	public Cluster getNext() {
		return next;
	}

	public void setNext(Cluster clusterN) {
		next = clusterN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String wordTitle) {
		title = wordTitle;
	}

	public boolean[][] getCluster() {
		return clusterX;
	}

	public void setkeepThisCluster() {
		this.removeThisCluster = false;
	}

	public boolean checkKeepThisCluster() {
		return (!this.removeThisCluster);
	}

	/**
	 * used to create a duplicate cluster
	 * 
	 * @param copyCluster
	 *            cluster to duplicate
	 */
	public void copyCluster(Cluster copyCluster) {
		this.removeThisCluster = copyCluster.removeThisCluster;
		boolean foundFirstPix = false;
		this.next = copyCluster.next;
		// this.myBuckets.copyBuckets(copyCluster.myBuckets.getBucketArray());
		setImageLocation(copyCluster.getImageLocation());
		title = copyCluster.getTitle();
		this.clusterSize = new Point(copyCluster.clusterSize);
		for (int j = 0; j < clusterX[0].length; j++) {
			for (int i = 0; i < clusterX.length; i++) {
				if (copyCluster.getPos(i, j)) {
					this.pixelCount++;
					if (!foundFirstPix) {
						firstPixel.setLocation(i, j);
						foundFirstPix = true;
					}
					clusterX[i][j] = true;
				}
			}
		}
	}

	/**
	 * this prints to the console a visual representation of the cluster
	 */
	public void clusterOut() {
		System.out.println("ScreenLocation:" + this.imageLocation.x + "," + this.imageLocation.y);
		System.out.println(this.getSize().x + "," + this.getSize().y + " : " + this.getTitle());
		System.out.println("PixelCount: " + pixelCount);
		for (int i = 0; i < this.getSize().y; i++) {
			for (int j = 0; j < this.getSize().x; j++) {
				if (this.getPos(j, i)) {
					System.out.print('M');
				} else {
					System.out.print('_');
				}
			}
			System.out.println("");
		}
	}

	public int calcSides() {
		int numSides = 0;

		return numSides;
	}

	/**
	 * this returns the point in the first row of the cluster box where the first part of the
	 * cluster occurs
	 * 
	 * @return the point in the first row of the cluster box where the first part of the cluster
	 *         occurs
	 */
	public Point getFirstPixel() {
		return firstPixel;
	}
	public LinkedList<Point> getPointList() {
		LinkedList<Point> truePoints=new LinkedList<Point>();
		for (int i = 0; i < this.getSize().y; i++) {
			for (int j = 0; j < this.getSize().x; j++) {
				if (this.getPos(j, i)) {
					truePoints.add(new Point(imageLocation.x+j,imageLocation.y+i));
				}
			}
		}
		return truePoints;
	}
	public Rectangle getBounds(){
		return new Rectangle(0,0,this.getSize().x,this.getSize().y);
	}
}
