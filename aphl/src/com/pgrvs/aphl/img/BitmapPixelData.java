/****************************************************************************\ 
 *                                                                           * 
 *                     ADVANCED POKéMON HACKING LIBRARY                      * 
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

import com.pgrvs.aphl.GbaRom;
import com.pgrvs.aphl.util.Lz77CompressionUtil;

/**
 * This class represents pixel data for an image. Pixel data is a map of how each pixel corresponds to a palette's 
 * colors by providing color indexes.
 * 
 * @author Phillip Groves
 *
 */
public class BitmapPixelData {
	
	/** Byte values containing pixel data for an image */
	private int[] values;
	
	/** The amount of bits per pixel */
	private final BitmapPixelDepth depth;
	
	/**
	 * 
	 * @param rom
	 * @param address Address of pixel data
	 * @param depth Bits per pixel
	 */
	public BitmapPixelData(GbaRom rom, int address, BitmapPixelDepth depth) {
		this.depth = depth;
		if (rom.getByte(address) == 0x10)
			this.values = Lz77CompressionUtil.decompress(rom, address);
	}
	
	/**
	 * 
	 * @param index Index of pixel
	 * @return Color palette Index of the pixel
	 */
	public int getPixel(int index) {
		int pixel = values[index / (8 / depth.value())];
		if ((index & 1) == 0)
			pixel &= 0x0F;
		else
			pixel = (pixel & 0xF0) >> depth.value();
		return pixel;
	}
	
	/**
	 * 
	 * @return All pixel data
	 */
	public int[] getValues() {
		return values;
	}

	/**
	 * 
	 * @return Bits used for each pixel
	 */
	public BitmapPixelDepth getDepth() {
		return depth;
	}
}