package com.cnh.dataobject;

public class UserInfoDo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.id
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.username
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.phonenumber
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    private String phonenumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.message
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    private String message;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.id
     *
     * @return the value of user_info.id
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.id
     *
     * @param id the value for user_info.id
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.username
     *
     * @return the value of user_info.username
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.username
     *
     * @param username the value for user_info.username
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.phonenumber
     *
     * @return the value of user_info.phonenumber
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.phonenumber
     *
     * @param phonenumber the value for user_info.phonenumber
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber == null ? null : phonenumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.message
     *
     * @return the value of user_info.message
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.message
     *
     * @param message the value for user_info.message
     *
     * @mbg.generated Sat May 18 15:54:34 CST 2019
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}