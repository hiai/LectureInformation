package com.example.administrator.freshapp.Util;

import java.util.Map;

/**
 * Created by Administrator on 2015/3/21.
 */
public interface DataService {
    public boolean addData(Object[] params);
    public Map<String,String> readData(String[] number);
    public boolean deleteData(Object[] number);
    public  boolean updateData(Object[] number);
    public Map<String,String> readDataByTitle(String title);
}
