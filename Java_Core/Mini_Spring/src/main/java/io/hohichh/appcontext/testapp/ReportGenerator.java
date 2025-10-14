package io.hohichh.appcontext.testapp;

import io.hohichh.appcontext.annotations.Autowired;
import io.hohichh.appcontext.annotations.Component;
import io.hohichh.appcontext.annotations.Scope;
import io.hohichh.appcontext.testapp.interfaces.IUserService;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ReportGenerator {

    @Autowired
    private IUserService userService;

    private String title;
    private final List<String> lines = new ArrayList<>();

    public ReportGenerator() {
        System.out.println(">>> New ReportGenerator instance created!");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void generateUserReport() {
        lines.add("--- User List ---");
        userService.getAllUsers()
                .forEach(user -> lines.add("User: " + user.getName() + " (ID: " + user.getId() + ")"));
    }

    public void addCustomLine(String line) {
        lines.add(line);
    }

    public void printReport() {
        System.out.println("=========================================");
        System.out.println("REPORT: " + (title != null ? title : "Untitled"));
        System.out.println("-----------------------------------------");
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println("=========================================\n");
    }
}