package com.htong.ws;
import javax.xml.ws.Endpoint;
public enum WebServiceController {
	instance;	
	private Endpoint endPoint;

	public void startWebService() {
		String url = "http://10.67.106.160:8090/ws";
		if(endPoint == null || !endPoint.isPublished()) {
			endPoint = Endpoint.publish(url, new com.htong.ws.WebServiceClass());
		}		
	}
	
//	public void stopWebService() {
//		if(endPoint != null && endPoint.isPublished()) {
//			endPoint.stop();
//			endPoint = null;
//		}
//	}	
	public static void main(String args[]) {
		WebServiceController.instance.startWebService();
	}
}
