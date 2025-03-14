package com.bvRadio.iLive.common.web.cos;

import java.io.*;

/**
 * An interface to provide a pluggable file renaming policy, particularly useful
 * to handle naming conflicts with an existing file.
 * 
 * @author Jason Hunter
 * @version 1.0, 2002/04/30, initial revision, thanks to Changshin Lee for the
 *          basic idea
 */
public interface FileRenamePolicy {

	/**
	 * Returns a File object holding a new name for the specified file.
	 *
	 * @see FilePart#writeTo(File fileOrDirectory)
	 */
	public File rename(File f);

}
