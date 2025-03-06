package com.bvRadio.iLive.iLive.entity.base;

import com.bvRadio.iLive.iLive.entity.DocumentManager;

public class BaseILiveFileDoc {
	
	private Long id;
	
	private Long fileId;
	
	private DocumentManager document;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public DocumentManager getDocument() {
		return document;
	}

	public void setDocument(DocumentManager document) {
		this.document = document;
	}

	public BaseILiveFileDoc(Long id, Long fileId, DocumentManager document) {
		super();
		this.id = id;
		this.fileId = fileId;
		this.document = document;
	}

	public BaseILiveFileDoc() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
