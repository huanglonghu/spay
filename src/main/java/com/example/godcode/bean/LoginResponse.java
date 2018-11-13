package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/5/17.
 */

public class LoginResponse {


    /**
     * result : {"accessToken":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjYiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic3kxNTM1OTg0MDgxIiwiQXNwTmV0LklkZW50aXR5LlNlY3VyaXR5U3RhbXAiOiJiMjk2ZTdlOS04NjM4LWU4NTYtOTYzOS0zOWU4YjAzZTdlN2EiLCJodHRwOi8vd3d3LmFzcG5ldGJvaWxlcnBsYXRlLmNvbS9pZGVudGl0eS9jbGFpbXMvdGVuYW50SWQiOiIxIiwic3ViIjoiNiIsImp0aSI6IjcyYjUwYTc5LWUzNTMtNGE1Ny1iZTA4LTlkM2Q4NWM1MjQ2YiIsImlhdCI6MTUzNjMwNjc0OCwibmJmIjoxNTM2MzA2NzQ4LCJleHAiOjE1MzYzMTM5NDgsImlzcyI6IkdvZENvZGUiLCJhdWQiOiJHb2RDb2RlIn0.Guoigv0amsWRWOySnAJt454w9RVCkj5iwq4opJesBn0","encryptedAccessToken":"wNYmO41/48SHNstaLVXxHCCre29BZQl1NhC6NM3R3rzpXtPQxVzH6jEzA/QhXFN5tu6Fk7pO53uppm1mVXMZgxbyRVz26dnepi/FyB6axBY+6gq1GL+uRQgoiFUCjRN2p8w6LevViwKlHyWZZJZO1DGVSjAi1m2U+og9pkHw9/SXYAkZ0oMKKMG4U14uRdix922nagJjM1vCwfZCcUyW5VgJLH4wln2HJAecacfA6Yw56kNP9gJi2yf6GtpppwjvS427xuk6aSsgUcWvOfhm0JPtSMV7oIEY1KtAmAPPWmzH1nJ6Y02+q2r7X7GNjNDhmu4qSiRFvle5FivBIllDH38TBL2PqzpulUiv+rQ9G3zzc4PpneDA7ZPzBAOHBy6CB0iknN4UwN+9j4UVqHY1SAdamOTGTAMonLIcwd0F2FHMdvrPodFygHjLJjgGa9CbjyUNokZNqQq20zMkJt5GczADzWzoLHCqFCWeBU7sTZU+cQIeCH1OvVXoXqCXWIHxruZwgGoP8yuuix1gu2xemnLI92FJTvMrxJ8Kj+81abS5eVM/uZlDTzQM8VK0QfoEFIHvGayaJqz0s8oftlNObiFMgKBxRWxrFS3+BLHJIaNyegm2lNCRqrbIHFJKnqgQCxt401a6NMWh5Y9vvTMN37T1zd3OUDnFKcsuSGear4SRZOhX65FvizTGyn1QmtAsfTkp1y8Mr0mOtk8kdeEuBGKAzUONJ4TFx5YebsNqxc4Y6CCxkxddCRH8l2kkO2WD2fapgJLE23SDPIvqxHRjYVDAoSwrxSHELi464aWm2ihUku3XmAiwLjuKG3MK1rVaFagTQHEK+m/6wvh9tf4XtnOJt8aOI4I0FCXMBj7Thss=","expireInSeconds":7200,"userId":6,"payServerUrl":"http://godcodepay.joinvalue.com:8901","uniquenessToken":"305D4AEAA575345081B7FA514E5D511984AEEE67CDEC19AFFEDC7825ED7E9517646581BEEBB16A4B"}
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private ResultBean result;
    private Object targetUrl;
    private boolean success;
    private Object error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(Object targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean is__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public static class ResultBean {
        /**
         * accessToken : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjYiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic3kxNTM1OTg0MDgxIiwiQXNwTmV0LklkZW50aXR5LlNlY3VyaXR5U3RhbXAiOiJiMjk2ZTdlOS04NjM4LWU4NTYtOTYzOS0zOWU4YjAzZTdlN2EiLCJodHRwOi8vd3d3LmFzcG5ldGJvaWxlcnBsYXRlLmNvbS9pZGVudGl0eS9jbGFpbXMvdGVuYW50SWQiOiIxIiwic3ViIjoiNiIsImp0aSI6IjcyYjUwYTc5LWUzNTMtNGE1Ny1iZTA4LTlkM2Q4NWM1MjQ2YiIsImlhdCI6MTUzNjMwNjc0OCwibmJmIjoxNTM2MzA2NzQ4LCJleHAiOjE1MzYzMTM5NDgsImlzcyI6IkdvZENvZGUiLCJhdWQiOiJHb2RDb2RlIn0.Guoigv0amsWRWOySnAJt454w9RVCkj5iwq4opJesBn0
         * encryptedAccessToken : wNYmO41/48SHNstaLVXxHCCre29BZQl1NhC6NM3R3rzpXtPQxVzH6jEzA/QhXFN5tu6Fk7pO53uppm1mVXMZgxbyRVz26dnepi/FyB6axBY+6gq1GL+uRQgoiFUCjRN2p8w6LevViwKlHyWZZJZO1DGVSjAi1m2U+og9pkHw9/SXYAkZ0oMKKMG4U14uRdix922nagJjM1vCwfZCcUyW5VgJLH4wln2HJAecacfA6Yw56kNP9gJi2yf6GtpppwjvS427xuk6aSsgUcWvOfhm0JPtSMV7oIEY1KtAmAPPWmzH1nJ6Y02+q2r7X7GNjNDhmu4qSiRFvle5FivBIllDH38TBL2PqzpulUiv+rQ9G3zzc4PpneDA7ZPzBAOHBy6CB0iknN4UwN+9j4UVqHY1SAdamOTGTAMonLIcwd0F2FHMdvrPodFygHjLJjgGa9CbjyUNokZNqQq20zMkJt5GczADzWzoLHCqFCWeBU7sTZU+cQIeCH1OvVXoXqCXWIHxruZwgGoP8yuuix1gu2xemnLI92FJTvMrxJ8Kj+81abS5eVM/uZlDTzQM8VK0QfoEFIHvGayaJqz0s8oftlNObiFMgKBxRWxrFS3+BLHJIaNyegm2lNCRqrbIHFJKnqgQCxt401a6NMWh5Y9vvTMN37T1zd3OUDnFKcsuSGear4SRZOhX65FvizTGyn1QmtAsfTkp1y8Mr0mOtk8kdeEuBGKAzUONJ4TFx5YebsNqxc4Y6CCxkxddCRH8l2kkO2WD2fapgJLE23SDPIvqxHRjYVDAoSwrxSHELi464aWm2ihUku3XmAiwLjuKG3MK1rVaFagTQHEK+m/6wvh9tf4XtnOJt8aOI4I0FCXMBj7Thss=
         * expireInSeconds : 7200
         * userId : 6
         * payServerUrl : http://godcodepay.joinvalue.com:8901
         * uniquenessToken : 305D4AEAA575345081B7FA514E5D511984AEEE67CDEC19AFFEDC7825ED7E9517646581BEEBB16A4B
         */

        private String accessToken;
        private String encryptedAccessToken;
        private int expireInSeconds;
        private int userId;
        private String payServerUrl;
        private String uniquenessToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getEncryptedAccessToken() {
            return encryptedAccessToken;
        }

        public void setEncryptedAccessToken(String encryptedAccessToken) {
            this.encryptedAccessToken = encryptedAccessToken;
        }

        public int getExpireInSeconds() {
            return expireInSeconds;
        }

        public void setExpireInSeconds(int expireInSeconds) {
            this.expireInSeconds = expireInSeconds;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPayServerUrl() {
            return payServerUrl;
        }

        public void setPayServerUrl(String payServerUrl) {
            this.payServerUrl = payServerUrl;
        }

        public String getUniquenessToken() {
            return uniquenessToken;
        }

        public void setUniquenessToken(String uniquenessToken) {
            this.uniquenessToken = uniquenessToken;
        }
    }
}
