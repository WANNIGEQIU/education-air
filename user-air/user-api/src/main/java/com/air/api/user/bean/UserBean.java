package com.air.api.user.bean;

public class UserBean {

    private String id;



    private String mobile;


    private String username;


    private String password;

    private String account;

    //"性别 1 女 2 男"
    private Integer sex;


    private Integer age;

        //   "盐"
    private String salt;


    private String avatar;

  //     "禁用状态 0 (false) 未禁用 1 禁用 ")
    private Integer prohibit;

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", account='" + account + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", salt='" + salt + '\'' +
                ", avatar='" + avatar + '\'' +
                ", prohibit=" + prohibit +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getProhibit() {
        return prohibit;
    }

    public void setProhibit(Integer prohibit) {
        this.prohibit = prohibit;
    }
}
