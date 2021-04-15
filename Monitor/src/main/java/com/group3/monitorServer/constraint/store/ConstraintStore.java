package com.group3.monitorServer.constraint.store;

import java.util.HashMap;
import java.util.Map;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;

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
	 */
	public void addConstraint(Constraint constraint) {
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
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
	 * @return The {@link Constraint} associated with the key in the store, if no {@link Constraint} is associated with the key then null is returned.
	 */
	public Constraint findConstraint(ConstraintKey key) {
		return constraints.get(key);
	}
}
