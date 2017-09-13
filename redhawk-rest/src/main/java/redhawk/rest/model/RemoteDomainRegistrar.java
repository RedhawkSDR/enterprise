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
package redhawk.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModelProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoteDomainRegistrar {
	@ApiModelProperty(value = "Remote domain name to registrar", required=true)
	private String domainName; 
	
	@ApiModelProperty(value = "omniORB port for the remote domain", required=true)
	private Integer nameServerPort;
	
	@ApiModelProperty(value = "omniORB host for the remote domain", required=true)
	private String nameServerHost;
	
	public RemoteDomainRegistrar() {	
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Integer getNameServerPort() {
		return nameServerPort;
	}

	public void setNameServerPort(Integer nameServerPort) {
		this.nameServerPort = nameServerPort;
	}

	public String getNameServerHost() {
		return nameServerHost;
	}

	public void setNameServerHost(String nameServerHost) {
		this.nameServerHost = nameServerHost;
	}
	
	
}
