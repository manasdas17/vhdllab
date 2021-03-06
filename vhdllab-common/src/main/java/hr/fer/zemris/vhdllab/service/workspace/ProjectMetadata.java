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
package hr.fer.zemris.vhdllab.service.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

public class ProjectMetadata implements Serializable {

    private static final long serialVersionUID = 6024599240022056727L;

    private Set<File> files;
    private Hierarchy hierarchy;

    @SuppressWarnings("unchecked")
    public ProjectMetadata(Project project) {
        this(new Hierarchy(project, Collections.EMPTY_LIST),
                new HashSet<File>());
    }

    public ProjectMetadata(Hierarchy hierarchy, Set<File> files) {
        Validate.notNull(files, "Files can't be null");
        setHierarchy(hierarchy);
        this.files = EntityUtils.cloneFiles(files);
    }

    public Project getProject() {
        return EntityUtils.lightweightClone(hierarchy.getProject());
    }

    public Set<File> getFiles() {
        return Collections.unmodifiableSet(files);
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Hierarchy hierarchy) {
        Validate.notNull(hierarchy, "Hierarchy can't be null");
        this.hierarchy = hierarchy;
    }

    public void addFile(File file) {
        Validate.notNull(file, "File can't be null");
        File fileToAdd = new File(file, hierarchy.getProject());
        files.remove(fileToAdd);
        files.add(fileToAdd);
    }

    public void removeFile(File file) {
        Validate.notNull(file, "File can't be null");
        files.remove(file);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append("files [");
        if (!files.isEmpty()) {
            sb.append("\n");
            for (File f : files) {
                sb.append(f).append("\n");
            }
        }
        sb.append("]\n").append(hierarchy);
        return sb.toString();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        Project project = getProject();
        for (File f : files) {
            f.setProject(project);
        }
        files = new HashSet<File>(files);
    }

}
