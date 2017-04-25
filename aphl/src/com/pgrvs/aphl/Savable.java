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
