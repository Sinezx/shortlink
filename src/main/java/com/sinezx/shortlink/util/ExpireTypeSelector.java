package com.sinezx.shortlink.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpireTypeSelector {
    private int expire;
    private String expireType;

    public ExpireTypeSelector(int expire, String expireType){
        select(expire, expireType);
    }

    public void select(int expire, String expireType){
        if(expireType != null && (
           (expireType.equals(SystemConstant.DAY) && 1 <= expire && expire <= 5) ||
           (expireType.equals(SystemConstant.HOUR) && 0 < expire && expire <= 24)
        )){
            this.expire = expire;
            this.expireType = expireType;
        }else {
            oneDayExpire();
        }
    }

    public void oneDayExpire(){
        this.expire = 1;
        this.expireType = SystemConstant.DAY;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getExpireType() {
        return expireType;
    }

    public void setExpireType(String expireType) {
        this.expireType = expireType;
    }
}
