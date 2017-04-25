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

package com.pgrvs.aphl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This class represents the read-only memory (ROM) file associated with 3rd generation Pokemon games. These 
 * files contain all game data, and as such, we can change this file to modify the correlating game. </p>
 * 
 * <p>Methods included with this class give the ability to read and write data from file. All get() messages 
 * are retrieved from the file in real-time, and all put() methods write immediately. Keep this in mind and 
 * cache your changes if needed. It is recommended that you create an object for the data you are editing, then 
 * implement the {@link Savable} interface and write your changes when required.</p>
 * 
 * <p>Also included are the underlying header (title, code, version), and character set for the ROM.</p>
 * 
 * @author Phillip Groves
 *
 */
public class GbaRom {

	/** A buffer holding the bytes of this ROM in little endian order */
	private final ByteBuffer bytes;
	
	/** The character set for generation 3 Pokemon games, mapped byte-to-character */
	private final HashMap<Integer, String> characters;
	
	/** Contains the header information (title, game code, version) of this game */
	private final GbaRomHeader header;
	
	/**
	 * 
	 * @param path Path to ROM file
	 */
	public GbaRom(String path) {
		this (new File(path));
	}
	
	/**
	 * 
	 * @param romFile The file to read
	 */
	public GbaRom(File romFile) {
		this.bytes = loadBytes(romFile);
		this.characters = loadCharacterSet();
		this.header = new GbaRomHeader(bytes);
	}
	
	/**
	 * 
	 * @return This game's header info
	 */
	public GbaRomHeader getHeader() {
		return header;
	}
	
	/**
	 * <p>See {@link ByteBuffer#get(int)}</p>
	 * 
	 * <p>Note: Unlike the ByteBuffer get(int) method, this value is returned unsigned (0-255).<p/>
	 * 
	 * @param address The position to read
	 * @return The retrieved byte (8-bit) value
	 */
	public int getByte(int address) {
		bytes.position(address);
		return getByte();
	}
	
	/**
	 * <p>See {@link ByteBuffer#get()}</p>
	 * 
	 * <p>Note: Unlike the ByteBuffer get(int) method, this value is returned unsigned (0-255).<p/>
	 * 
	 * @return The retrieved byte (8-bit) value
	 */
	public int getByte() {
		return bytes.get() & 0xFF; // & 0xFF will unsign the 8-bit value
	}
	
	/**
	 * Bulk get method. Reads the bytes at the given address and length, and then returns the result.
	 * 
	 * @param address The position to read
	 * @param length The amount of bytes to read
	 * @return The retrieved byte (8-bit) values
	 */
	public int[] getBytes(int address, int length) {
		int[] result = new int[length];
		for (int i = 0; i < length; i++)
			result[i] = getByte(address + i);
		return result;
	}
	
	/**
	 * <p>See {@link ByteBuffer#getShort(int)}</p>
	 * 
	 * @param address The position to read
	 * @return The little endian short (16-bit) value
	 */
	public int getShort(int address) {
		bytes.position(address);
		return getShort();
	}
	
	/**
	 * <p>See {@link ByteBuffer#getShort()}</p>
	 * 
	 * @return The little endian short (16-bit) value
	 */
	public int getShort() {
		return bytes.getShort() & 0xFFFF; // & 0xFFFF will unsign the 16-bit value
	}
	
	/**
	 * Bulk get method. Reads the 16-bit values at the given address and length, and then returns the result.
	 * 
	 * @param address The position to read
	 * @param length The amount of shorts to read
	 * @return The retrieved short (16-bit) values
	 */
	public int[] getShorts(int address, int length) {
		int[] result = new int[length];
		for (int i = 0; i < length; i++)
			result[i] = getShort(address + (i * 2));
		return result;
	}
	
	/**
	 * <p>See {@link ByteBuffer#getInt(int)}</p>
	 * 
	 * @param address The position to read
	 * @return The little endian integer (32-bit) value
	 */
	public int getInt(int address) {
		bytes.position(address);
		return getInt();
	}
	
	/**
	 * <p>See {@link ByteBuffer#getShort()}</p>
	 * 
	 * @return The little endian short (16-bit) value
	 */
	public int getInt() {
		return bytes.getInt();
	}
	
	/**
	 * Bulk get method. Reads the 32-bit values at the given address and length, and then returns the result.
	 * 
	 * @param address The position to read
	 * @param length The amount of integers to read
	 * @return The retrieved short (32-bit) values
	 */
	public int[] getInts(int address, int length) {
		int[] result = new int[length];
		for (int i = 0; i < length; i++)
			result[i] = getInt(address + (i * 4));
		return result;
	}
	
	/**
	 * <p>See {@link ByteBuffer#getInt(int)}</p>
	 * 
	 * <p>Note: Unlike a standard integer, "pointer" values are only comprised of the trailing 3 bytes for our 
	 * purposes. The leading byte of the integer contains a number (08 or 09), but this method removes it.</p>
	 * 
	 * @param address The position to read
	 * @return The little endian pointer value
	 */
	public int getPointer(int address) {
		bytes.position(address);
		return getPointer();
	}
	
	/**
	 * <p>See {@link ByteBuffer#getInt()}</p>
	 * 
	 * <p>Note: Unlike a standard integer, "pointer" values are only comprised of the trailing 3 bytes for our 
	 * purposes. The leading byte of the integer contains a number (08 or 09), but this method removes it.</p>
	 * 
	 * @param address The position to read
	 * @return The little endian pointer value
	 */
	public int getPointer() {
		return bytes.getInt() & 0x1FFFFFF; // & 0x1FFFFFF will remove the leading 8 bits from an integer
	}
	
	/**
	 * <p>Bulk get method. Reads the pointer values at the given address and length, and then returns the result.</p>
	 * 
	 * <p>Note: Unlike a standard integer, "pointer" values are only comprised of the trailing 3 bytes for our 
	 * purposes. The leading byte of the integer contains a number (08 or 09), but this method removes it.</p>
	 * @param address The position to read
	 * @param length The amount of integers to read
	 * @return The retrieved short (32-bit) values
	 */
	public int[] getPointers(int address, int length) {
		int[] result = new int[length];
		for (int i = 0; i < length; i++)
			result[i] = getPointer(address + (i * 4));
		return result;
	}
	
	/**
	 * <p>Reads text at the given address and length, and then returns the result. Strings read through this 
	 * method are automatically converted to "Poketext", which uses a special character set for in-game strings.</p>
	 * 
	 * @param address The position to read
	 * @param length The amount of characters to read
	 * @return The retrieved text
	 */
	public String getString(int address, int length) {
		bytes.position(address);
		return getString(length);
	}
	
	/**
	 *  <p>Reads text at the current address and given length, and then returns the result. Strings read through
	 *  this method are automatically converted to "Poketext", which uses a special character set for 
	 *  in-game strings.</p>
	 * 
	 * @param length The amount of characters to read
	 * @return The retrieved text
	 */
	public String getString(int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++)
			builder.append(characters.get(getByte()));
		return builder.toString();
	}
	
	/**
	 * <p>Reads text at the given address and continues to read until 0xFF (ending character) is reached, 
	 * 	then returns the result. Strings read through this method are automatically converted to "Poketext", 
	 *  which uses a special character set for in-game strings.</p>
	 * 
	 * @param address The address to read
	 * @return The retrieved text
	 */
	public String getStringUtilEnd(int address) {
		bytes.position(address);
		return getStringUtilEnd();
	}
	
	/**
	 * <p>Reads text at the current address and continues to read until 0xFF (ending character) is reached, 
	 * 	then returns the result. Strings read through this method are automatically converted to "Poketext", 
	 *  which uses a special character set for in-game strings.</p>
	 * 
	 * @param address The address to read
	 * @return The retrieved text
	 */
	public String getStringUtilEnd() {
		StringBuilder builder = new StringBuilder();
		String current;
		
		while (!(current = characters.get(getByte())).equalsIgnoreCase("|end|"))
			builder.append(current);
		
		return builder.toString().trim();
	}
	
	/**
	 * Retrieves a list of text strings that are seperated by 0xFF (ending character). Underlying this method is 
	 * a loop containing {@link #getStringUtilEnd()}.
	 * 
	 * @param address The address to read
	 * @param length The amount of strings to read
	 * @return The list of retrieved strings
	 */
	public String[] getStringList(int address, int length) {
		String[] list = new String[length];
		
		bytes.position(address);
		for (int i = 0; i < list.length; i++)
			list[i] = getStringUtilEnd();
		
		return list;
	}
	
	/**
	 * See {@link ByteBuffer#put(int, int)}
	 * 
	 * @param address The address to write
	 * @param value The 8-bit value to be written
	 */
	public void putByte(int address, int value) {
		bytes.position(address);
		putByte(value);
	}
	
	/**
	 * See {@link ByteBuffer#put(int)}
	 * 
	 * @param value The 8-bit value to be written
	 */
	public void putByte(int value) {
		bytes.put((byte) value);
	}
	
	/**
	 * Bulk put method. Writes bytes equal to that of the given array at the given address.
	 * 
	 * @param address The position to write
	 * @param values The 8-bit values to write
	 */
	public void putBytes(int address, int[] values) {
		for (int i = 0; i < values.length; i++)
			putByte(address + i, values[i]);
	}
	
	/**
	 * See {@link ByteBuffer#putShort(int, int)}
	 * 
	 * @param address The address to write
	 * @param value The 16-bit value to be written
	 */
	public void putShort(int address, int value) {
		bytes.position(address);
		putShort(value);
	}
	
	/**
	 * See {@link ByteBuffer#putShort(int)}
	 * 
	 * @param value The 16-bit value to be written
	 */
	public void putShort(int value) {
		bytes.putShort((short) value);
	}
	
	/**
	 * Bulk put method. Writes shorts equal to that of the given array at the given address.
	 * 
	 * @param address The position to write
	 * @param values The 16-bit values to write
	 */
	public void putShorts(int address, int[] values) {
		for (int i = 0; i < values.length; i++)
			putByte(address + (i * 2), values[i]);
	}
	
	/**
	 * See {@link ByteBuffer#putInt(int, int)}
	 * 
	 * @param address The address to write
	 * @param value The 32-bit value to be written
	 */
	public void putInt(int address, int value) {
		bytes.position(address);
		putInt(value);
	}
	
	/**
	 * See {@link ByteBuffer#putInt(int)}
	 * 
	 * @param value The 16-bit value to be written
	 */
	public void putInt(int value) {
		bytes.putInt((short) value);
	}
	
	/**
	 * Bulk put method. Writes integers equal to that of the given array at the given address.
	 * 
	 * @param address The position to write
	 * @param values The 32-bit values to write
	 */
	public void putInts(int address, int[] values) {
		for (int i = 0; i < values.length; i++)
			putByte(address + (i * 4), values[i]);
	}
	
	/**
	 * Writes the given string of text at the given location. This text is automatically converted to "Poketext", 
	 * which uses a special character set for in-game strings.
	 * 
	 * @param address The address to write
	 * @param text The text to write
	 */
	public void putString(int address, String text) {
		for (int i = 0; i < text.length(); i++) {
			for (Map.Entry<Integer, String> entry : characters.entrySet()) {
				if (entry.getValue().equals(text.substring(i, i + 1))) {
					System.out.println("Got here");
					putByte(address + i, entry.getKey());
				}
			}
		}
	}
	
	/**
	 * Initializes the buffer that is used to read and write bytes from file
	 * 
	 * @param romFile The file to load
	 * @return The bytes of the file in little endian order
	 */
	private ByteBuffer loadBytes(File romFile) {
		ByteBuffer buffer = null;
		
		try {
			RandomAccessFile reader = new RandomAccessFile(romFile, "rw");
			buffer = reader.getChannel().map(MapMode.READ_WRITE, 0, reader.length()).order(ByteOrder.LITTLE_ENDIAN);
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (buffer == null)
			throw new IllegalStateException("Underlying buffer for loaded GBA ROM is NULL!");
		
		return buffer;
	}
	
	/**
	 * Initializes the character set used for "Poketext", which can be found at /src/resources/character-set.ini
	 * 
	 * @return The loaded set
	 */
	private HashMap<Integer, String> loadCharacterSet() {
		HashMap<Integer, String> set = new HashMap<Integer, String>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/resources/character-set.ini"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("=")) {
					String[] data = line.split("=", 2);
					set.put(Integer.decode("0x" + data[0]), data[1]);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}
}
