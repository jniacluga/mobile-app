package pup.com.gsouapp.Domain;

public class Evaluator {

    private String evaluatorType;
    private String evaluatorName;
    private Double payment;

    public Evaluator() {

    }

    public Evaluator(String evaluatorType, String evaluatorName, Double payment) {
        this.evaluatorType = evaluatorType;
        this.evaluatorName = evaluatorName;
        this.payment = payment;
    }

    public String getEvaluatorType() {
        return evaluatorType;
    }

    public void setEvaluatorType(String evaluatorType) {
        this.evaluatorType = evaluatorType;
    }

    public String getEvaluatorName() {
        return evaluatorName;
    }

    public void setEvaluatorName(String evaluatorName) {
        this.evaluatorName = evaluatorName;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }
}
