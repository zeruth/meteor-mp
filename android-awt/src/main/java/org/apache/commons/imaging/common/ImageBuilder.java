/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Development notes:
 * This class was introduced to the Apache Commons Imaging library in
 * order to improve performance in building images.  The setRGB method
 * provided by this class represents a substantial improvement in speed
 * compared to that of the BufferedImage class that was originally used
 * in Apache Sanselan.
 * This increase is attained because ImageBuilder is a highly specialized
 * class that does not need to perform the general-purpose logic required
 * for BufferedImage.  If you need to modify this class to add new
 * image formats or functionality, keep in mind that some of its methods
 * are invoked literally millions of times when building an image.
 * Since even the introduction of something as small as a single conditional
 * inside of setRGB could result in a noticeable increase in the
 * time to read a file, changes should be made with care.
 * During development, I experimented with inlining the setRGB logic
 * in some of the code that uses it. This approach did not significantly
 * improve performance, leading me to speculate that the Java JIT compiler
 * might have inlined the method at run time.  Further investigation
 * is required.
 */
package org.apache.commons.imaging.common;

import java.awt.image.*;
import java.util.Properties;

/**
 * A utility class primary intended for storing data obtained by reading
 * image files.
 */
public class ImageBuilder {
    private final int[] data;
    private final int width;
    private final int height;
    private final boolean hasAlpha;

    /**
     * Construct an ImageBuilder instance
     * @param width the width of the image to be built
     * @param height the height of the image to be built
     * @param hasAlpha indicates whether the image has an alpha channel
     * (the selection of alpha channel does not change the memory
     * requirements for the ImageBuilder or resulting BufferedImage.
     */
    public ImageBuilder(final int width, final int height, final boolean hasAlpha) {
        if (width <= 0) {
            throw new RasterFormatException("zero or negative width value");
        }
        if (height <= 0) {
            throw new RasterFormatException("zero or negative height value");
        }

        data = new int[width * height];
        this.width = width;
        this.height = height;
        this.hasAlpha = hasAlpha;
    }

    /**
     * Get the width of the ImageBuilder pixel field
     * @return a positive integer
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the ImageBuilder pixel field
     * @return a positive integer
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the RGB or ARGB value for the pixel at the position (x,y)
     * within the image builder pixel field. For performance reasons
     * no bounds checking is applied.
     * @param x the X coordinate of the pixel to be read
     * @param y the Y coordinate of the pixel to be read
     * @return the RGB or ARGB pixel value
     */
    public int getRGB(final int x, final int y) {
        final int rowOffset = y * width;
        return data[rowOffset + x];
    }

    /**
     * Set the RGB or ARGB value for the pixel at position (x,y)
     * within the image builder pixel field. For performance reasons,
     * no bounds checking is applied.
     * @param x the X coordinate of the pixel to be set
     * @param y the Y coordinate of the pixel to be set
     * @param argb the RGB or ARGB value to be stored.
     */
    public void setRGB(final int x, final int y, final int argb) {
        final int rowOffset = y * width;
        data[rowOffset + x] = argb;
    }

    /**
     * Create a BufferedImage using the data stored in the ImageBuilder.
     * @return a valid BufferedImage.
     */
    public BufferedImage getBufferedImage() {
        return makeBufferedImage(data, width, height, hasAlpha);
    }

    /**
     * Gets a subimage from the ImageBuilder using the specified parameters.
     * If the parameters specify a rectangular region that is not entirely
     * contained within the bounds defined by the ImageBuilder, this method will
     * throw a RasterFormatException.  This runtime-exception behavior
     * is consistent with the behavior of the getSubimage method
     * provided by BufferdImage.
     * @param x the X coordinate of the upper-left corner of the 
     *          specified rectangular region
     * @param y the Y coordinate of the upper-left corner of the 
     *          specified rectangular region
     * @param w the width of the specified rectangular region
     * @param h the height of the specified rectangular region
     * @return a BufferedImage that constructed from the deta within the
     *           specified rectangular region
     * @throws RasterFormatException f the specified area is not contained 
     *         within this ImageBuilder
     */
    public BufferedImage getSubimage(final int x, final int y, final int w, final int h) {
        if (w <= 0) {
            throw new RasterFormatException("negative or zero subimage width");
        }
        if (h <= 0) {
            throw new RasterFormatException("negative or zero subimage height");
        }
        if (x < 0 || x >= width) {
            throw new RasterFormatException("subimage x is outside raster");
        }
        if (x + w > width) {
            throw new RasterFormatException(
                    "subimage (x+width) is outside raster");
        }
        if (y < 0 || y >= height) {
            throw new RasterFormatException("subimage y is outside raster");
        }
        if (y + h > height) {
            throw new RasterFormatException(
                    "subimage (y+height) is outside raster");
        }


        // Transcribe the data to an output image array  
        final int[] argb = new int[w * h];
        int k = 0;
        for (int iRow = 0; iRow < h; iRow++) {
            final int dIndex = (iRow + y) * width + x;
            System.arraycopy(this.data, dIndex, argb, k, w);
            k += w;

        }

        return makeBufferedImage(argb, w, h, hasAlpha);

    }

    private BufferedImage makeBufferedImage(
            final int[] argb, final int w, final int h, final boolean useAlpha) {
        ColorModel colorModel;
        WritableRaster raster;
        final DataBufferInt buffer = new DataBufferInt(argb, w * h);
        if (useAlpha) {
            colorModel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00,
                    0x000000ff, 0xff000000);
            raster = Raster.createPackedRaster(buffer, w, h,
                    w, new int[]{0x00ff0000, 0x0000ff00, 0x000000ff,
                            0xff000000}, null);
        } else {
            colorModel = new DirectColorModel(24, 0x00ff0000, 0x0000ff00,
                    0x000000ff);
            raster = Raster.createPackedRaster(buffer, w, h,
                    w, new int[]{0x00ff0000, 0x0000ff00, 0x000000ff},
                    null);
        }
        return new BufferedImage(colorModel, raster,
                colorModel.isAlphaPremultiplied(), new Properties());
    }
}
