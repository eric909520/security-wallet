package org.secwallet.schedule.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "customer")
public class CyhrCustomerInfo implements Serializable {
    private String id;
    private String name;
    private String idcard;
    private String mobilePhone;
    private String email;
    private String companyName;
    private String adminDep;
    private String salesman;
    private String ssCompany;
    private String ssCompanyRegNo;
    private String ssAccount;
    private String ssAccountType;
    private String ssArea;
    private String ssInsureType;
    private String afCompany;
    private String afCompanyRegNo;
    private String afAccount;
    private String openBank;
    private String openBankNo;
    private String afStatus;
    private String wechatUId;
    private String afAdminDep;
}
