package com.jwzt.billing.manager;

import java.io.IOException;
import java.util.List;

import com.jwzt.billing.entity.bo.OperationUser;

/**
 * @author ysf
 */
public interface OperationUserMng {

	List<OperationUser> listByParams() throws IOException;

}
