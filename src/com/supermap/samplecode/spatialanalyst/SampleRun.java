package com.supermap.samplecode.spatialanalyst;

import com.supermap.analyst.spatialanalyst.*;
import com.supermap.data.*;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.ThemeLabel;
import com.supermap.ui.MapControl;

import java.awt.*;

/**
 * <p>
 * Title:插值分析
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------版权声明----------------------------
 * 此文件为SuperMap Objects Java 的示范代码 
 * 版权所有：北京超图软件股份有限公司
 * ----------------------------------------------------------------
 * ---------------------SuperMap iObjects Java 示范程序说明------------------------
 * 
 * 1、范例简介：展示插值分析中的距离反比插值（IDW)、克吕金插值或RBF插值功能
 * 2、示例数据：安装目录\SampleData\Interpolation\Interpolation.smwu
 * 3、关键类型/成员: 
 *      Layers.add 方法
 *      Layers.clear 方法
 *      InterpolationIDWParameter.setBounds 方法
 *      InterpolationIDWParameter.setResolution 方法
 *      InterpolationIDWParameter.setSearchMode 方法
 *      InterpolationIDWParameter.setExpectedCount 方法
 *      InterpolationIDWParameter.setPower 方法
 *      InterpolationKrigingParameter.setSearchMode 方法
 *      InterpolationKrigingParameter.setExpectedCount 方法
 *      InterpolationKrigingParameter.setResolution 方法
 *      InterpolationKrigingParameter.setBounds 方法
 *      InterpolationRBFParameter.setSearchMode 方法
 *      InterpolationRBFParameter.setTension 方法
 *      InterpolationRBFParameter.setExpectedCount 方法
 *      InterpolationRBFParameter.setResolution 方法
 *      InterpolationRBFParameter.setBounds 方法
 *      InterpolationRBFParameter.setSmooth 方法
 *      Interpolator.interpolate 方法      
 * 4、使用步骤：
 *   (1)选择距离反比插值（IDW)、克吕金插值或 RBF 插值按钮
 *   (2)在地图中查看
 * ------------------------------------------------------------------------------
 * ============================================================================>
 * </p> 
 * 
 * <p>
 * Company: 北京超图软件股份有限公司
 * </p>
 * 
 */
/**
 * <p>
 * Title: Interpolator
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------CopyRight----------------------------
 *This file contants the sample code of SuperMap Objects Java.
 * Copyright @2005-2012 SuperMap Software Co., Ltd. All Rights Reserved. 
 * ----------------------------------------------------------------
 * ---------------------SuperMap Objects Java Sample Code Description------------------------
 * 
 * 1. Sample Code Description:Demo for the Inverse Distance Weighted method, and the Radial Basis Function(RBF) interpolation method.
 * 2. Sample Data Location:Installation directory\SampleData\Interpolation\Interpolation.smw
 * 3,Key Classes/Methods:
 *      Layers.add method
 *      Layers.clear method
 *      InterpolationIDWParameter.setBounds method
 *      InterpolationIDWParameter.setResolution method
 *      InterpolationIDWParameter.setSearchMode method
 *      InterpolationIDWParameter.setExpectedCount method
 *      InterpolationIDWParameter.setPower method
 *      InterpolationKrigingParameter.setSearchMode method
 *      InterpolationKrigingParameter.setExpectedCount method
 *      InterpolationKrigingParameter.setResolution method
 *      InterpolationKrigingParameter.setBounds method
 *      InterpolationRBFParameter.setSearchMode method
 *      InterpolationRBFParameter.setTension method
 *      InterpolationRBFParameter.setExpectedCount method
 *      InterpolationRBFParameter.setResolution method
 *      InterpolationRBFParameter.setBounds method法
 *      InterpolationRBFParameter.setSmooth method
 *      Interpolator.interpolate method
 * 4. Procedures:
 *   (1)Select the button.
 *   (2)Display the result in the map.
 * ------------------------------------------------------------------------------
 * ============================================================================>
 * </p> 
 * 
 * <p>
 * Company: SuperMap Software Co., Ltd
 * </p>
 * 
 */
public class SampleRun {

	private MapControl m_mapControl;

	private Workspace m_workspace;

	private Datasource m_datasource;

	private DatasetVector m_dataset;

	private DatasetGrid m_datasetResult;

	/**
	 * 根据Workspace和MapControl构造SampleRun对象
	 * Initialize the SampleRun object by the workspace and mapControl object.
	 */
	public SampleRun(Workspace workspace, MapControl mapControl) {
		m_workspace = workspace;
		m_mapControl = mapControl;
		m_mapControl.getMap().setWorkspace(m_workspace);
		initialize();
	}

	/**
	 * 对SampleRun初始化
	 * Initialize the SampleRun
	 */
	private void initialize() {
			// 打开工作空间
			// Open the workspace.
		WorkspaceConnectionInfo info = new WorkspaceConnectionInfo(
				"../../SampleData/Interpolation/Interpolation.smwu");
		info.setType(WorkspaceType.SMWU);
		m_workspace.open(info);
		m_datasource = m_workspace.getDatasources().get(0);
		m_dataset = (DatasetVector) m_datasource.getDatasets().get("SamplesP");

		// 将得到的数据集添加到图层中
		// Add the dataset to the layer.
		m_mapControl.getMap().getLayers().add(m_dataset, true);
		m_mapControl.getMap().refresh();
	}

	/**
	 * 距离反比插值（IDW），变长方式查找
	 *  Inverse Distance Weighted method.
	 */
	public void interpolationIDW() {
		// 构造距离反比权值插值参数对象
		// Initialize the interpolationIDW object.
		InterpolationIDWParameter interpolationIDWParameter = new InterpolationIDWParameter();
		// 设置插值范围、分辨率、查找方式（变长）、点数12、最大搜索半径为0、幂次为2
		// Set the parameters.
		interpolationIDWParameter.setBounds(m_dataset.computeBounds());
		interpolationIDWParameter.setResolution(9000);
		interpolationIDWParameter.setSearchMode(SearchMode.KDTREE_FIXED_COUNT);
		interpolationIDWParameter.setExpectedCount(12);
		interpolationIDWParameter.setPower(2);

		// 通过插值参数对象、点数据等7参数进行插值分析
		// Inverse Distance Weighted analyst with the specified paprameters..
		String name = "del_IDW";
		m_datasource.getDatasets().delete(name);
		m_datasetResult = Interpolator.interpolate(interpolationIDWParameter,
				m_dataset, "AVG_TMP", 1, m_datasource, name,
				PixelFormat.SINGLE);

		// 将得到的结果数据集添加到图层中
		// Add the dataset to the layer.
		m_mapControl.getMap().getLayers().clear();
		m_mapControl.getMap().getLayers().add(m_datasetResult, false);
		LayerSettingVector layerSetting = new LayerSettingVector();
		layerSetting.getStyle().setLineColor(Color.BLACK);
		layerSetting.getStyle().setMarkerSize(new Size2D(3,3));
		m_mapControl.getMap().getLayers().add(m_dataset, layerSetting,true);
		ThemeLabel themeLabel = new ThemeLabel();
		themeLabel.setLabelExpression("AVG_TMP");
		themeLabel.setNumericPrecision(1);
		themeLabel.setOffsetFixed(true);
		themeLabel.setOffsetX("60");
		m_mapControl.getMap().getLayers().add(m_dataset, themeLabel,true);
		m_mapControl.getMap().refresh();
	}

	/**
	 * 克吕金插值分析,变长查找
	 * Kriging Interpolation Analyst
	 */
	public void interpolationKriging() {
		// 构造克里金插值参数对像
		// Initialize the parameters
		InterpolationKrigingParameter parameter = new InterpolationKrigingParameter();
		parameter.setSearchMode(SearchMode.KDTREE_FIXED_COUNT);
		parameter.setExpectedCount(5);
		parameter.setResolution(9000);
		parameter.setBounds(m_dataset.computeBounds());

		// 插值分析
		// Kriging Interpolation Analyst
		String name = "del_Kriging";
		m_datasource.getDatasets().delete(name);
		m_datasetResult = Interpolator.interpolate(parameter, m_dataset,
				"AVG_TMP", 1, m_datasource, name, PixelFormat.SINGLE);

		// 将得到的结果数据集添加到图层中
		// Add the dataset to the layer.
		m_mapControl.getMap().getLayers().clear();
		m_mapControl.getMap().getLayers().add(m_datasetResult, true);
		LayerSettingVector layerSetting = new LayerSettingVector();
		layerSetting.getStyle().setLineColor(Color.BLACK);
		layerSetting.getStyle().setMarkerSize(new Size2D(3,3));
		m_mapControl.getMap().getLayers().add(m_dataset, layerSetting,true);
		ThemeLabel themeLabel = new ThemeLabel();
		themeLabel.setLabelExpression("AVG_TMP");
		themeLabel.setNumericPrecision(1);
		themeLabel.setOffsetFixed(true);
		themeLabel.setOffsetX("60");
		m_mapControl.getMap().getLayers().add(m_dataset, themeLabel,true);
		m_mapControl.getMap().refresh();
	}

	/**
	 * RBF插值分析
	 * Radial Basis Function(RBF) interpolation method
	 */
	public void interpolationRBF() {
		// 构造RBF插值法参数对象
		// Initialize the parameters.
		InterpolationRBFParameter interpolationRBFParameter = new InterpolationRBFParameter();
		// 设置分辨率、范围、张力系数、光滑系数、查找方式为变长查找、查找点为20、光滑系数设置为0.1
		// Set the parameters.
		interpolationRBFParameter.setSearchMode(SearchMode.KDTREE_FIXED_COUNT);
		interpolationRBFParameter.setTension(40);
		interpolationRBFParameter.setExpectedCount(12);
		interpolationRBFParameter.setResolution(9000);
		interpolationRBFParameter.setBounds(m_dataset.computeBounds());
		interpolationRBFParameter.setSmooth(0.1);

		// 插值分析
		// Radial Basis Function(RBF) interpolation method
		String name = "del_RBF";
		m_datasource.getDatasets().delete(name);
		m_datasetResult = Interpolator.interpolate(interpolationRBFParameter,
				m_dataset, "AVG_TMP", 1, m_datasource, name,
				PixelFormat.SINGLE);

		// 将得到的结果数据集添加到图层中
		// Add the dataset to the layer.
		m_mapControl.getMap().getLayers().clear();
		m_mapControl.getMap().getLayers().add(m_datasetResult, true);
		LayerSettingVector layerSetting = new LayerSettingVector();
		layerSetting.getStyle().setLineColor(Color.BLACK);
		layerSetting.getStyle().setMarkerSize(new Size2D(3,3));
		m_mapControl.getMap().getLayers().add(m_dataset, layerSetting,true);
		ThemeLabel themeLabel = new ThemeLabel();
		themeLabel.setLabelExpression("AVG_TMP");
		themeLabel.setNumericPrecision(1);
		themeLabel.setOffsetFixed(true);
		themeLabel.setOffsetX("60");
		m_mapControl.getMap().getLayers().add(m_dataset, themeLabel,true);
		m_mapControl.getMap().refresh();
	}
}

