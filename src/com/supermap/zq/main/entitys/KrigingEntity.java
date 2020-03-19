package com.supermap.zq.main.entitys;

import com.supermap.data.Point2D;

/**
 * 分析任务实体类
 * @author GZQ
 */
public class KrigingEntity {
    private Point2D[] p2ds;

    private String type;

    private double[] zValues;

    private double[] vals;

    public KrigingEntity() {
    }

    public Point2D[] getP2ds() {
        return p2ds;
    }

    public void setP2ds(Point2D[] p2ds) {
        this.p2ds = p2ds;
    }

    public String getType() {
        return type;
    }

    public double[] getZValues() {
        return zValues;
    }

    public void setZValues(double[] zValues) {
        this.zValues = zValues;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getVals() {
        return vals;
    }

    public void setVals(double[] vals) {
        this.vals = vals;
    }

    public KrigingEntity(Point2D[] p2ds, String type, double[] vals, double[] zValues) {
        this.p2ds = p2ds;
        this.type = type;
        this.vals = vals;
        this.zValues = zValues;
    }
}
