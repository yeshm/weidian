package org.ofbiz.ext.geo;
import javolution.util.FastList
import net.sf.json.JSONArray

import org.apache.commons.io.FileUtils;
import org.ofbiz.base.util.UtilValidate
import org.ofbiz.entity.*
import org.ofbiz.service.*

try {
	
	String chinaGeoId = "CHN";
	
	DispatchContext dctx = (DispatchContext) context.get("dctx");
	Delegator delegator = dctx.getDelegator();

	Map<String, Object> result = ServiceUtil.returnSuccess()
	result.result = "load data success!"
	
	List<MyArea> areaList = getCasecadeGeos(chinaGeoId, delegator);
	JSONArray jsonArray = JSONArray.fromObject(areaList);
	
	StringBuffer sb = new StringBuffer("var geoData=");
	sb.append(jsonArray.toString());
	sb.append(";")
	
	String filePath = "hot-deploy/extcommon/webapp/extcommon/assets/js/geoData.js";
	FileUtils.writeStringToFile(new File(filePath), sb.toString(), "utf-8");
	
	return result
} catch(Throwable e) {
	e.printStackTrace();
}

List<MyArea> getCasecadeGeos(String geoId, Delegator delegator){
	List<MyArea> areaList = FastList.newInstance();
	
	if(UtilValidate.isEmpty(geoId)) {
		return areaList;
	}
	
	List<GenericValue> geos = delegator.findByAnd("GeoAssocAndGeoTo", [geoAssocTypeId: "REGIONS", geoIdFrom: geoId]);
	if(geos != null && geos.size() > 0){
		for(GenericValue geo : geos){
			MyArea area = new MyArea();
			area.setId(geo.getString("geoId"));
			area.setName(geo.getString("geoName"));
			List<MyArea> childs = getCasecadeGeos(geo.getString("geoId"), delegator);
			if (UtilValidate.isNotEmpty(childs) && childs.size() > 0) {
				area.setChilds(childs);
			}
			areaList.add(area);
		}
	}
	return areaList;
}


class MyArea {
	private String id;
	private String name;
	private List<MyArea> childs;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MyArea> getChilds() {
		return childs;
	}
	public void setChilds(List<MyArea> childs) {
		this.childs = childs;
	}
}