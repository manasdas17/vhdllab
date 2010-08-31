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
package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class AutomatonInfoForm extends AbstractMultiValidationForm {

    public AutomatonInfoForm() {
        super(new AutomatonInfo(), "newAutomatonInfo");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        focusOnBeginning(builder
                .add(combobox("automatonType", "Moore", "Mealy"))[1]);
        builder.row();
        builder.add(combobox("resetValue", "0", "1"));
        builder.row();
        builder.add(combobox("clockValue", "falling_edge", "rising_edge", "0",
                "1"));
    }

    private Binding combobox(String formProperty, String... selectableItems) {
        SwingBindingFactory factory = (SwingBindingFactory) getBindingFactory();
        return factory.createBoundComboBox(formProperty, selectableItems);
    }

}
