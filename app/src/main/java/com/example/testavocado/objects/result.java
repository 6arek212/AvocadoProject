package com.example.testavocado.objects;

public class result {
    private String state;
    private  String exception;
    private  String json_data;
    //Ctor----------------------------------------------------------------->

    public result(String state, String exception, String json_data) {
        this.state = state;
        this.exception = exception;
        this.json_data = json_data;
    }
    public result()
    {}
    //Get/Set------------------------------------------------------------------>

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getJson_data() {
        return json_data;
    }

    public void setJson_data(String json_data) {
        this.json_data = json_data;
    }
    //toString------------------------------------------------------------------>
    @Override
    public String toString() {
        return "result{" +
                "state='" + state + '\'' +
                ", exception='" + exception + '\'' +
                ", json_data='" + json_data + '\'' +
                '}';
    }
}
