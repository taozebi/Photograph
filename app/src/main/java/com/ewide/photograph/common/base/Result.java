package com.ewide.photograph.common.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Http 请求返回数据结构
 * Created by Taoze on 2018/04/21.
 */
public class Result<T> implements Serializable {

    public static String KEY_PAGE_SIZE = "pageSize";
    public static String KEY_PAGE_INDEX = "pageIndex";
    public static int PAGE_SIZE = 20;
    public static int PAGE_INDEX = 1;
    private static final String OK = "ok";
    private String state;
    private String errCode;
    private String errMsg;
    private ResultMap resultMap;

    private List<T> datas;

    private T data;

    public String getState() {
        return state;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public ResultMap getResultMap() {
        return resultMap;
    }

    public JsonArray getResultJsonArray() {
        return resultMap.getResults();
    }

    public T getData(Class<T> cls){
        if(data != null){
            return data;
        }
        data = getResultMap().getData(cls);
        return data;
    }

    public List<T> getDatas(Class<T> cls){
        if(datas != null){
            return datas;
        }
        datas = getResultMap().getDateList(cls);
        return datas;
    }

    public boolean hasData(){
        if(getResultMap().getResult() != null){
            return true;
        }

        if(getResultMap().getResults() != null && getResultMap().getResults().size() > 0){
            return true;
        }
        return false;
    }

    public boolean isOK(){
        if(OK.equals(getState())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    class ResultMap{
        private int count;
        private JsonArray results;
        private JsonObject result;

        public int getCount() {
            return count;
        }

        public JsonArray getResults() {
            return results;
        }

        public JsonObject getResult() {
            return result;
        }

        public String toJson(){
            return new Gson().toJson(this);
        }
        public String getResultsJson(){
            return getResults().toString();
        }

        public String getResultJson(){
            return getResult().toString();
        }

        public T getData(Class<T> clz){
            T obj = null;
            try {
                GsonBuilder builder = new GsonBuilder();
                // Register an adapter to manage the date types as long values
                //TODO 解决Gson解析 时间格式的数据出现问题
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();
                JsonObject jsonObject = new JsonParser().parse(getResultJson()).getAsJsonObject();
                obj = gson.fromJson(jsonObject, clz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }

        public List<T> getDateList(Class<T> clz) {
            List<T> list = new ArrayList<T>();
            try {
                GsonBuilder builder = new GsonBuilder();
                // Register an adapter to manage the date types as long values
                //TODO 解决Gson解析 时间格式的数据出现问题
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();
                JsonArray arry = new JsonParser().parse(getResultsJson()).getAsJsonArray();
                for (JsonElement jsonElement : arry) {
                    list.add(gson.fromJson(jsonElement, clz));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}
