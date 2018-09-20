package cn.corner.web.core.validate.code;

public enum ValidateCodeType {
    IMAGE("image"),SMS("sms");

    private String type;

    ValidateCodeType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
