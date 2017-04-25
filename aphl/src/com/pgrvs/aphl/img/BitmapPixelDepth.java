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
