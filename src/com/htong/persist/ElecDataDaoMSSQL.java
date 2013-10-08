package com.htong.persist;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htong.domain.ElecDataMSSQL;

public class ElecDataDaoMSSQL {

	public void insert(ElecDataMSSQL elecDataMSSQL) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		try {
			 ts = session.beginTransaction();

			session.save(elecDataMSSQL);
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
