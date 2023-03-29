//{ Driver Code Starts
import java.lang.*;
import java.io.*;
import java.util.*;
class Test
{
    public static void main (String[] args) throws IOException
    {

            System.out.println(Solution.minJumps(new int[]{9, 10, 1, 2, 3, 4, 8, 0, 0, 0, 0, 0, 0, 0, 1}));
        }

    class Solution{
        static int minJumps(int[] arr){
            // your code here
            int len = arr.length;
            int jumps = 0;
            int pointer = 0;
            while(pointer < len - 1){
                if(arr[pointer] == 0 && pointer < len-1) return -1;
                if(arr[pointer] == 1) {
                    pointer++;
                    jumps++;
                    continue;
                }
                int diff = len - (arr[pointer] + pointer + 1);
                if(diff <= 0) return ++jumps;
                int cursor = 1;
                for(int i=pointer+1; i <= arr[pointer]; ++i){
                    int df = len - (arr[i] + cursor);
                    if(df < diff){
                        diff = df;
                        pointer = i;
                    }
                    cursor++;
                }
                jumps++;
            }
            return jumps;
        }
    }

}

// } Driver Code Ends


