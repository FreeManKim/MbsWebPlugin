package com.mobisoft.MbsDemo;

/**
 * Author：Created by fan.xd on 2017/5/16.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class TestMove {

    public static void rcr(int[] a, int n, int k) {

        int i, j, t, count, temp;
        count = 0;
        k = k % n;
        if (k != 0) {
            i = 0;
            while (count <= n) {
                j = i;
                t = i;
                temp = a[i];
//                while ()
            }
        }
    }

    public int[] newRcr(int[] a, int n, int k) {
        int i, j, t, count, temp;
        count = 0;
        i = 0;
        int length = a.length;
        while (count <= n) {
            j = i;
            t = i;
            temp = a[i];
            a[i] = a[i + k];
            a[i + k] = temp;
            i++;
            count++;
        }

        return a;
    }
}
