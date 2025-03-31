package com.skku.sucpi.util;

public class UserUtil {

    static public Float getCodeFromDepartment(String department) {
        return switch (department) {
            case "소프트웨어학과" -> 1F;
            case "지능형소프트웨어학과" -> 2F;
            case "글로벌융합학과" -> 3F;
            default -> 0F;
        };
    }

    static public String getDepartmentFromCode(Float code) {
        return switch (code.intValue()) {
            case 1 ->"소프트웨어학과";
            case 2 -> "지능형소프트웨어학과";
            case 3 -> "글로벌융합학과";
            default -> "";
        };
    }

    // 율전캠퍼스 체크
    static public boolean checkCampusY (Float code) {
        return switch (code.intValue()) {
            case 1 -> true;
            case 2 -> true;
            case 3 -> false;
            default -> false;
        };
    }
}
