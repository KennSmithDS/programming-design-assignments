package concurrentSolution;

public class InboundCSVRow {

    private String module;
    private String presentation;
    private Integer student;
    private Integer site;
    private Integer date;
    private Integer clicks;

    InboundCSVRow(String module, String presentation, Integer student, Integer site, Integer date, Integer clicks) {
        this.module = module;
        this.presentation = presentation;
        this.student = student;
        this.site = site;
        this.date = date;
        this.clicks = clicks;

    }



}
