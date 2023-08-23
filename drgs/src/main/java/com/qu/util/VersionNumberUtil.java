package com.qu.util;

import org.apache.commons.lang3.StringUtils;

public class VersionNumberUtil {

    /**
     * 自动升级版本号，版本号+1
     * @param version
     * @return
     */
    public static String autoUpgradeVersion(String version){
        if (StringUtils.isBlank(version)) {
            version = "1.0.0";
            return version;
        }
        //将版本号拆解成整数数组
        String[] arr = version.split("\\.");
        int[] ints=new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = Integer.valueOf(arr[i]);
        }

        //递归调用
        VersionNumberUtil.autoUpgradeVersion(ints, ints.length - 1);

        //数组转字符串
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ints.length; i++) {
            sb.append(ints[i]);
            if ((i + 1) != ints.length) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * 自动升级版本号，版本号+1
     * @param ints
     * @param index
     */
    private static void autoUpgradeVersion(int[] ints, int index){
        if(index == 0){
            ints[0] = ints[0] + 1;
        }
        else {
            int value = ints[index] + 1;
            if (value < 10) {
                ints[index] = value;
            }
            else {
                ints[index] = 0;
                autoUpgradeVersion(ints, index - 1);
            }
        }
    }

//    public static void main(String[] args) {
//        System.out.println(VersionNumberUtil.autoUpgradeVersion(""));
//        System.out.println(VersionNumberUtil.autoUpgradeVersion("1.0.0"));
//        System.out.println(VersionNumberUtil.autoUpgradeVersion("1.0.1"));
//        System.out.println(VersionNumberUtil.autoUpgradeVersion("1.0.9"));
//        System.out.println(VersionNumberUtil.autoUpgradeVersion("1.9.9"));
//        System.out.println(VersionNumberUtil.autoUpgradeVersion("9.9.9"));
//    }


}
