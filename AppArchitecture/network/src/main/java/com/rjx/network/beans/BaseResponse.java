package com.rjx.network.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Jianxiong Rao
 * @date 2020-04-15
 * @description : 返回用的基类
 */
public class BaseResponse {
    @SerializedName("showapi_res_code")
    @Expose
    public Integer showapiResCode;
    @SerializedName("showapi_res_error")
    @Expose
    public String showapiResError;
}
