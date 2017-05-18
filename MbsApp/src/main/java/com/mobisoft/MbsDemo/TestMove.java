package com.mobisoft.MbsDemo;

import java.util.Arrays;

/**
 * Author：Created by fan.xd on 2017/5/16.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class TestMove {

    public static void rcr(int[] a, int k) {

        System.out.println("排序后得数组" + Arrays.toString(newRcr(a, a.length, k)));

    }

    private static int[] newRcr(int[] a, int n, int k) {
        int i, t, count;
        count = 0;
        i = 0;
        t = 0;
        if (k % n == 0) {
            return a;
        }
        if (n % 2 == 0) {
            scortAdd(a, n, k, i, t, count);
        } else {
            sortEven(a, n, k, i, t, count);
        }
        return a;
    }

    /**
     * 奇数个数得数组排序
     *
     * @param a
     * @param n
     * @param k
     * @param i
     * @param t
     * @param count
     */
    private static void scortAdd(int[] a, int n, int k, int i, int t, int count) {
        int temp;
        while (count < n - 1) {
            temp = a[t];
            if (i + k < n) {
                a[t] = a[i + k];
                a[i + k] = temp;
                i = i + k;
            }
            if (count + 1 == (n - 2) / 2) {
                t = 1;
                i = 1;
            }
            count++;
        }
    }

    /**
     * 偶数个数得数组排序
     *
     * @param a
     * @param n
     * @param k
     * @param i
     * @param t
     * @param count
     */
    private static void sortEven(int[] a, int n, int k, int i, int t, int count) {
        int temp;
        while (count < n - 1) {

            temp = a[t];
            if (i + k < n) {
                a[t] = a[i + k];
                a[i + k] = temp;
                i = i + k;
            } else {
                a[t] = a[i - n + k];
                a[i - n + k] = temp;
                i = i - n + k;
            }
            count++;
        }
    }
}
