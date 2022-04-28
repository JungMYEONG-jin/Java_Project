package kakao.learningtest.template;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest(classes = Runnable.class)
public class CalcSumTest {


    @Test
    public void sumTest() throws IOException
    {
        Calculator calculator = new Calculator();
        System.out.println("hello");
        int sum = calculator.calcSum(getClass().getResource("/numbers.txt").getPath());
        Assertions.assertThat(sum).isEqualTo(10);
    }


    static class Calculator
    {
        public Integer calcSum(String filePath) throws IOException
        {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(filePath));
                Integer sum = 0;
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println("line = " + line);
                    sum += Integer.valueOf(line);
                }
                return sum;
            }catch (IOException e)
            {
                System.out.println("e.getMessage() = " + e.getMessage());
                throw e;
            }finally {
                if(br!=null)
                {
                    try{
                        br.close();
                    }catch (IOException e)
                    {
                        System.out.println("e.getMessage() = " + e.getMessage());
                    }
                }
            }
            
        }
    }
}
