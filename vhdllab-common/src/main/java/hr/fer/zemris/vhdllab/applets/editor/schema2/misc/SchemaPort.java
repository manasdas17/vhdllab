/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.SchemaPortWrapper;




/**
 * Port neke komponente.
 * 
 * @author Axel
 *
 */
public final class SchemaPort {
	
	public static final int NO_PORT = Integer.MIN_VALUE;
	
	private XYLocation loc;
	private Caseless name;
	private Caseless mappedto;
	private int portindex;
	
	public SchemaPort() {
		loc = new XYLocation();
		name = new Caseless("");
		mappedto = null;
		portindex = NO_PORT;
	}
	
	public SchemaPort(int xoffset, int yoffset) {
		loc = new XYLocation(xoffset, yoffset);
		name = new Caseless("");
		mappedto = null;
		portindex = NO_PORT;
	}
	
	public SchemaPort(int xoffset, int yoffset, Caseless portname) {
		loc = new XYLocation(xoffset, yoffset);
		name = portname;
		mappedto = null;
		portindex = NO_PORT;
	}
	
	public SchemaPort(SchemaPort other) {
		this.loc = new XYLocation(other.loc);
		this.name = other.name;
		this.mappedto = other.mappedto;
		this.portindex = other.portindex;
	}
	
	/**
	 * Za potrebe deserijalizacije iskljucivo.
	 * 
	 * @param spwrapper
	 */
	public SchemaPort(SchemaPortWrapper spwrapper) {
		this.loc = new XYLocation(spwrapper.getXOffset(), spwrapper.getYOffset());
		this.name = new Caseless(spwrapper.getName());
		this.mappedto = new Caseless(spwrapper.getMapping());
		this.portindex = spwrapper.getPortindex();
	}
	
	
	/**
	 * Vraca offset porta od gornjeg
	 * lijevog kuta komponente.
	 * Ne vraca kopiju, vec referencu na pravu vrijednost!
	 * 
	 */
	public final XYLocation getOffset() {
		return loc;
	}
	
	
	/**
	 * Postavlja offset porta.
	 * 
	 * @param offset
	 * Ako je null, bit ce ocuvana stara vrijednost.
	 */
	public final void setOffset(XYLocation offset) {
		if (offset != null) loc = offset;
	}
	
	public final void setXOffset(Integer x) {
		loc.x = x;
	}
	
	public final void setYOffset(Integer y) {
		loc.y = y;
	}
	
	public final void setXOffset(String xStr) {
		loc.x = Integer.parseInt(xStr);
	}
	
	public final void setYOffset(String yStr) {
		loc.y = Integer.parseInt(yStr);
	}

	/**
	 * Dobavlja ime porta.
	 * 
	 */
	public final Caseless getName() {
		return name;
	}

	/**
	 * Postavlja ime porta.
	 * 
	 * @param name
	 */
	public final void setName(Caseless name) {
		this.name = name;
	}
	
	/**
	 * Vraca zicu na koju je spojen port.
	 * 
	 * @return
	 * Null ili prazan string ako nije spojen ni na sto,
	 * ime signala inace.
	 * @see Caseless#isNullOrEmpty(Caseless)
	 */
	public final Caseless getMapping() {
		return mappedto;
	}
	
	/**
	 * Spaja port na signal (zicu) danog imena.
	 * 
	 * @param signalToMapTo
	 * Ime signala (zice). Null odspaja
	 * port od signala ako je na njega
	 * bio spojen.
	 */
	public final void setMapping(Caseless signalToMapTo) {
		mappedto = signalToMapTo;
	}
	
	/**
	 * Spaja port na signal (zicu) danog imena.
	 * 
	 * @param signalToMapTo
	 * Ime signala (zice). Null, string od samih praznina
	 * ili prazan string odspajaju.
	 */
	public final void setStringMapping(String signalToMapTo) {
		if (signalToMapTo.trim().equals("")) this.mappedto = null;
		else this.mappedto = new Caseless(signalToMapTo);
	}
	
	public final void setStringName(String name) {
		if (name == null) this.name = new Caseless("");
		else this.name = new Caseless(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object arg0) {
		if (arg0 == null) return false;
		if (!(arg0 instanceof SchemaPort)) return false;
		SchemaPort port = (SchemaPort)arg0;
		return (port.loc.equals(this.loc) && port.name.equals(this.name));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return 119 * loc.hashCode() + name.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return super.toString();
	}

	public final void setPortindex(int portindex) {
		this.portindex = portindex;
	}

	public final int getPortindex() {
		return portindex;
	}
	
	
	
	
	
}














