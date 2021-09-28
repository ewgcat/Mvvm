package com.lishuaihua.mvvmframework;

public class Test {


    /**
     * @param numbers int整型一维数组
     * @param target  int整型
     * @return int整型一维数组
     */
    public int[] twoSum(int[] numbers, int target) {
        int[] result ={};
        int num1, num2, num3;
        for (int i = 0; i < numbers.length; i++) {
            num1 = numbers[i];
            for (int j = i + 1; j < numbers.length; j++) {
                num2 = numbers[j];
                num3 = num1 + num2;
                if (num3 == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String s="abcde";
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char temp=chars[i];

        }

    }

}
