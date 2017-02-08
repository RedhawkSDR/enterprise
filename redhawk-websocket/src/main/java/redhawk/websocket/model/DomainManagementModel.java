/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.websocket.model;


/**
 * POJO for representing DomainManagementObject.*EventType
 */
public class DomainManagementModel {
	private String producerId; 
	
	private String sourceId; 
	
	private String sourceIOR;
	
	private SourceCategory sourceCategory;
	
	private String sourceName;
	
	private DomainManagementAction action; 
	
	public DomainManagementModel(){
		
	}

	public DomainManagementAction getAction() {
		return action;
	}

	public void setAction(DomainManagementAction action) {
		this.action = action;
	}

	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceIOR() {
		return sourceIOR;
	}

	public void setSourceIOR(String sourceIOR) {
		this.sourceIOR = sourceIOR;
	}

	public SourceCategory getSourceCategory() {
		return sourceCategory;
	}

	public void setSourceCategory(SourceCategory sourceCategory) {
		this.sourceCategory = sourceCategory;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	
	
}
