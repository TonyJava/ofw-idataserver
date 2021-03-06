package com.htong.persist;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.htong.domain.SQLWell;

public class SQLWellDao {

	public void insert(SQLWell sqlWell) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		try {
			 ts = session.beginTransaction();

			session.save(sqlWell);
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
	
	public SQLWell getWellByDtuId(String id) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		SQLWell sqlWell = null;
		try {
			 ts = session.beginTransaction();

			Query query = session.createQuery("from SQLWell as a where a.dtu='"+id+"'");
			sqlWell = (SQLWell) query.uniqueResult();

			ts.commit();
		} catch (HibernateException e) {
			if(ts!=null) {
				ts.rollback();
			}
			e.printStackTrace();
		} finally {
			MySessionFactory.closeSession();
		}
		
		return sqlWell;
	}

}
