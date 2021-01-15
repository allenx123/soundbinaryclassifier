package io.kloyd.soundbinaryclassifierdownloader.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="organizations")
public class Organization {

	@Id
	@Column(name="orgid")
	private String orgID;
	
	@Column(name="email")
	private String email;

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
