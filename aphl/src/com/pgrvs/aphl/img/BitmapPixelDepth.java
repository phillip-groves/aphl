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

/**
 * This simple enumerated type lists the different pixel depth options that are available in a 3rd generation 
 * Pokemon (or GBA in general) game.Each type also contains its bit value (e.g. BPP_4 -> 4). Each is named with 
 * BPP_VALUE, where BPP means "bits per pixel" and VALUE is the depth.
 * 
 * @author Phillip Groves
 *
 */
public enum BitmapPixelDepth {
	BPP_1( 1 ),
	BPP_2( 2 ),
	BPP_4( 4 ),
	BPP_8( 8 );
	
	/** Bits per pixel amount */
	private final int value;
	
	private BitmapPixelDepth(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return Bits per pixel
	 */
	public int value() {
		return value;
	}
}
