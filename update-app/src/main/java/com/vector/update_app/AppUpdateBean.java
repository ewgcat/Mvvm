package com.vector.update_app;

public class AppUpdateBean
{
    /**
     * code : 200
     * message : 成功
     * data : {"id":1,"name":"android","systemType":1,"edition":2,"url":"https://fubangrongxing-test.oss-cn-beijing.aliyuncs.com/backstage/test/dongweixichu.apk","type":1,"editionNum":"1.0.0","createTime":null}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : android
         * systemType : 1
         * edition : 2
         * url : https://fubangrongxing-test.oss-cn-beijing.aliyuncs.com/backstage/test/dongweixichu.apk
         * type : 1
         * editionNum : 1.0.0
         * createTime : null
         */

        private int id;
        private String name;
        private int systemType;
        private int edition;
        private String url;
        private int type;
        private String editionNum;
        private Object createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSystemType() {
            return systemType;
        }

        public void setSystemType(int systemType) {
            this.systemType = systemType;
        }

        public int getEdition() {
            return edition;
        }

        public void setEdition(int edition) {
            this.edition = edition;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getEditionNum() {
            return editionNum;
        }

        public void setEditionNum(String editionNum) {
            this.editionNum = editionNum;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }
    }
}
