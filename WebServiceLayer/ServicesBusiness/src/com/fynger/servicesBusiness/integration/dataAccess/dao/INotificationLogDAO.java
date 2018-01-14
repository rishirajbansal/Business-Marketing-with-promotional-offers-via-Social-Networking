package com.fynger.servicesBusiness.integration.dataAccess.dao;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.NotificationLogData;

public interface INotificationLogDAO {
	
	public boolean storeNotificationLog(NotificationLogData notificationLogData) throws DataAccessException;

}
