package concurrentSolution;

import java.util.Objects;

/**
 * Simple class to represent the row of a CSV file with student click data
 * Only has a constructor and getter methods for primary properties
 */
public class InboundCSVRow {

    private String module;
    private String presentation;
    private Integer student;
    private Integer site;
    private String date;
    private Integer clicks;
    private String codeKey;

    /**
     * Constructor method for InboundCSVRow class expects all row variables to be passed
     * @param module String identification code for a module
     * @param presentation the identification code of the module presentation
     * @param student a unique identification number for the student
     * @param site an identification number for the VLE material
     * @param date the date of student’s interaction with the material measured as the number of days since the start of the module-presentation
     * @param clicks the number of times a student interacts with the material in that day
     */
    InboundCSVRow(String module, String presentation, Integer student, Integer site, String date, Integer clicks) {
        this.module = module;
        this.presentation = presentation;
        this.student = student;
        this.site = site;
        this.date = date;
        this.clicks = clicks;
        this.codeKey = this.module + "_" + this.presentation;
    }

    /**
     * Method to get the code key when building the ConcurrentHashMap
     * @return String concatenation of module and presentation codes
     */
    public String getCodeKey() {
        return codeKey;
    }

    /**
     * Method to get the date integer when building the ConcurrentHasMap
     * @return Integer of the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Method to get the clicks integer when building the ConcurrentHashMap
     * @return Integer of the clicks
     */
    public Integer getClicks() {
        return clicks;
    }

    public void setCodeKey(String newCodeKey) {
        this.codeKey = newCodeKey;
    }

    /**
     * Method to override the default equals()
     * @param o object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InboundCSVRow that = (InboundCSVRow) o;
        return Objects.equals(module, that.module) &&
                Objects.equals(presentation, that.presentation) &&
                Objects.equals(student, that.student) &&
                Objects.equals(site, that.site) &&
                Objects.equals(date, that.date) &&
                Objects.equals(clicks, that.clicks) &&
                Objects.equals(codeKey, that.codeKey);
    }

    /**
     * Method to override the default hashCode()
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(module, presentation, student, site, date, clicks, codeKey);
    }

    /**
     * Method to override the default toString()
     * @return String
     */
    @Override
    public String toString() {
        return "InboundCSVRow{" +
                "module='" + module + '\'' +
                ", presentation='" + presentation + '\'' +
                ", student=" + student +
                ", site=" + site +
                ", date=" + date +
                ", clicks=" + clicks +
                ", codeKey='" + codeKey + '\'' +
                '}';
    }
}
