package model;

public class RateGold {
    private String id;
    private String date;
    private double kt24;
    private double kt22;
    private double kt21;
    private double kt18;
    private double kt9;

    public RateGold() {
    }

    public RateGold(String id, String date, double kt24, double kt22, double kt21, double kt18, double kt9) {
        this.id = id;
        this.date = date;
        this.kt24 = kt24;
        this.kt22 = kt22;
        this.kt21 = kt21;
        this.kt18 = kt18;
        this.kt9 = kt9;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getKt24() {
        return kt24;
    }

    public void setKt24(double kt24) {
        this.kt24 = kt24;
    }

    public double getKt22() {
        return kt22;
    }

    public void setKt22(double kt22) {
        this.kt22 = kt22;
    }

    public double getKt21() {
        return kt21;
    }

    public void setKt21(double kt21) {
        this.kt21 = kt21;
    }

    public double getKt18() {
        return kt18;
    }

    public void setKt18(double kt18) {
        this.kt18 = kt18;
    }

    public double getKt9() {
        return kt9;
    }

    public void setKt9(double kt9) {
        this.kt9 = kt9;
    }


    @Override
    public String toString() {
        return "RateGold{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", kt24=" + kt24 +
                ", kt22=" + kt22 +
                ", kt21=" + kt21 +
                ", kt18=" + kt18 +
                ", kt9=" + kt9 +
                '}';
    }
}
