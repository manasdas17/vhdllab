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
package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME_2;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME_UPPERCASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class NamedEntityTest extends ValueObjectTestSupport {

    private NamedEntity entity;

    @Before
    public void initEntity() throws Exception {
        entity = new NamedEntityStub();
    }

    @Test
    public void basics() {
        NamedEntity another = new NamedEntity();
        assertNull("name is set.", another.getName());

        another.setName(NAME);
        assertNotNull("name is null.", another.getName());
        another.setName(null);
        assertNull("name not cleared.", another.getName());
    }

    @Test
    public void constructorName() {
        NamedEntity another = new NamedEntity(NAME);
        assertEquals("name not same.", NAME, another.getName());
    }

    @Test
    public void copyConstructor() {
        NamedEntity another = new NamedEntity(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        NamedEntity another = new NamedEntity(entity);
        assertEqualsAndHashCode(entity, another);

        another.setName(NAME_2);
        assertNotEqualsAndHashCode(entity, another);

        another.setName(NAME_UPPERCASE);
        assertEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
