package com.weidian.open.sdk.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.weidian.open.sdk.response.AbstractResponse;

public class VdianOrderListGetResponse extends AbstractResponse {

  private VdianOrderListGetResult result;

  public VdianOrderListGetResult getResult() {
    return result;
  }

  public void setResult(VdianOrderListGetResult result) {
    this.result = result;
  }


  /***** VdianOrderListGetResult *****/
  public static class VdianOrderListGetResult {

    @JsonProperty("total_num")
    private int totalNum;

    @JsonProperty("order_num")
    private int orderNum;

    private ListOrder[] orders;

    public int getTotalNum() {
      return totalNum;
    }

    public void setTotalNum(int totalNum) {
      this.totalNum = totalNum;
    }

    public int getOrderNum() {
      return orderNum;
    }

    public void setOrderNum(int orderNum) {
      this.orderNum = orderNum;
    }

    public ListOrder[] getOrders() {
      return orders;
    }

    public void setOrders(ListOrder[] orders) {
      this.orders = orders;
    }

  }

}
