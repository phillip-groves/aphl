package com.pgrvs.aphl.util;

import com.pgrvs.aphl.GbaRom;

/**
 * This class is responsible for providing utility methods for Lz77 compression, which is used throughout GBA 
 * games for palettes, images, and other purposes. All methods within this class are static for easy access.
 * 
 * @author Phillip Groves
 *
 */
public class Lz77CompressionUtil {
	
	private static final int LZ77_OPCODE = 0x10;
	
	/**
	 * <p>Decompresses Lz77 data at the given address, and then returns the result.</p>
	 * <p>This algorithm was originally developed by CUE @ dsdcmp, but it has been optimized and edited 
	 * for our purposes. </p>
	 * 
	 * @param rom 
	 * @param address Address to read
	 * @return Decompressed data
	 */
	public static int[] decompress(GbaRom rom, int address) {
		if (rom.getByte(address) != LZ77_OPCODE)
			throw new IllegalStateException("Invalid Lz77 compression opcode.");
		
		int[] data = new int[getDecompLength(rom)];
		int destination, flags, offset, length, value, position = 0;
		boolean flagged;
		
		while (position < data.length) {
			flags = rom.getByte();
			for (int i = 0; i < 8; i++) {
				flagged = (flags & ( 0x80 >> i) ) > 0;
				if ( flagged ) {
					value = rom.getByte();
					length = (value >> 4) + 3;
					offset = ((value & 0x0F) << 8) | rom.getByte();
					destination = position;
					
					if (offset > position)
						throw new IllegalStateException("Cannot go back more than already written.");
					for (int j = 0; j < length; j++)
						data[position++] = data[destination - offset - 1 + j];
				} else {
					value = rom.getByte();
					if (position < data.length)
						data[position++] = value;
					else if (value == 0)
						break;
				}
				if (position > data.length)
					break;
			}
		}
		return data;
	}
	
	/**
	 * Calculates the length of compressed data
	 * 
	 * @param rom 
	 * @return Amount to read
	 */
	private static int getDecompLength(GbaRom rom) {
		int length = 0;
		for (int i = 0; i < 3; i++)
			length |= (rom.getByte() << ( i * 8));
		if (length == 0)
			length = rom.getInt();
		return length;
	}
}