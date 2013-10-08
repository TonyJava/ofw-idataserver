package com.htong.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.htong.domain.WellModel;
import com.htong.idataserver.TagDataBase;
import com.htong.status.DTUStatus;

@WebService
public class WebServiceClass {
	
	public List<DTUStatusModel> getDTUStatus() {
		List<DTUStatusModel> dtuStatusList = new ArrayList<DTUStatusModel>();
		List<WellModel> wellList = TagDataBase.INSTANCE.getWellList();
		
		if(wellList != null && !wellList.isEmpty()) {
			for(WellModel well : wellList) {
				DTUStatusModel status = new DTUStatusModel();
				
				status.setWellNum(well.getNum());
				status.setDtuNum(well.getDtuId());
				status.setConnStatus(DTUStatus.instance.getDtuStatusMap().get(well.getDtuId()));
				status.setCommStatus(DTUStatus.instance.getCommStatusMap().get(well.getDtuId()));
				status.setHeartBeatTime(DTUStatus.instance.getHeartBeatMap().get(well.getDtuId()));
				
				dtuStatusList.add(status);
				System.out.println(well.getNum() + ":" + status.getConnStatus() + ":" + status.getCommStatus() + ":" + status.getHeartBeatTime());
			}
		}
		
		return dtuStatusList;
	}

}
