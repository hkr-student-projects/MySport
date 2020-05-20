package model.Tools;

public enum Language{
    EN("en"),
    SE("se"),
    NO("no"),
    RS("rs"),
    HR("hr"),
    RU("ru"),
    LV("lv"),
    DE("de");

    private String code;

    Language(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}
