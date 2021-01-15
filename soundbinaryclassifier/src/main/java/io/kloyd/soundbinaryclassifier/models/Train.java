package io.kloyd.soundbinaryclassifier.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="training")
public class Train {
	
	@Id
	@Column(name="uuid")
	private String uuid;
	
	@Column(name="orgid")
	private String orgid;
	
	@Column(name="sharelink")
	private String shareLink;
	
	@Column(name="download")
	private boolean download;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}
	
	

}
