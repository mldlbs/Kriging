package com.supermap.samplecode.spatialanalyst;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.supermap.data.Workspace;
import com.supermap.ui.MapControl;

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
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel m_jContentPane;

	private JToolBar m_jToolBar;

	private JButton m_buttonInterpolationIDW;

	private JButton m_buttonInterpolationKriging;

	private JButton m_buttonInterpolationRBF;

	private MapControl m_mapControl;

	private Workspace m_workspace;

	private SampleRun m_sampleRun;

	/**
	 * 程序入口点
	 * The entrance of the programme.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame thisClass = new MainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * 构造函数
	 * The constructor.
	 */
	public MainFrame() {
		super();
		initialize();
	}

	/**
	 * 初始化窗体
	 * Initialize the forms.
	 */
	private void initialize() {
		// 最大化显示窗体
		// Maximum display the form.
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setSize(800, 500);
		this.setContentPane(getJContentPane());
		this.setTitle(getText("thisTitle"));
		this.addWindowListener(new WindowAdapter() {

			// 在主窗体加载时，初始化SampleRun类型，来完成功能的展现
			// Initialize a SampleRun object when loading the main forms.
			public void windowOpened(WindowEvent e) {
				m_workspace = new Workspace();
				m_sampleRun = new SampleRun(m_workspace, m_mapControl);

			}

			// 在窗体关闭时，需要释放相关的资源
			// Dispose the resources when closing the forms.
			public void windowClosing(WindowEvent e) {
				m_mapControl.dispose();
				m_workspace.dispose();
			}

		});
	}

	/**
	 * 获取m_jToolBar
	 * Get the m_jToolBar
	 */
	private JToolBar getJToolBar() {
		if (m_jToolBar == null) {
			m_jToolBar = new JToolBar();
			m_jToolBar.add(getButtonInterpolationIDW());
			m_jToolBar.add(getButtonInterpolationKriging());
			m_jToolBar.add(getButtonInterpolationRBF());
		}
		return m_jToolBar;
	}

	/**
	 * 获取m_buttonInterpolationIDW
	 * Get the m_buttonInterpolationIDW
	 */
	private JButton getButtonInterpolationIDW() {
		if (m_buttonInterpolationIDW == null) {
			m_buttonInterpolationIDW = new JButton();
			m_buttonInterpolationIDW.setText(getText("m_buttonInterpolationIDW"));
			m_buttonInterpolationIDW.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					m_sampleRun.interpolationIDW();

				}

			});
		}
		return m_buttonInterpolationIDW;
	}

	/**
	 * 获取m_buttonInterpolationKriging
	 * Get the m_buttonInterpolationKriging
	 */
	private JButton getButtonInterpolationKriging() {
		if (m_buttonInterpolationKriging == null) {
			m_buttonInterpolationKriging = new JButton();
			m_buttonInterpolationKriging.setText(getText("m_buttonInterpolationKriging"));
			m_buttonInterpolationKriging
					.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							m_sampleRun.interpolationKriging();

						}

					});
		}
		return m_buttonInterpolationKriging;
	}

	/**
	 * 获取m_buttonInterpolationRBF
	 * Get the m_buttonInterpolationRBF
	 */
	private JButton getButtonInterpolationRBF() {
		if (m_buttonInterpolationRBF == null) {
			m_buttonInterpolationRBF = new JButton();
			m_buttonInterpolationRBF.setText(getText("m_buttonInterpolationRBF"));
			m_buttonInterpolationRBF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					m_sampleRun.interpolationRBF();
				}

			});
		}
		return m_buttonInterpolationRBF;
	}

	/**
	 * 	获取m_mapControl
	 * 	Get the m_mapControl
	 */
	private MapControl getMapControl() {
		if (m_mapControl == null) {
			m_mapControl = new MapControl();
		}
		return m_mapControl;
	}

	/**
	 * 获取m_jContentPane
	 * Get the m_jContentPane
	 */
	private JPanel getJContentPane() {
		if (m_jContentPane == null) {
			m_jContentPane = new JPanel();
			m_jContentPane.setLayout(new BorderLayout());
			m_jContentPane.add(getJToolBar(), BorderLayout.NORTH);
			m_jContentPane.add(getMapControl(), BorderLayout.CENTER);
		}
		return m_jContentPane;
	}
	// 根据语言设置text是中文还是英文
	// To change text to English or Chinese by CurrentCulture
	private String getText(String id) {		
		String text = "";
        if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
        {      	        
        	if(id == "thisTitle")
        	{       		
        		text = "插值分析";
        	}
        	if(id == "m_buttonInterpolationIDW")
        	{       		
        		text = "IDW插值";
        	}
        	if(id == "m_buttonInterpolationKriging")
        	{       		
        		text = "Kriging插值";
        	}
        	if(id == "m_buttonInterpolationRBF")
        	{       		
        		text = "RBF插值";
        	}         	
        }
        else
        {
        	if(id == "thisTitle")
        	{       		
        		text = "Interpolator";
        	}
        	if(id == "m_buttonInterpolationIDW")
        	{       		
        		text = "IDW Interpolar";
        	}
        	if(id == "m_buttonInterpolationKriging")
        	{       		
        		text = "Kriging Interpolar";
        	}
        	if(id == "m_buttonInterpolationRBF")
        	{       		
        		text = "RBF Interpolar";
        	}      	
        }        
        return text;	
	}
}

