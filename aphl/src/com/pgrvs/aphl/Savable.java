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

/**
 * This interface is intended to be implemented by objects that hold game data and may need to save at a given 
 * time. By implementing this interface, it is possible for developers to more easily cache their changed objects 
 * and save in bulk (or alone).
 * 
 * @author Phillip Groves
 *
 */
public interface Savable {

	/**
	 * Saves queued values to the ROM
	 */
	public void save();
}
