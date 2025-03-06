package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFile;

import java.sql.Timestamp;

public class IliveMeetingFile extends BaseILiveMediaFile {


    /**
     * 文件大小描述
     */
    private String fileSizeDesc;

    public String getFileSizeDesc() {
        return fileSizeDesc;
    }

    public void setFileSizeDesc(String fileSizeDesc) {
        this.fileSizeDesc = fileSizeDesc;
    }

    @Override
    public void setFileSize(Long fileSize) {
        super.setFileSize(fileSize);
        this.fileSizeDesc = convertMemorySizeFromKb2Mb(fileSize == null ? 0 : fileSize);
    }

    /**
     * 换算存储量大小：kb-》Mb
     *
     * @param size
     *            存储量大小 kb
     * @return
     */
    private String convertMemorySizeFromKb2Mb(Long size) {
        if (size == null)
            return "0.0";
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }
}
