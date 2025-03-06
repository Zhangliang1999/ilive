package com.bvRadio.iLive.iLive.manager;

import java.util.Set;

public interface SentitivewordFilterMng {

	Set<String> getSensitiveWord(String text);
	
	String replaceSensitiveWord(String text);
}
