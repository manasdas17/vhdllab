package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.EntityDAO;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

/**
 * This class fully implements {@link EntityDAO} interface by persisting
 * entities to database and in addition it defines extra methods for finding
 * entities through queries.
 * 
 * @param <T>
 *            an entity type
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public abstract class AbstractEntityDAO<T> implements EntityDAO<T> {

	/**
	 * A query hint key for hibernate persistence provider to set query as
	 * cacheable.
	 */
	private static final String CACHEABLE_HINT = "org.hibernate.cacheable";

	/**
	 * A logger for this class.
	 */
	private static final Logger log = Logger.getLogger(AbstractEntityDAO.class);

	/**
	 * A class of an entity.
	 */
	private Class<T> clazz;

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            a class of an entity
	 * @throws NullPointerException
	 *             if <code>clazz</code> is <code>null</code>
	 */
	public AbstractEntityDAO(Class<T> clazz) {
		if (clazz == null) {
			throw new NullPointerException("Entity class cant be null");
		}
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.EntityDAO#save(java.lang.Object)
	 */
	@Override
	public void save(T entity) throws DAOException {
		if (entity == null) {
			throw new NullPointerException("Entity cant be null");
		}
		EntityManager em = EntityManagerUtil.currentEntityManager();
		EntityManagerUtil.beginTransaction();
		try {
			em.persist(entity);
		} catch (EntityExistsException e) {
            EntityManagerUtil.rollbackTransaction();
			log.error("Possible constraint violation: " + entity, e);
			throw new DAOException(StatusCodes.DAO_ALREADY_EXISTS, e);
		} catch (PersistenceException e) {
		    EntityManagerUtil.rollbackTransaction();
		    if(e.getCause() instanceof ConstraintViolationException) {
		        /*
		         * When upgraded to hibernate to version 3.3.0.GA it seems that
		         * hibernate doesn't wrap JPA exceptions correctly. When trying
		         * to persist an entity that already exists hibernate
		         * persistence provider should throw EntityExistsException but
		         * hibernate wraps ConstraintViolationException in
		         * PersistenceException!
		         * 
		         * Reported as bug in hibernate JIRA:
		         * http://opensource.atlassian.com/projects/hibernate/browse/EJB-382
		         */
	            log.error("Possible constraint violation: " + entity, e);
	            throw new DAOException(StatusCodes.DAO_ALREADY_EXISTS, e);
		    }
			log.error("Unexpected error.", e);
			throw new DAOException(StatusCodes.SERVER_ERROR, e);
		}
		EntityManagerUtil.commitAndCloseTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.EntityDAO#load(java.lang.Long)
	 */
	@Override
	public T load(Integer id) throws DAOException {
		T entity = loadEntityImpl(id);
		if (entity == null) {
			throw new DAOException(StatusCodes.DAO_DOESNT_EXIST, clazz
					.getCanonicalName()
					+ "[" + id + "]" + " doesn't exit");
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.EntityDAO#exists(java.lang.Long)
	 */
	@Override
	public boolean exists(Integer id) throws DAOException {
		T entity = loadEntityImpl(id);
		return entity != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.EntityDAO#delete(java.lang.Long)
	 */
	@Override
	public void delete(Integer id) throws DAOException {
		if (id == null) {
			throw new NullPointerException("Entity identifier cant be null");
		}
		EntityManager em = EntityManagerUtil.currentEntityManager();
		EntityManagerUtil.beginTransaction();
		try {
			T entity = em.find(clazz, id);
			if (entity == null) {
	            EntityManagerUtil.rollbackTransaction();
				throw new DAOException(StatusCodes.DAO_DOESNT_EXIST, clazz
						.getCanonicalName()
						+ "[" + id + "]" + " doesn't exit");
			}
			preDeleteAction(entity);
			em.remove(entity);
		} catch (PersistenceException e) {
            EntityManagerUtil.rollbackTransaction();
			log.error("Unexpected error.", e);
			throw new DAOException(StatusCodes.SERVER_ERROR, e);
		}
		EntityManagerUtil.commitAndCloseTransaction();
	}

	/**
	 * This method is called each time an entity is about to be deleted.
	 * 
	 * @param entity
	 *            an entity that will be deleted
	 */
	protected void preDeleteAction(T entity) {
	}

	/**
	 * Returns <code>true</code> if an entity was found in a query.
	 * 
	 * @param query
	 *            a query to execute
	 * @param paramters
	 *            a parameters to a query
	 * @return an entity list
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	protected final boolean existsEntity(String query,
			Map<String, Object> paramters) throws DAOException {
		T entity = findSingleEntityImpl(query, paramters);
		return entity != null;
	}

	/**
	 * Returns a single entity found by specified query. A
	 * {@link DAOException} will be thrown no entity was found by a query.
	 * 
     * @param query
     *            a query to execute
	 * @param parameters
	 *            a parameters to a query
	 * @return an entity
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	protected final T findSingleEntity(String query,
			Map<String, Object> parameters) throws DAOException {
		T entity = findSingleEntityImpl(query, parameters);
		if (entity == null) {
			throw new DAOException(StatusCodes.DAO_DOESNT_EXIST,
					"No entity for such constraints");
		}
		return entity;
	}

	/**
	 * Returns a list of entities found by specified query. A
	 * {@link DAOException} will be thrown no entity was found by a query.
	 * 
     * @param query
     *            a query to execute
	 * @param parameters
	 *            a parameters to a query
	 * @return an entity list
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	protected final List<T> findEntityList(String query,
			Map<String, Object> parameters) throws DAOException {
		List<T> entities = findEntityListImpl(query, parameters);
		if (entities == null) {
			throw new DAOException(StatusCodes.DAO_DOESNT_EXIST,
					"No entities for such constraints");
		}
		return entities;
	}

	/**
	 * This is an actual implementation of load entity method. If an entity with
	 * specified <code>id</code> doesn't exist then this method will return
	 * <code>null</code>.
	 * 
	 * @param id
	 *            an identifier of an entity
	 * @return an entity with specified id
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	private T loadEntityImpl(Integer id) throws DAOException {
		if (id == null) {
			throw new NullPointerException("Entity identifier cant be null");
		}
		EntityManager em = EntityManagerUtil.currentEntityManager();
		EntityManagerUtil.beginTransaction();
		T entity;
		try {
			entity = em.find(clazz, id);
		} catch (PersistenceException e) {
            EntityManagerUtil.rollbackTransaction();
			log.error("Unexpected error.", e);
			throw new DAOException(StatusCodes.SERVER_ERROR, e);
		}
		EntityManagerUtil.commitAndCloseTransaction();
		return entity;
	}

	/**
	 * Returns a single entity found by specified query. If no entity was
	 * found by query then this method will return <code>null</code>.
	 * 
     * @param query
     *            a query to execute
	 * @param parameters
	 *            a parameters to a query
	 * @return an entity
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	@SuppressWarnings("unchecked")
	private T findSingleEntityImpl(String queryString,
			Map<String, Object> parameters) throws DAOException {
		checkParams(queryString, parameters);
		EntityManagerUtil.beginTransaction();
		T entity;
		try {
			Query query = createQuery(queryString, parameters);
			entity = (T) query.getSingleResult();
		} catch (NoResultException e) {
			EntityManagerUtil.rollbackTransaction();
			return null;
		} catch (EntityExistsException e) {
            EntityManagerUtil.rollbackTransaction();
			log.error("Possible constraint violation: query: "
					+ queryString + ", params: " + parameters.toString(), e);
			throw new DAOException(StatusCodes.DAO_ALREADY_EXISTS, e);
		} catch (PersistenceException e) {
            EntityManagerUtil.rollbackTransaction();
			log.error("Unexpected error.", e);
			throw new DAOException(StatusCodes.SERVER_ERROR, e);
		}
		EntityManagerUtil.commitAndCloseTransaction();
		if (entity.getClass() != clazz) {
			throw new ClassCastException("Expected " + clazz.getCanonicalName()
					+ " but found " + entity.getClass().getCanonicalName());
		}
		return entity;
	}

	/**
	 * Returns a list of entities found by specified query. If no entity
	 * was found by a query then this method will return <code>null</code>.
	 * 
     * @param query
     *            a query to execute
	 * @param parameters
	 *            a parameters to a query
	 * @return an entity list
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	@SuppressWarnings("unchecked")
	private List<T> findEntityListImpl(String queryString,
			Map<String, Object> parameters) throws DAOException {
		checkParams(queryString, parameters);
		EntityManagerUtil.beginTransaction();
		List<T> entities;
		try {
			Query query = createQuery(queryString, parameters);
			entities = query.getResultList();
		} catch (NoResultException e) {
			EntityManagerUtil.rollbackTransaction();
			return null;
		} catch (PersistenceException e) {
            EntityManagerUtil.rollbackTransaction();
			log.error("Unexpected error.", e);
			throw new DAOException(StatusCodes.SERVER_ERROR, e);
		}
		EntityManagerUtil.commitAndCloseTransaction();
		if (!entities.isEmpty() && entities.get(0).getClass() != clazz) {
			throw new ClassCastException("Expected " + clazz.getCanonicalName()
					+ " but found "
					+ entities.get(0).getClass().getCanonicalName());
		}
		return entities;
	}

	/**
	 * Throws adequate exception if parameters are faulty.
	 * 
     * @param query
     *            a query to execute
	 * @param parameters
	 *            a parameters to a query
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	private void checkParams(String query, Map<String, Object> parameters) {
		if (query == null) {
			throw new NullPointerException("Query string cant be null");
		}
		if (parameters == null) {
			throw new NullPointerException("Query parameters can't be null");
		}
	}

	/**
	 * Creates a query out of query string and specified parameters.
	 * 
     * @param query
     *            a query to execute
	 * @param parameters
	 *            a parameters to a query
	 * @return created query
	 */
	private Query createQuery(String queryString, Map<String, Object> parameters) {
		EntityManager em = EntityManagerUtil.currentEntityManager();
		Query query = em.createQuery(queryString);
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		/*
		 * Sets a query as cacheable. This hint is implementation dependent.
		 */
		query.setHint(CACHEABLE_HINT, Boolean.TRUE);
		return query;
	}

}
