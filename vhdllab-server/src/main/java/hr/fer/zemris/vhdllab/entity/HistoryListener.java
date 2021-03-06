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

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public class HistoryListener {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(HistoryListener.class);
    
    private static EntityManagerFactory entityManagerFactory;

    public static void setEntityManagerFactory(
            EntityManagerFactory entityManagerFactory) {
        HistoryListener.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager newEntityManager() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        return em;
    }

    private void closeEntityManager(EntityManager em) {
        em.getTransaction().commit();
        em.close();
    }

    @PreRemove
    public void preFileRemove(Object o) {
        if(o instanceof File) {
            File f = (File) o;
            Project project = f.getProject();
            if(project != null) {
                /*
                 * Project is being removed which will, by cascade, delete
                 * files. But Project#removeFile will not be called which means
                 * that post remove project reference will not be set so we do
                 * it manually (because post remove event depends on it)!
                 */
                f.setPostRemoveProjectReference(project);
            }
        }
    }

    @PostPersist
    public void createHistory(Object o) {
        try {
            EntityManager em = newEntityManager();

            Object history;
            if (o instanceof Project) {
                Project project = (Project) o;
                ProjectHistory projectHistory;
                try {
                    projectHistory = getLastProjectHistory(em, project);
                } catch (NoResultException e) {
                    projectHistory = null;
                }
                History h;
                if(projectHistory == null) {
                    h = new History();
                } else {
                    h = new History(projectHistory.getHistory().getInsertVersion()+1, 0);
                }
                history = new ProjectHistory(project, h);
            } else if(o instanceof File) {
                File f = (File) o;
                ProjectHistory projectHistory = getLastProjectHistory(em, f.getProject());
                FileHistory fileHistory;
                try {
                    fileHistory = getLastFileHistory(em, projectHistory.getId(), f.getName());
                } catch (Exception e) {
                    fileHistory = null;
                }
                History h;
                if(fileHistory == null) {
                    h = new History();
                } else {
                    h = new History(fileHistory.getHistory().getInsertVersion()+1, 0);
                }
                history = new FileHistory(f, projectHistory.getId(), h);
            } else {
                history = null;
            }
            if (history != null) {
                em.persist(history);
            }

            closeEntityManager(em);
        } catch (Exception e) {
            LOG.error("Exception in history creation.", e);
        }
    }

    @PostUpdate
    public void updateHistory(Object o) {
        try {
            EntityManager em = newEntityManager();

            Object history;
            if (o instanceof File) {
                File f = (File) o;
                ProjectHistory projectHistory = getLastProjectHistory(em, f.getProject());
                FileHistory fileHistory = getLastFileHistory(em, projectHistory.getId(), f.getName());
                History h = new History(fileHistory.getHistory().getInsertVersion(), fileHistory.getHistory().getUpdateVersion()+1);
                history = new FileHistory(f, fileHistory.getProjectId(), h);
            } else {
                history = null;
            }
            if (history != null) {
                em.persist(history);
            }

            closeEntityManager(em);
        } catch (Exception e) {
            LOG.error("Exception in history creation.", e);
        }
    }
    
    @PostRemove
    public void removeHistory(Object o) {
        try {
            EntityManager em = newEntityManager();

            if (o instanceof Project) {
                Project project = (Project) o;
                ProjectHistory projectHistory = getLastProjectHistory(em, project);
                projectHistory.getHistory().setDeletedOn(new Date());
                em.persist(projectHistory);
            } else if(o instanceof File) {
                File f = (File) o;
                ProjectHistory projectHistory = getLastProjectHistory(em, f.getPostRemoveProjectReference());
                FileHistory fileHistory = getLastFileHistory(em, projectHistory.getId(), f.getName());
                fileHistory.getHistory().setDeletedOn(new Date());
                em.persist(fileHistory);
            }

            closeEntityManager(em);
        } catch (Exception e) {
            LOG.error("Exception in history creation.", e);
        }
    }
    
    private ProjectHistory getLastProjectHistory(EntityManager em, Project project) {
        String userId = project.getUserId();
        String name = project.getName();
        StringBuilder sb = new StringBuilder(1000);
        sb.append("select h from ProjectHistory as h");
        sb.append("    where h.userId = :userId");
        sb.append("        and h.name = :name");
        sb.append("        and h.history.insertVersion = (");
        sb.append("             select max(ih.history.insertVersion) from ProjectHistory as ih");
        sb.append("                 where ih.userId = :userId and ih.name = :name)");
        sb.append("        and h.history.updateVersion = (");
        sb.append("             select max(uh.history.updateVersion) from ProjectHistory as uh");
        sb.append("                 where uh.userId = :userId and uh.name = :name");
        sb.append("                     and uh.history.insertVersion = h.history.insertVersion)");
        Query query = em.createQuery(sb.toString());
        query.setParameter("userId", userId);
        query.setParameter("name", name);
        return (ProjectHistory) query.getSingleResult();
    }

    private FileHistory getLastFileHistory(EntityManager em, Integer projectId, String name) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("select h from FileHistory as h");
        sb.append("    where h.projectId = :projectId");
        sb.append("        and h.name = :name");
        sb.append("        and h.history.insertVersion = (");
        sb.append("             select max(ih.history.insertVersion) from FileHistory as ih");
        sb.append("                 where ih.projectId = :projectId and ih.name = :name)");
        sb.append("        and h.history.updateVersion = (");
        sb.append("             select max(uh.history.updateVersion) from FileHistory as uh");
        sb.append("                 where uh.projectId = :projectId and uh.name = :name");
        sb.append("                     and uh.history.insertVersion = h.history.insertVersion)");
        Query query = em.createQuery(sb.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("name", name);
        return (FileHistory) query.getSingleResult();
    }

}
