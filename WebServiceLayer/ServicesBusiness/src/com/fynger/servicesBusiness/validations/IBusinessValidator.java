/**
 * 
 */
package com.fynger.servicesBusiness.validations;

import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;

/**
 * @author Rishi
 *
 */
public interface IBusinessValidator {
	
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException;

}
