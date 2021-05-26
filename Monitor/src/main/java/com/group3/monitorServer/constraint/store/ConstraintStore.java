package com.group3.monitorServer.constraint.store;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;
import com.group3.monitorServer.constraint.exception.ConstraintAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

/**
 * The ConstraintStore is used to store the {@link Constraint}s that have been defined and exposing methods to access and modify the {@link Constraint}s in the store.
 *
 */
public class ConstraintStore {

	private Map<ConstraintKey, Constraint> constraints;
	
	/**
	 * Constructor for the ConstraintStore.
	 */
	public ConstraintStore() {
		constraints = new HashMap<ConstraintKey, Constraint>();
	}
	
	/**
	 * Adds a {@link Constraint} to the store.
	 * 
	 * @param constraint The constraint to add to the store.
	 * @throws ObjectAlreadyExistsException 
	 */
	public void addConstraint(Constraint constraint) {
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		
		if(constraints.containsKey(key)) {
			throw new ConstraintAlreadyExistsException("The constraint <" + constraint + "> already exists");
		}
		
		constraints.put(key, constraint);
	}
	
	/**
	 * Removes the {@link Constraint} with the given ConstraintKey from the store.
	 * 
	 * @param key The key associated with the constraint in the store.
	 * @return True if the a {@link Constraint} for the given key was removed, false otherwise.
	 */
	public boolean removeConstraint(ConstraintKey key) {
		if (constraints.remove(key) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Retrieves the {@link Constraint} with the given key from the store.
	 * 
	 * @param key The key associated with the {@link Constraint} to retrieve.
	 * 
	 * @return The {@link Constraint} associated with the key in the store, if no {@link Constraint} is associated with the key then null is returned.
	 */
	public Constraint findConstraint(ConstraintKey key) {
		return findConstraint(key, false);
	}
	
	/**
	 * Retrieves the {@link Constraint} with the given key from the store.
	 * 
	 * @param key The key associated with the {@link Constraint} to retrieve.
	 * @param checkForGeneralConstraint Determines if a {@link Constraint} could not be found for a specific node, if instead a general {@link Constraint} for the endpoint should be searched for.
	 * 
	 * @return The {@link Constraint} associated with the key in the store, if no {@link Constraint} is associated with the key then null is returned.
	 */
	public Constraint findConstraint(ConstraintKey key, boolean checkForGeneralConstraint) {
		Constraint con = constraints.get(key);
		if (con == null && checkForGeneralConstraint) {
			return findConstraint(new ConstraintKey(key.getEndpoint()));
		}
		
		return con;
	}
}
