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
package hr.fer.zemris.vhdllab.dao.impl.support;

import java.sql.Types;

import org.hibernate.dialect.HSQLDialect;

/**
 * HSQL DB dialect whose string columns are case insensitive.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class CaseInsensitiveHSQLDialect extends HSQLDialect {

    public CaseInsensitiveHSQLDialect() {
        super();
        registerColumnType(Types.VARCHAR, "varchar_ignorecase($l)");
    }

}
