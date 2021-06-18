package application.util;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class CoordConverter {
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
	private static final double TEXTURE_OFFSET = 1.01;
	
	public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }
	
	public static Point2D SpaceCoordToGeoCoord(Point3D p) {
	    float lat = (float) (Math.asin(-p.getY() / TEXTURE_OFFSET) 
	                          * (180 / Math.PI) - TEXTURE_LAT_OFFSET);
	    float lon;
	    
	    if (p.getZ() < 0) {
	    	lon = 180 - (float) (Math.asin(-p.getX() / (TEXTURE_OFFSET 
		        * Math.cos((Math.PI / 180) 
	               * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI + TEXTURE_LON_OFFSET);
	    } else {
	    	lon = (float) (Math.asin(-p.getX() / (TEXTURE_OFFSET * Math.cos((Math.PI / 180) 
	    		* (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI - TEXTURE_LON_OFFSET);
	    }
	    
	    return new Point2D(lat, lon);
	}
}
