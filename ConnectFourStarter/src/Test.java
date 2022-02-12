public class Test {
        public static void main(String[] args) {
            int[] array = {7,20,3,4,14,9,6,49};
            int min;
            min=array[0];
            int k=0;
            System.out.print(min+" ");
            for(int i=1;i<array.length;i++){
                System.out.print(array[i]+" ");
                if (array[i]<min){
                    min = array[i];
                    k=i;
                }
            }
            System.out.println("最小值="+array[k]);

            //删除最小的 number
            int[] newNumbers = new int[array.length - 1];
            for (int j=0; j < newNumbers.length; j++) {
                    if (j >= k) {
                        newNumbers[j] = array[j+1];
                    } else  {
                        newNumbers[j] = array[j];
                    }
            }

            for (int s = 0; s < newNumbers.length; s++) {
                System.out.print(newNumbers[s]+" ");
            }
        }
}
