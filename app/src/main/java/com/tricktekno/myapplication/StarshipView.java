package com.tricktekno.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class displays a bitmap. Methods are provided to set the rotation, scale, and offsets (x, y)
 * of the bitmap.
 */

public class StarshipView extends View {

    private Matrix mMatrix = new Matrix ();

/**
 */
// Constructors

public StarshipView (Context context) {
	super (context);
}
public StarshipView (Context context, AttributeSet attrs) {
	super (context, attrs);
}
public StarshipView (Context context, AttributeSet attrs, int style) {
	super (context, attrs, style);
}

/**
 */
// Properties

/* Property Bitmap */
/**
 * This variable holds the value of the Bitmap property.
 */

   private Bitmap pBitmap;

/**
 * Get the value of the Bitmap property.
 * 
 * @return Bitmap
 */

public Bitmap getBitmap () {
   return pBitmap;
} // end getBitmap

/**
 * Set the value of the Bitmap property.
 * 
 * @param newValue Bitmap
 */

public void setBitmap (Bitmap newValue) {
   pBitmap = newValue;
} // end setBitmap
/* end Property Bitmap */

/* Property Degrees */
/**
 * This variable holds the value of the Degrees property.
 */

   private int pDegrees;

/**
 * Get the number of degrees of rotation.
 * 
 * @return int
 */

public int getDegrees () {
   //if (pDegrees == null) {}
   return pDegrees;
} // end getDegrees

/**
 * Set the number of degrees of rotation.
 * 
 * @param newValue int
 */

public void setDegrees (int newValue) {
   pDegrees = newValue;
} // end setDegrees
/* end Property Degrees */

/* Property OffsetX */
/**
 * This variable holds the value of the OffsetX property.
 */

   private float pOffsetX;

/**
 * Get the value of the OffsetX property.
 * 
 * @return float
 */

public float getOffsetX () {
   //if (pOffsetX == null) {}
   return pOffsetX;
} // end getOffsetX

/**
 * Set the value of the OffsetX property.
 * 
 * @param newValue float
 */

public void setOffsetX (float newValue) {
   pOffsetX = newValue;
} // end setOffsetX
/* end Property OffsetX */

/* Property OffsetY */
/**
 * This variable holds the value of the OffsetY property.
 */

   private float pOffsetY;

/**
 * Get the value of the OffsetY property.
 * 
 * @return float
 */

public float getOffsetY () {
   //if (pOffsetY == null) {}
   return pOffsetY;
} // end getOffsetY

/**
 * Set the value of the OffsetY property.
 * 
 * @param newValue float
 */

public void setOffsetY (float newValue) {
   pOffsetY = newValue;
} // end setOffsetY
/* end Property OffsetY */


/* Property Scale */
/**
 * This variable holds the value of the Scale property.
 */

   private float pScale = 1.0f;

/**
 * Get the value of the Scale property.
 * 
 * @return float
 */

public float getScale () {
   //if (pScale == null) {}
   return pScale;
} // end getScale

/**
 * Set the value of the Scale property.
 * 
 * @param newValue float
 */

public void setScale (float newValue) {
   pScale = newValue;
} // end setScale
/* end Property Scale */

/**
 */
// Methods


/**
 * Draw the bitmap onto the canvas.
 *
 * The following transformations are done using a Matrix object:
 * (1) the bitmap is scaled to fit within the view;
 * (2) the bitmap is translated up and left half the width and height, to support rotation around the center;
 * (3) the bitmap is rotated N degrees;
 * (4) the bitmap is translated to the specified offset valuess.
 */

@Override public void onDraw(Canvas canvas) {
    if (pBitmap == null) return;

    // Use the same Matrix over and over again to minimize 
    // allocation in onDraw.
    Matrix matrix = mMatrix;        
    matrix.reset ();
 
    float vw = this.getWidth ();
    float vh = this.getHeight ();
    float hvw = vw / 2;
    float hvh = vh / 2;
    float bw = (float) pBitmap.getWidth ();
    float bh = (float) pBitmap.getHeight ();
 
    // First scale the bitmap to fit into the view. 
    // Use either scale factor for width and height, 
    // whichever is the smallest.
    float s1x = vw / bw;
    float s1y = vh / bh;
    float s1 = (s1x < s1y) ? s1x : s1y;
    matrix.postScale (s1, s1);
 
    // Translate the image up and left half the height 
    // and width so rotation (below) is around the center.
    matrix.postTranslate(-hvw, -hvh);

    // Rotate the bitmap the specified number of degrees.
    int rotation = getDegrees ();
    matrix.postRotate(rotation);

    // If the bitmap is to be scaled, do so.
    // Also figure out the x and y offset values, which start 
    // with the values assigned to the view
    // and are adjusted based on the scale.
    float offsetX = getOffsetX (), offsetY = getOffsetY ();
    if (pScale != 1.0f) {
       matrix.postScale (pScale, pScale);
 
       float sx = (0.0f + pScale) * vw / 2;
       float sy = (0.0f + pScale) * vh / 2;
 
       offsetX += sx;
       offsetY+= sy;
 
    } else {
      offsetX += hvw;
      offsetY += hvh;
    }
 
    // The last translation moves the bitmap to where it has to be to have its top left point be
    // where it should be following the rotation and scaling.
    matrix.postTranslate (offsetX, offsetY);
 
    // Finally, draw the bitmap using the matrix as a guide.
    canvas.drawBitmap (pBitmap, matrix, null);
 
}

/**
 * Set the bitmap for the view, given the resource id of the drawable.
 * 
 * @param drawableId int
 * @return void
 */

public void setBitmapFromResource (int drawableId) {
    Resources res = getResources ();
    setBitmap (BitmapFactory.decodeResource (res, drawableId));
}

} // end class
