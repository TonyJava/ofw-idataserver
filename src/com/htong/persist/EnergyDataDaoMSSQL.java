package com.htong.persist;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htong.domain.EnergyDataMSSQL;

public class EnergyDataDaoMSSQL {

	public void insert(EnergyDataMSSQL energyDataMSSQL) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		try {
			 ts = session.beginTransaction();

			session.save(energyDataMSSQL);
			session.flush();

			ts.commit();
		} catch (HibernateException e) {
			if(ts!=null) {
				ts.rollback();
			}
			e.printStackTrace();
		} finally {
			MySessionFactory.closeSession();
		}
		
	}

}
