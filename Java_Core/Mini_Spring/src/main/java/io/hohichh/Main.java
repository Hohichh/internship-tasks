package io.hohichh;

import io.hohichh.appcontext.MiniApplicationContext;
import io.hohichh.appcontext.testapp.Controller;

import io.hohichh.appcontext.testapp.interfaces.*;
import io.hohichh.appcontext.testapp.ReportGenerator;


public class Main {
    public static void main(String[] args) {
        MiniApplicationContext context = new MiniApplicationContext("io.hohichh.appcontext.testapp");
        Controller ctrl = context.getBean(Controller.class);
        ctrl.runApplicationScenario();

        System.out.println("---Test scope: Singleton ---");
        IUserService userService1 = context.getBean(IUserService.class);
        IUserService userService2 = context.getBean(IUserService.class);

        System.out.println("1st IUserService: " + userService1);
        System.out.println("2nd IUserService: " + userService2);

        if (userService1 == userService2) {
            System.out.println(">>> Success! Same reference to object!\n");
        } else {
            System.out.println(">>> Error! Different reference to object!\n");
        }


        System.out.println("--- Test scope: Prototype ---");
        ReportGenerator report1 = context.getBean(ReportGenerator.class);
        ReportGenerator report2 = context.getBean(ReportGenerator.class);

        System.out.println("1st ReportGenerator: " + report1);
        System.out.println("2nd ReportGenerator: " + report2);

        if (report1 != report2) {
            System.out.println(">>> Success! Different reference to object!\n");
        } else {
            System.out.println(">>> Error! Same reference to object!\n");
        }

        report1.setTitle("Report 1");
        report2.setTitle("Diff report 2");

        System.out.println("Reports:");
        report1.printReport();
        report2.printReport();
    }
}