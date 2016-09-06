package org.ofbiz.express.postOder;

import org.ofbiz.express.pojo.TaskRequest;
import org.ofbiz.express.pojo.TaskResponse;
import org.ofbiz.ext.util.AppUtil;
import org.ofbiz.ext.util.ExtHttpClient;
import org.ofbiz.service.ServiceUtil;

import java.util.HashMap;
import java.util.Map;


public class PostOrder {

    public void post(TaskRequest req) {
        HashMap<String, String> p = new HashMap<String, String>();
        p.put("schema", "json");
        p.put("param", AppUtil.toJson(req));
        try {
            Map httpResult = ExtHttpClient.post("defalut.kuaidi.requesturl", p);
            if (!ServiceUtil.isSuccess(httpResult)) {
                System.out.println("订阅失败");
            }

            String ret = (String) httpResult.get("responseString");
            TaskResponse resp = AppUtil.fromJson(ret, TaskResponse.class);
            if (resp.getResult() == true) {
                System.out.println("订阅成功");
            } else {
                System.out.println("订阅失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        TaskRequest req = new TaskRequest();
        req.setCompany("yuantong");
        req.setFrom("上海浦东新区");
        req.setTo("广东深圳南山区");
        req.setNumber("12345678");
        req.getParameters().put("callbackurl", "http://www.yourdmain.com/kuaidi");
        req.setKey("defalut.kuaidi.Key");

        HashMap<String, String> p = new HashMap<String, String>();
        p.put("schema", "json");
        p.put("param", AppUtil.toJson(req));
        try {
            Map httpResult = ExtHttpClient.post("http://www.kuaidi100.com/poll", p);
            if (!ServiceUtil.isSuccess(httpResult)) {
                System.out.println("订阅失败");
            }

            String ret = (String) httpResult.get("responseString");
            TaskResponse resp = AppUtil.fromJson(ret, TaskResponse.class);
            if (resp.getResult() == true) {
                System.out.println("订阅成功");
            } else {
                System.out.println("订阅失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
