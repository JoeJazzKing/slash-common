package com.heycine.slash.common.gateway.config.properties;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单配置
 *
 * @author zzj
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "ignore")
@Data
public class IgnoreWhiteProperties {
    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    private List<String> whites = new ArrayList<>();

    /**
     * 白名单动态配置
     */
    public static String[] whitesArrays = null;

    public String[] getWhitesArray() {
        refreshWhitesArray();
        return whitesArrays;
    }

    public void refreshWhitesArray() {
        whitesArrays = ArrayUtil.toArray(whites, String.class);
    }

}
