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

import com.pgrvs.aphl.GbaRom;
import com.pgrvs.aphl.Savable;
import com.pgrvs.aphl.util.Lz77CompressionUtil;

/**
 * This class represents a palette of colors within the ROM. Each color palette can contain 16 or 512 colors, and 
 * contains 16-bit integer values. Methods for getting and setting palette data are available, and changes can be 
 * saved as well.
 * 
 * @author Phillip Groves
 *
 */
public class BitmapPaletteData implements Savable {
	
	/** Locally held for saving */
	private final GbaRom rom;
	
	/** Address of this palette data */
	private final int address;
	
	/** The 16-bit values backing this palette */
	private int[] values;
	
	/** Color objects held for ease with other Java components (e.g. Swing) */
	private Color[] colors;
	
	/**
	 * 
	 * @param rom
	 * @param address Address to read
	 * @param size Amount of colors
	 */
	public BitmapPaletteData(GbaRom rom, int address, int size) {
		this.address = address;
		this.rom = rom;
		
		if (rom.getByte(address) == 0x10)
			this.values = Lz77CompressionUtil.decompress(rom, address);
			
		this.colors = new Color[values.length / 2];
		
		for (int i = 0; i < values.length; i++) {
			int value = values[i] | (values[++i] << 8);
			
			int red = ( value & 0x1F ) << 3;
			int green = ( value & 0x3E0 ) >> 2;
			int blue = ( value & 0x7C00 ) >> 7;
			
			colors[i / 2] = new Color(red, green, blue);
		}
	}
	
	/**
	 * 
	 * @param index The index of the color
	 * @return The color at the given index
	 */
	public int getValue(int index) {
		return values[index];
	}
	
	/**
	 * 
	 * @param index The index of the color to replace
	 * @param value The value of the new color
	 */
	public void setValue(int index, int value) {
		values[index] = value;
	}
	
	/**
	 * 
	 * @return All color values
	 */
	public int[] getValues() {
		return values;
	}
	
	/**
	 * 
	 * @param index The index of the color
	 * @return The color at the given index
	 */
	public Color getColor(int index) {
		return colors[index];
	}
	
	/**
	 * 
	 * @return All colors on this palette
	 */
	public Color[] getColors() {
		return colors;
	}
	
	/**
	 * 
	 * @return {@link #address}
	 */
	public int getAddress() {
		return address;
	}
	
	@Override
	public void save() {
		rom.putShorts(address, values);
	}
}
