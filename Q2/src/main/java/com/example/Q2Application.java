package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

@SpringBootApplication
public class Q2Application {

    public static void main(String[] args)
    {
        SpringApplication.run(Q2Application.class, args);

        Scanner sc = new Scanner(System.in).useDelimiter("\n");
        Logic logic = new Logic();
        InputVerfiy inputVerfiy = new InputVerfiy();
        while(true)
        {
            System.out.println("Please enter the command in below format.");
            System.out.println("<Rover 1 start position & facing> <Rover 1 command> <Optional:Rover 2 start position & facing> <Optional:Rover 2 command>");
            System.out.println("Position can be negative, but must be whole value.");
            System.out.println("Facing only accept N=North, S=South, E=East, W=West. Case sensitive.");
            System.out.println("Action command only accept lower case alphabet. f= move front, b= move back, l=rotate 90 degree anti clockwise, r=rotate 90 degree clockwise.");
            System.out.println("Example");
            System.out.println("“3,4,N” “f,f,r,f,f” “-3,4,S” “f,f,r,f,f”");

            boolean dummy = inputVerfiy.VerfiyInput(sc.next(), logic);



        }


    }

}
