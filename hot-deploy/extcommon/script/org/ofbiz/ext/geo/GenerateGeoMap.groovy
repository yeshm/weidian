package org.ofbiz.ext.geo

import javolution.util.FastMap
import org.apache.commons.io.FileUtils
import org.ofbiz.entity.Delegator
import org.ofbiz.entity.GenericValue
import org.ofbiz.ext.util.AppUtil
import org.ofbiz.service.ServiceUtil

try {
    def chinaGeoId = "CHN";
    Delegator delegator = delegator;

    def result = ServiceUtil.returnSuccess()
    result.result = "load data success!"

    def geoDataMap = FastMap.newInstance();

    getCasecadeGeos(chinaGeoId, delegator, geoDataMap);

    StringBuffer sb = new StringBuffer("var geoDataMap=");
    sb.append(AppUtil.toJson(geoDataMap));
    sb.append(";")

    def filePath = "hot-deploy/extcommon/webapp/extcommon/assets/js/geoDataMap.js";
    FileUtils.writeStringToFile(new File(filePath), sb.toString(), "utf-8");

    return result
} catch (Throwable e) {
    e.printStackTrace();
}

void getCasecadeGeos(String geoId, Delegator delegator, Map<String, String> geoDataMap) {
    def geos = delegator.findByAnd("GeoAssocAndGeoTo", [geoAssocTypeId: "REGIONS", geoIdFrom: geoId]);
    if (geos != null && geos.size() > 0) {
        for (GenericValue geo : geos) {
            def tmpgeoId = geo.getString("geoId");
            def tmpgeoName = geo.getString("geoName")
            println "geoid: " + tmpgeoId + " geoName: " + tmpgeoName;

            geoDataMap.put(tmpgeoId, tmpgeoName);
            getCasecadeGeos(tmpgeoId, delegator, geoDataMap);
        }
    }
}
