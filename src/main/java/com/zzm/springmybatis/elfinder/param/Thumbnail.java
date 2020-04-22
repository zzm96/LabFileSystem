package com.zzm.springmybatis.elfinder.param;

import java.math.BigInteger;

public class Thumbnail {
    private BigInteger width=new BigInteger("80");

    public BigInteger getWidth() {
        return width;
    }

    public void setWidth(BigInteger width) {
        this.width = width;
    }
}
