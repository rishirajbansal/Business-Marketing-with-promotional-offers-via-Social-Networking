/**
 * 
 */
package com.fynger.servicesBusiness.business.base;

import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;


/**
 * @author Rishi
 *
 */
public interface IBusinessAction {
	
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception;

}
