package org.ofbiz.ext.geo;

import javolution.util.FastList

import org.ofbiz.base.util.UtilMisc
import org.ofbiz.service.ServiceUtil
import org.ofbiz.entity.*
import org.ofbiz.service.*

try {
    DispatchContext dctx = (DispatchContext) context.get("dctx");
    Delegator delegator = dctx.getDelegator();

    def result = ServiceUtil.returnSuccess()
    result.result = "load data success!"

    def filePath = "hot-deploy/extcommon/data/taobao_area_20130720.txt";
    File file = new File(filePath);

    def geoList = FastList.newInstance();
    def geoAssocList = FastList.newInstance();
    int count = 0
    file.eachLine { line ->
        geoList.add(generateGeo(generateArea(line), delegator));
        geoAssocList.add(generateGeoAssoc(generateArea(line), delegator));
        count++;
    }

    delegator.storeAll(geoList);

    delegator.storeAll(geoAssocList);

    println "xxxxxx: " + count + " | geoList: " + geoList.size() + " | geoAssocList: " + geoAssocList.size();

    return result
} catch (Throwable e) {
    e.printStackTrace();
}

static GenericValue generateGeoAssoc(Area area, Delegator delegator) {
    def map = UtilMisc.toMap("geoId", area.parentGeoId, "geoIdTo", area.geoId, "geoAssocTypeId", "REGIONS")
    return delegator.makeValue("GeoAssoc", map);
}

static GenericValue generateGeo(Area area, Delegator delegator) {
    def map = UtilMisc.toMap("geoId", area.geoId, "geoTypeId", area.geoTypeId, "geoName", area.geoName, "wellKnownText", area.wellKnownText)
    return delegator.makeValue("Geo", map);
}

static Area generateArea(String str) {
    Area area = new Area();
    String[] array = str.split(":");
    area.setGeoId(array[0]);

    String[] subArray = array[1].substring(1, array[1].length() - 2).split(",");

    area.setGeoName(subArray[0]);
    area.setParentGeoId(subArray[1]);
    area.setWellKnownText(subArray[2]);
    return area;
}


class Area {
    private String parentGeoId;
    private String geoId;
    private String geoName;
    private String wellKnownText;

    private String geoTypeId;

    public String getParentGeoId() {
        return parentGeoId;
    }

    public void setParentGeoId(String parentGeoId) {
        if (parentGeoId.equals("1")) {
            this.parentGeoId = "CHN";
        } else {
            this.parentGeoId = "CN-" + parentGeoId.trim().replace("﻿", "");
        }
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = "CN-" + geoId.trim().replace("﻿", "");
    }

    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public String getWellKnownText() {
        return wellKnownText;
    }

    public void setWellKnownText(String wellKnownText) {
        this.wellKnownText = wellKnownText.replace(" ", "");
    }

    public String getGeoTypeId() {
        if (geoId.endsWith("0000")) {
            geoTypeId = "PROVINCE";
        } else if (geoId.endsWith("00")) {
            geoTypeId = "CITY";
        } else {
            geoTypeId = "COUNTY";
        }
        return geoTypeId;
    }

    public void setGeoTypeId(String geoTypeId) {
        this.geoTypeId = geoTypeId;
    }
}