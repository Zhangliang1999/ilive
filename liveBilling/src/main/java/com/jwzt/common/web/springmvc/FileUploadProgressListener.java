package com.jwzt.common.web.springmvc;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author ysf
 */

@Component
public class FileUploadProgressListener implements ProgressListener {
	private final static Logger log = LogManager.getLogger();

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		log.trace("pBytesRead={}, pContentLength={}, pItems={}", pBytesRead, pContentLength, pItems);
	}

}
