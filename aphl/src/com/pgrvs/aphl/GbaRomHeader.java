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

import java.nio.ByteBuffer;

/**
 * This class represents the header information for this ROM. Header information includes information such as 
 * the game code, title, and version.
 * 
 * @author Phillip Groves
 *
 */
public class GbaRomHeader implements Savable {

	private static final int GAME_TITLE_ADDRESS = 0xA0;
	private static final int GAME_TITLE_LENGTH = 12;
	private static final int GAME_CODE_ADDRESS = 0xAC;
	private static final int GAME_CODE_LENGTH = 4;
	private static final int GAME_VERSION_ADDRESS = 0xBC;
	
	private final ByteBuffer bytes;
	
	/** 12-character representation of game name */
	private String title = "";
	
	/** 4-character game code, where each character provides different information (e.g. language) */
	private String gameCode = "";
	
	/** Version for this ROM, which is especially useful because different versions require change */
	private String version;
	
	/**
	 * 
	 * @param bytes The bytes of this ROM
	 */
	public GbaRomHeader(ByteBuffer bytes) {
		for (int i = 0; i < GAME_TITLE_LENGTH; i++)
			this.title += (char) bytes.get(GAME_TITLE_ADDRESS + i);
		for (int i = 0; i < GAME_CODE_LENGTH; i++)
			this.gameCode += (char) bytes.get(GAME_CODE_ADDRESS + i);
		this.version = "1." + bytes.get(GAME_VERSION_ADDRESS);
		this.bytes = bytes;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public void save() {
		bytes.put(GAME_VERSION_ADDRESS, (byte) version.charAt(2));
		for (int i = 0; i < GAME_TITLE_LENGTH; i++)
			bytes.put(GAME_TITLE_ADDRESS + i, title.getBytes()[i]);
		for (int i = 0; i < GAME_CODE_LENGTH; i++)
			bytes.put(GAME_CODE_ADDRESS + i, gameCode.getBytes()[i]);
	}
}
