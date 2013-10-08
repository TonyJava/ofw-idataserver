package com.htong.persist;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htong.domain.SQLData;

public class SQLDataDao {

	public void insert(SQLData sqlData) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		try {
			 ts = session.beginTransaction();

			session.save(sqlData);
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
