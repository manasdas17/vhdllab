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

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "project_history", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "user_id", "name", "insert_version", "update_version" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectHistory extends OwnedEntity {

    private static final long serialVersionUID = 7347617814219101311L;

    @NotNull
    @Embedded
    private History history;

    public ProjectHistory() {
        super();
    }

    public ProjectHistory(OwnedEntity entity, History history) {
        super(entity);
        setHistory(history);
        setId(null);
        setVersion(null);
    }

    public ProjectHistory(ProjectHistory project) {
        this(project, project.getHistory());
    }
    
    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .appendSuper(super.hashCode())
                    .append(history)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ProjectHistory))
            return false;
        ProjectHistory other = (ProjectHistory) obj;
        return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(history, other.history)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("history", history)
                    .toString();
    }

}
