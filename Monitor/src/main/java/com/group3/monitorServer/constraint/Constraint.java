package com.group3.monitorServer.constraint;

/**
 * Class to contain the information about Constraints defined on endpoints, with the possibility of also specifying the specific node ID with in the distributed system the endpoint is specified.
 * <br>
 * The nodeID is to be used if multiple nodes in the monitored distributed system have the same endpoint path defined and the Constraint information for the endpoints needs to be differentiated.
 * <br>
 * A general Constraint for an endpoint path can be created by omitting the nodeID information, this will make it so that if a specific Constraint can't be found, a search for a general can be triggered.
 *
 */
public class Constraint {

	// Required
	private Integer max;
	private String endpoint;
	
	// Optional
	private Integer min;
	private String name;
	private String description;
	private Integer nodeID;
	
	/**
	 * Constructor for the Constraint class.
	 * 
	 * @param endpoint The path of the endpoint that the Constraint is associated with.
	 * @param max The maximum amount of time allowed for a call to the specified endpoint in milliseconds (inclusive).
	 */
	public Constraint(String endpoint, Integer max) {
		this.endpoint = endpoint;
		this.max = max;
	}
	
	/**
	 * Sets the minimum value on the Constraint (optional) (inclusive).
	 * 
	 * @param min The minimum time that a call needs to take.
	 * @return The current Constraint instance.
	 */
	public Constraint withMin(Integer min) {
		this.min = min;
		return this;
	}
	
	/**
	 * Sets the name associated with the Constraint (optional).
	 * 
	 * @param name The name associated with the Constraint.
	 * @return The current Constraint instance.
	 */
	public Constraint withName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the description associated with the Constraint (optional).
	 * 
	 * @param description The description associated with the Constraint.
	 * @return The current Constraint instance.
	 */
	public Constraint withDescription(String description) {
		this.description = description;
		return this;
	}
	
	/**
	 * Sets the ID of the node which contains the endpoint being constrained (optional).
	 * 
	 * @param nodeID The ID of the node containing the constrained endpoint.
	 * @return The current Constraint instance.
	 */
	public Constraint withNodeID(Integer nodeID) {
		this.nodeID = nodeID;
		return this;
	}

	/**
	 * Get the maximum time (inclusive) specified for the Constraint.
	 * 
	 * @return The maximum time (inclusive) specified for the Constraint.
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * Get the path of the endpoint.
	 * 
	 * @return The path of the endpoint.
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * Get the minimum time (inclusive) specified for the Constraint.
	 * 
	 * @return The minimum time (inclusive) specified for the Constraint.
	 */
	public Integer getMin() {
		return min;
	}

	/**
	 * Get the associated name of the Constraint.
	 * 
	 * @return The associated name of the Constraint.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the associated description of the Constraint.
	 * 
	 * @return The associated description of the Constraint.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the ID of the node containing the constrained endpoint.
	 * 
	 * @return The ID of the node containing the constrained endpoint.
	 */
	public Integer getNodeID() {
		return nodeID;
	}
}
