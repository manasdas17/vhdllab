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
package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.CommandExecutorException;

import java.beans.PropertyChangeListener;
import java.util.List;



/**
 * Posrednik izmedu ISchemaCorea (modela)
 * i bilo kojeg view-a.
 * 
 * @author Axel
 *
 */
public interface ISchemaController {
	
	/**
	 * Salje ICommand objekt do registriranog modela.
	 * ICommand objekt radi sve nuzne promjene na modelu
	 * sukladno tome o kojoj se komandi radi i vraca 
	 * ICommandResponse koji sadrzi informaciju o uspjesnosti
	 * izvodenja komande.
	 * 
	 * @param command
	 */
	ICommandResponse send(ICommand command);
	
	/**
	 * Obavlja upit nad modelom i cacheira upit ako
	 * je to moguce. Ako je potpuno isti upit vec
	 * obavljen, a u meduvremenu nije bilo promjena
	 * nad modelom, vraca rezultate prethodno izvedenog
	 * i cacheiranog upita.
	 * 
	 * @param query
	 */
	IQueryResult send(IQuery query);
	
	/**
	 * Registrira core na controller. Ako se core ne registrira
	 * na controller, zahtjevi od view-ova nece biti proslijedeni
	 * nikome.
	 * 
	 * @param core
	 * Core kojem ce biti proslijedeni zahtjevi.
	 * Ako se proslijedi null, zahtjevi nece biti proslijedivani.
	 */
	void registerCore(ISchemaCore core);


	/**
	 * Dodaje novi listener. Pri bilo kakvoj
	 * promjeni u modelu sheme svi ce listeneri
	 * biti obavijesteni.
	 * 
	 * @param listener
	 * @param changeType
	 * Tip promjene za koji se listener aktivira.
	 * ANY_CHANGE za sve vrste promjena.
	 */
	void addListener(EPropertyChange changeType, PropertyChangeListener listener);
	
	
	/**
	 * Mice listener s popisa.
	 * 
	 * @param listener
	 */
	void removeListener(PropertyChangeListener listener);
	
	
	/**
	 * Read-only objekt.
	 * 
	 * @return
	 * Objekt koji je moguce iskljucivo citati.
	 * Nikakve promjene se ne smiju raditi izravno
	 * na ovom objektu.
	 */
	ISchemaInfo getSchemaInfo();
	
	
	/**
	 * Za odredivanje da li je moguc undo.
	 * 
	 * @return Ako stog prethodno izvedenih komandi nije prazna, vraca true, u
	 *         protivnom false.
	 * 
	 */
	boolean canUndo();

	/**
	 * Za odredivanje da li je moguc redo.
	 * 
	 * @return Slicno kao i prethodna metoda.
	 */
	boolean canRedo();

	/**
	 * Vraca listu naziva komandi koje su prethodno obavljene. Vraca praznu
	 * listu, ako takvih nema.
	 * 
	 * @return Jasno samo po sebi.
	 * 
	 */
	List<String> getUndoList();

	/**
	 * Vraca listu naziva komandi koje ce tek biti obavljene. Ako takvih nema,
	 * vraca praznu listu.
	 * 
	 * @return Jasno samo po sebi.
	 * 
	 */
	List<String> getRedoList();

	/**
	 * Obavlja komandu na vrhu stoga undo komandi, i ako je rezultat uspjesan,
	 * popne je i stavi na stog redo komandi. OPREZ: U principu se ne bi trebalo
	 * dogoditi da je rezultat izvodenja neuspjesan, no ako se to dogodi, brisu
	 * se oba stoga, i undo i redo stog.
	 * 
	 * @throws CommandExecutorException
	 * Ako je stog undo komandi prazan, ili se dogodila neka druga greska.
	 * Pritom odmah cisti oba stoga.
	 * 
	 * @return Objekt koji govori o uspjesnosti izvodenja undo komande.
	 */
	ICommandResponse undo() throws CommandExecutorException;

	/**
	 * Obavlja komandu s vrha redo stoga i stavlja je na undo stog.
	 * 
	 * @throws CommandExecutorException
	 * Slicno kao i prethodno, samo za redo stog.
	 * Pritom odmah cisti oba stoga.
	 * 
	 * @return Vidi prethodnu metodu.
	 */
	ICommandResponse redo() throws CommandExecutorException;
	
	/**
	 * Brise komande s undo i redo liste.
	 *
	 */
	void clearCommandCache();
	
	/**
	 * Brise cacheirane upite.
	 */
	void clearQueryCache();
	
	
}









