/**
 * 
 */
package basic_objects;

import idiogram.Idiogram;

import java.util.Collection;

import characterization.ChromosomeBand;

/**
 * @author Robert
 *
 */
public class Chromosome extends Idiogram {

	private int width;

	public Chromosome(int width) {
		super();
		this.width = width;
	}

	public Chromosome(Collection<ChromosomeBand> c, int width) {
		super(c);
		this.width = width;
		
	}

	public Chromosome(Idiogram i) {
		super(i);
		this.width = 0;
	}
	
	
}
