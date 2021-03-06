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

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EConstraintExplanation;

import java.util.Set;



/**
 * Sucelje koje definira ogranicenja
 * za pojedine parametre.
 * Primjerice, neki parametri mogu
 * biti ograniceni na vrijednosti
 * u nekom skupu ili na vrijednosti
 * izvan tog skupa.
 * 
 * @author Axel
 *
 */
public interface IParameterConstraint {
	/**
	 * Provjerava da li je vrijednost
	 * za odredeni parametar smislena. 
	 * 
	 * @return
	 * Vraca true ako je parametar moze
	 * poprimiti tu vrijednost, ili false
	 * ako ne moze.
	 */
	boolean checkValue(Object object);
	
	/**
	 * Metoda je slicna prethodnoj.
	 * Provjerava da li je vrijednost
	 * za odredeni parametar smislena,
	 * i istovremeno vraca kratku
	 * poruku koja odgovara na pitanje
	 * zasto vrijednost nije smislena.
	 * 
	 * @return
	 * Vraca null ako je parametar moze
	 * poprimiti tu vrijednost.
	 * Ako ne moze, NECE biti vracen null,
	 * vec String koji odgovara na pitanje
	 * zasto nije moguce pridruziti navedenu
	 * vrijednost.
	 */
	EConstraintExplanation getExplanation(Object object);
	
	/**
	 * Odreduje skup vrijednosti koje
	 * parametar moze poprimiti, ako
	 * je on konacan.
	 * Inace, vraca null!!! 
	 * 
	 * @return
	 * Vraca konacni skup vrijednosti
	 * koje parametar moze poprimiti.
	 * Ako taj skup nije konacan, vraca
	 * null!!!
	 * Ako je taj skup prazan, tad naprosto
	 * vraca prazan skup.
	 *  
	 */
	Set<Object> getPossibleValues();
	
	
	/**
	 * Postavlja dozvoljeni skup vrijednosti.
	 * Ako se postavi null, prihvacaju se sve
	 * vrijednosti.
	 * 
	 * @param possibleValues
	 */
	void setPossibleValues(Set<Object> possibleValues);
}











