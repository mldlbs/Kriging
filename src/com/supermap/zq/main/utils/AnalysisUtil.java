package com.supermap.zq.main.utils;

import com.alibaba.fastjson.JSONArray;
import com.supermap.analyst.spatialanalyst.*;
import com.supermap.data.*;
import com.supermap.zq.main.entitys.KrigingEntity;
import com.supermap.zq.main.Analysis.AnalysisRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 差值分析主工具类
 *
 * @author GZQ
 */
public class AnalysisUtil {
    private static Logger _log = LoggerFactory.getLogger(AnalysisRun.class);
    //裁剪空间数据
    private static Map tMap = new HashMap();
    public WorkSpaceUtil workSpaceUtil;
    {
        tMap.put("temp", "温度");
        tMap.put("water", "降水");
        tMap.put("hum", "湿度");
        tMap.put("windy", "风速");
        tMap.put("press", "气压");
    }

    private KrigingEntity kn;

    public KrigingEntity getKn() {
        return kn;
    }

    public void setKn(KrigingEntity kn) {
        this.kn = kn;
    }

    public AnalysisUtil() {
    }

    public AnalysisUtil(WorkspaceConnectionInfo info, Workspace workspace) {
        this.workSpaceUtil = WorkSpaceUtil.getInstance(info,workspace);
    }

    /**
     * 执行插值分析生成落区图需要的数据集
     *
     * @param kn
     */
    public void doAnalysis(KrigingEntity kn) {
        String analysisTargetName = this.interpolationKriging(kn);
        //String clipTargetName =  doClip(analysisTargetName);
        DatasetGrid datasetGrid = (DatasetGrid) this.workSpaceUtil.getDatasource().getDatasets().get(analysisTargetName);
        //_log.info(datasetGrid.getPrjCoordSys().getName());
        //this.extractIsoline(analysisTargetName,datasetGrid);
        this.extractIsoRegion(analysisTargetName, datasetGrid,kn);
    }

    /**
     * 执行删除过期数据集操作
     *
     * @param
     */
    public void doDelete() {
        Datasets datasets = this.workSpaceUtil.getDatasource().getDatasets();
        long fhTimes = DateUtil.getNdDateTimes(2);
        for (int i = 0; i < datasets.getCount(); i++) {
            Dataset dataset = datasets.get(i);
            String dataSetName = dataset.getName();
            String dataSetNameNum =  DateUtil.getNumber(dataSetName);
            if(dataSetNameNum.length()>=10){
                long dataTimes = DateUtil.getCurrDataTimes(dataSetNameNum.substring(0, 10));
                if (fhTimes > dataTimes) {
                    Boolean isSuccess =  datasets.delete(dataSetName);
                    if(isSuccess){
                        _log.info(dataSetName + "数据集删除成功 - \t" + DateUtil.getGeneralString());
                    }else{
                        _log.info(dataSetName + "数据集删除失败 - \t" + DateUtil.getGeneralString());
                    }
                }
            }
        }
        this.workSpaceUtil.getWorkspace().save();
    }

    /**
     * 克吕金差值分析
     *
     * @param kn
     */
    public String interpolationKriging(KrigingEntity kn) {
        _log.info(AnalysisUtil.tMap.get(kn.getType()) + "插值分析开始 - \t" + DateUtil.getGeneralString());
        // 构造克里金插值参数对像
        InterpolationKrigingParameter parameter = new InterpolationKrigingParameter();
        parameter.setSearchMode(SearchMode.KDTREE_FIXED_COUNT);
        parameter.setExpectedCount(12);
        parameter.setResolution(0.0005);
        parameter.setBounds(this.workSpaceUtil.getAnalysisDataset().computeBounds());
        // 插值分析
        String cts = DateUtil.getYMDHMString();
        String name = kn.getType() + "_K" + cts;
        if (this.workSpaceUtil.getDatasource().getDatasets().get(name) != null) {
            Boolean isSuccess =  this.workSpaceUtil.getDatasource().getDatasets().delete(name);
            if(isSuccess){
                _log.info(name + "数据集删除成功 - \t" + DateUtil.getGeneralString());
            }else{
                _log.info(name + "数据集删除失败 - \t" + DateUtil.getGeneralString());
            }
        }
        String targetName = this.workSpaceUtil.getDatasource().getDatasets().getAvailableDatasetName(name);
        _log.info("插值分析结果数据集：\t" + name);
        _log.info(this.workSpaceUtil.getAnalysisDataset().getPrjCoordSys().getName());
        System.out.println(kn.getVals().length);
        Interpolator.interpolate(parameter, kn.getP2ds(), kn.getVals(), this.workSpaceUtil.getAnalysisDataset().getPrjCoordSys(), 1, this.workSpaceUtil.getDatasource(), targetName, PixelFormat.SINGLE);
        this.workSpaceUtil.getWorkspace().save();
        _log.info(AnalysisUtil.tMap.get(kn.getType()) + "插值分析结束 - \t" + DateUtil.getGeneralString());
        return targetName;
    }

    /**
     * 裁剪面
     *
     * @param targetString
     */
    public String doClip(String targetString) {
        String[] targetNames = targetString.split(",");
        _log.info(AnalysisUtil.tMap.get(kn.getType()) + "裁剪面开始 - \t" + DateUtil.getGeneralString());
        Recordset recordset = this.workSpaceUtil.getClipDataset().getRecordset(false, CursorType.DYNAMIC);
        Geometry geometry = recordset.getGeometry();
        RasterClip.clip(this.workSpaceUtil.getDatasource().getDatasets().get(targetNames[1]), (GeoRegion) geometry, true, false, this.workSpaceUtil.getDatasource(), targetNames[0]);
        this.workSpaceUtil.getDatasource().getDatasets().delete(targetNames[1]);
        this.workSpaceUtil.getWorkspace().save();
        _log.info(AnalysisUtil.tMap.get(kn.getType()) + "裁剪面结束 - \t" + DateUtil.getGeneralString());
        return targetNames[0];
    }

    /**
     * 表面分析 提取等值线
     * Extract the isoline.
     */
    public void extractIsoline(String targetName, DatasetGrid datasetGrid) {
        _log.info(AnalysisUtil.tMap.get(kn.getType()) + "提取等值线 开始 - \t" + DateUtil.getGeneralString());
        try {
            // 设置表面分析提取操作参数
            SurfaceExtractParameter parameter = new SurfaceExtractParameter();
            parameter.setDatumValue(datasetGrid.getMinValue());
            parameter.setInterval((datasetGrid.getMaxValue() - datasetGrid.getMinValue()) / 5);
            parameter.setSmoothMethod(SmoothMethod.POLISH);
            parameter.setSmoothness(3);
            if (this.workSpaceUtil.getDatasource().getDatasets().get(targetName + "Line") != null) {
                Boolean isSuccess =  this.workSpaceUtil.getDatasource().getDatasets().delete(targetName + "Line");
                if(isSuccess){
                    _log.info(targetName + "Line" + "数据集删除成功 - \t" + new Date());
                }else{
                    _log.info(targetName + "Line" + "数据集删除失败 - \t" + new Date());
                }
            }
            String name = this.workSpaceUtil.getDatasource().getDatasets().getAvailableDatasetName(targetName + "Line");
            // 提取等值线
            Recordset recordset = this.workSpaceUtil.getClipDataset().getRecordset(false, CursorType.DYNAMIC);
            Geometry geometry = recordset.getGeometry();
            SurfaceAnalyst.extractIsoline(parameter, datasetGrid, this.workSpaceUtil.getDatasource(), name, (GeoRegion) geometry);
        } catch (RuntimeException e) {
            System.out.println("extractIsoLine:" + e.getMessage());
        }
        _log.info(AnalysisUtil.tMap.get(kn.getType()) + " 提取等值线 结束 - \t" + DateUtil.getGeneralString());
    }

    /**
     * 表面分析 提取等值面
     * Extract isoregion.
     */
    public void extractIsoRegion(String targetName, DatasetGrid datasetGrid,KrigingEntity kn) {
        _log.info(AnalysisUtil.tMap.get(kn.getType()) + " 提取等值面 开始 - \t" + DateUtil.getGeneralString());
        try {
            // 设置表面分析提取操作参数
            SurfaceExtractParameter parameter = new SurfaceExtractParameter();
            parameter.setDatumValue(datasetGrid.getMinValue());
            parameter.setInterval((datasetGrid.getMaxValue() - datasetGrid.getMinValue()) / 6);

            //parameter.setExpectedZValues(kn.getZValues());
            parameter.setSmoothMethod(SmoothMethod.BSPLINE);
            parameter.setSmoothness(2);
            if (this.workSpaceUtil.getDatasource().getDatasets().get(targetName + "Line") != null) {
                Boolean isSuccess =  this.workSpaceUtil.getDatasource().getDatasets().delete(targetName + "Region");
                if(isSuccess){
                    _log.info(targetName + "Region" + "数据集删除成功 - \t" + DateUtil.getGeneralString());
                }else{
                    _log.info(targetName + "Region" + "数据集删除失败 - \t" + DateUtil.getGeneralString());
                }
            }
            String name = this.workSpaceUtil.getDatasource().getDatasets().getAvailableDatasetName(targetName + "Region");
            // 提取等值面
            Recordset recordset = this.workSpaceUtil.getClipDataset().getRecordset(false, CursorType.DYNAMIC);
            Geometry geometry = recordset.getGeometry();
            DatasetVector datasetVector = SurfaceAnalyst.extractIsoregion(parameter, datasetGrid, this.workSpaceUtil.getDatasource(), name, (GeoRegion) geometry);
            if(datasetVector.getRecordCount()>0){
                _log.info(AnalysisUtil.tMap.get(kn.getType()) + "提取等值面 结束 - \t" + DateUtil.getGeneralString());
                _log.info("提取等值面结果数据集" + datasetVector.getName() + " - " +  DateUtil.getGeneralString());
                this.saveStatisticResource(datasetVector.getName(),datasetGrid.getMaxValue(),datasetGrid.getMinValue());
                String gridName = datasetGrid.getName();
                Boolean isSuccess = this.workSpaceUtil.getDatasource().getDatasets().delete(gridName);
                if(isSuccess){
                    _log.info(gridName + "数据集删除成功 - \t" + DateUtil.getGeneralString());
                }else{
                    _log.info(gridName + "数据集删除失败 - \t" + DateUtil.getGeneralString());
                }
            }
        } catch (RuntimeException e) {
            System.out.println("extractIsoRegion:" + e.getMessage()+e.getLocalizedMessage());
        }
        this.workSpaceUtil.getWorkspace().save();
    }

    /**
     * 保存最大值，最小值，到szsStatistic数据集
     * @param datasetName
     * @param max
     * @param min
     */
    public void saveStatisticResource(String datasetName,double max,double min){
        DatasetVectorInfo info = new DatasetVectorInfo();
        GeoPoint point = new GeoPoint(108,34);
        DatasetVector szsStatistic = (DatasetVector) this.workSpaceUtil.getDatasource().getDatasets().get("szsStatistic");
        Recordset recordset = szsStatistic.getRecordset(false, CursorType.DYNAMIC);
        java.util.Map<java.lang.String,java.lang.Object> values = new HashMap<String, Object>();
        values.put("dsName",datasetName);
        values.put("dsMax",max);
        values.put("dsMin",min);
        recordset.addNew(point,values);
        recordset.update();
        this.workSpaceUtil.getWorkspace().save();
        // 关闭记录集，释放几何对象、记录集
        //szsStatistic.append(recordset);
    }

    /**
     * 解析请求过来的数据
     */
    private Map<String, Object> analysisData(Recordset recordset,Map<String, JSONArray> dataMap){
        DecimalFormat df = new DecimalFormat("0.00");//转换成Double 格式化
        Map<String, Object> pointDataMap = new HashMap<>();
        Map<Integer, Feature> map = recordset.getAllFeatures();
        Point2D[] p2ds = new Point2D[map.keySet().size()];
        double[] tempValues = new double[map.keySet().size()];
        double[] waterValues = new double[map.keySet().size()];
        double[] humValues = new double[map.keySet().size()];
        double[] windyValues = new double[map.keySet().size()];
        double[] pressValues = new double[map.keySet().size()];
        int i = 0;
        for (Integer in : map.keySet()) {
            p2ds[i] = map.get(in).getGeometry().getInnerPoint();
            //_log.info(p2ds[i].x+ "");
            Integer num = map.get(in).getInt32("F_NUM");
            Double tempRes = dataMap.get("temp").getDouble(num);
            tempValues[i] = Double.parseDouble(df.format(tempRes));
            Double waterRes = dataMap.get("water").getDouble(num);
            waterValues[i] = Double.parseDouble(df.format(waterRes));
            Double humRes = dataMap.get("hum").getDouble(num);
            humValues[i] = Double.parseDouble(df.format(humRes));
            Double windyRes = dataMap.get("windy").getDouble(num);
            windyValues[i] = Double.parseDouble(df.format(windyRes));
            Double pressRes = dataMap.get("press").getDouble(num);
            pressValues[i] = Double.parseDouble(df.format(pressRes));
            i++;
        }
        pointDataMap.put("points", p2ds);
        pointDataMap.put("temp", tempValues);
        pointDataMap.put("water", waterValues);
        pointDataMap.put("hum", humValues);
        pointDataMap.put("windy", windyValues);
        pointDataMap.put("press", pressValues);
        _log.info( "查询数据成功 - \t" + DateUtil.getGeneralString());
        return pointDataMap;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public Map<String, Object> getPoints(String url) {
        _log.info( "查询格点数据 - \t" + DateUtil.getGeneralString());
        Recordset recordset = this.workSpaceUtil.getAnalysisDataset().getRecordset(false, CursorType.DYNAMIC);
        //请求数据
        Map<String, JSONArray> dataMap = new HashMap<>();
        try {
            String res = NetUtil.post(url);
            dataMap = AnalysisDataUtil.getGridData(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.analysisData(recordset,dataMap);
    }


    /**
     * 获取温度格点数据
     *
     * @return
     */
    public Map<String, Object> getTempPoints() {
        _log.info( "查询格点数据 - \t" + DateUtil.getGeneralString());
        Recordset recordset = this.workSpaceUtil.getAnalysisDataset().getRecordset(false, CursorType.DYNAMIC);
        //请求数据
        Map<String, JSONArray> dataMap = new HashMap<>();
        try {
            dataMap = AnalysisDataUtil.getTempGridData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.analysisTempData(recordset,dataMap);
    }

    /**
     * 解析温度格点数据
     */
    private Map<String, Object> analysisTempData(Recordset recordset,Map<String, JSONArray> dataMap){
        DecimalFormat df = new DecimalFormat("0.00");//转换成Double 格式化
        Map<String, Object> pointDataMap = new HashMap<>();
        Map<Integer, Feature> map = recordset.getAllFeatures();
        Point2D[] p2ds = new Point2D[map.keySet().size()];
        double[] tempValues = new double[map.keySet().size()];
        int i = 0;
        for (Integer in : map.keySet()) {
            p2ds[i] = map.get(in).getGeometry().getInnerPoint();
            //_log.info(p2ds[i].x+ "");
            Integer num = map.get(in).getInt32("F_NUM");
            Double tempRes = dataMap.get("temp").getDouble(num);
            tempValues[i] = Double.parseDouble(df.format(tempRes));
            i++;
        }
        pointDataMap.put("points", p2ds);
        pointDataMap.put("temp", tempValues);
        _log.info( "查询数据成功 - \t" + DateUtil.getGeneralString());
        return pointDataMap;
    }
}
