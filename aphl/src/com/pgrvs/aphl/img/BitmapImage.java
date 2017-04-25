/****************************************************************************\ 
 *                                                                           * 
 *                     ADVANCED POKÈMON HACKING LIBRARY                      * 
 *                                                                           * 
 *     A Java library for helping developers modify Pokemon game data        * 
 *                                                                           * 
 *                Copyright (C) 2017  Phillip Groves                         * 
 *                                                                           * 
 * This program is free software; you can redistribute it and/or modify it   * 
 * under the terms of the GNU General Public License as published by the     * 
 * Free Software Foundation; either version 2 of the License, or (at your    * 
 * option) any later version.                                                * 
 *                                                                           * 
 * This program is distributed in the hope that it will be useful, but       * 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANT-      * 
 * ABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the LICENSE file for    * 
 * more details.                                                             * 
 *                                                                           * 
 * You should have received a copy of the GNU General Public License along   * 
 * with this program; if not, write to the Free Software Foundation, Inc.,   * 
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.             * 
 /****************************************************************************/

package com.pgrvs.aphl.img;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an image within the ROM. Each image is built on {@link BitmapPixelData}
 * and {@link BitmapPaletteData}. For ease with other Java components (e.g. Swing), and for logical reasons, 
 * this class extends {@link BufferedImage}.
 * 
 * @author Phillip Groves
 *
 */
public class BitmapImage extends BufferedImage {
	
	/** Each bitmap image is comprised of 8x8 tiles */
	private static final int TILE_SIZE = 8;
	
	/** The map allows for faster building and maintaining of the image */
	private Map<Integer, BufferedImage> tiles = new HashMap<Integer, BufferedImage>();
	
	/**
	 * 
	 * @param pixels Pixel data
	 * @param palette Color data
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public BitmapImage(BitmapPixelData pixels, BitmapPaletteData palette, int width, int height) {
		super (width, height == 0 ? getHeight(pixels, width) : height, BufferedImage.TYPE_INT_ARGB);
		
		if (width % TILE_SIZE != 0 || height % TILE_SIZE != 0)
			throw new IllegalStateException( "Bitmap image width and height must be divisible by 8!" );
		
		int index = 0;
		// loop through rows of tiles
		for (int yTile = 0; yTile < (super.getHeight() / TILE_SIZE); yTile++) {
			// loop through columns of tiles
			for (int xTile = 0; xTile < (super.getWidth() / TILE_SIZE); xTile++) {
				// loop through rows of pixels inside tile
				for (int yPixel = 0; yPixel < TILE_SIZE; yPixel++) {
					// loop through columns of pixels inside tile
					for (int xPixel = 0; xPixel < TILE_SIZE; xPixel++) {
						int colorIndex = pixels.getPixel(index);
						Color pixel = palette.getColor( colorIndex );
				
						getRaster().setPixel(xPixel + (xTile * TILE_SIZE), yPixel + (yTile * TILE_SIZE), 
								new int[] { pixel.getRed(), pixel.getGreen(), pixel.getBlue(),
										colorIndex == 0 ? 0 : 255 });
						index++;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param pixels Pixel data
	 * @param palette Color data
	 * @param width Width in pixels
	 */
	public BitmapImage(BitmapPixelData pixels, BitmapPaletteData palette, int width) {
		this (pixels, palette, width, 0);
	}
	
	/**
	 * Retrieves an 8x8 tile from this image at the given tile id (usually given by game, e.g. in tilesets)
	 * 
	 * @param id Index of tile
	 * @param xFlip Whether to flip on the horizontal axis
	 * @param yFlip Whether to flip on the vertical axis
	 * @return 8x8 tile subimage of this image
	 */
	public BufferedImage getTile(int id, boolean xFlip, boolean yFlip) {
		BufferedImage tile = getTile(id);
		if (xFlip)
			tile = flipTileX(tile);
		if (yFlip)
			tile = flipTileY(tile);
		return tile;
	}
	
	/**
	 * Retrieves an 8x8 tile from this image at the given tile id (usually given by game, e.g. in tilesets)
	 * 
	 * @param id Index of tile
	 * @return 8x8 tile subimage of this image
	 */
	private BufferedImage getTile(int id) {
		if (!tiles.containsKey( id)) {
			int x = (id % ( super.getWidth() / TILE_SIZE )) * TILE_SIZE;
			int y = (id / (super.getWidth() / TILE_SIZE )) * TILE_SIZE;
			tiles.put(id, super.getSubimage(x, y, TILE_SIZE, TILE_SIZE));
		}
		return tiles.get( id );
	}
	
	/**
	 * Flips an 8x8 tile on the horizontal axis.
	 * 
	 * @param tile Tile to flip
	 * @return Flipped tile
	 */
	private BufferedImage flipTileX(BufferedImage tile) {
		BufferedImage image = new BufferedImage(TILE_SIZE, TILE_SIZE, tile.getType());
		Graphics2D graphics = image.createGraphics();
		graphics.drawImage(tile, 0, 0, TILE_SIZE, TILE_SIZE, TILE_SIZE, 0, 0, TILE_SIZE, null);
		graphics.dispose();
		return image;
	}
	
	/**
	 * Flips an 8x8 tile on the vertical axis.
	 * 
	 * @param tile Tile to flip
	 * @return Flipped tile
	 */
	private BufferedImage flipTileY( BufferedImage tile ) {
		BufferedImage image = new BufferedImage(TILE_SIZE, TILE_SIZE, tile.getType());
		Graphics2D graphics = image.createGraphics();
		graphics.drawImage(tile, 0, 0, TILE_SIZE, TILE_SIZE, 0, TILE_SIZE, TILE_SIZE, 0, null);
		graphics.dispose();
		return image;
	}
	
	/**
	 * Calculates the height of this image based on the length of pixel data, pixel depth, and width.
	 * 
	 * @param pixels Pixel data
	 * @param width Width in pixels
	 * @return Height in pixels
	 */
	private static int getHeight( BitmapPixelData pixels, int width ) {
		return (pixels.getValues().length / width) * (8 / pixels.getDepth().value());
	}
}