package com.virtualightning.dlna;

/**
 * Created by xjw04 on 17/6/2.
 */
public class Icon {
    String mimetype;//文件类型
    int width;//宽
    int height;//高
    int depth;//深度
    String url;//图片路径

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getMimetype() {
        return mimetype;
    }

    public String getUrl() {
        return url;
    }
}
