package hr.fer.zemris.vhdllab.dao.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalFileDAOMemoryImpl implements GlobalFileDAO {

	private long id = 0;

	Map<Long, GlobalFile> files = new HashMap<Long, GlobalFile>();

	public synchronized GlobalFile load(Long id) throws DAOException {
		GlobalFile file = files.get(id);
		if(file==null) throw new DAOException("Unable to load global file!");
		return file;
	}

	public synchronized void save(GlobalFile file) throws DAOException {
		if(file.getId()==null) file.setId(Long.valueOf(id++));
		files.put(file.getId(), file);
	}

	public synchronized void delete(GlobalFile file) throws DAOException {
		files.remove(file.getId());
	}

	public synchronized List<GlobalFile> findByType(String type) throws DAOException {
		List<GlobalFile> fileList = new ArrayList<GlobalFile>();
		for(GlobalFile f : files.values()) {
			if(f.getType().equals(type)) {
				fileList.add(f);
			}
		}
		return fileList;
	}
	
	public synchronized boolean exists(Long fileId) throws DAOException {
		return files.containsKey(fileId);
	}
	
	public synchronized boolean exists(String name) throws DAOException {
		for(GlobalFile f : files.values()) {
			if(f.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}
