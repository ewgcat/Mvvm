package com.lishuaihua.index.bean;

import java.util.List;

public class SecondItemListBean {

    /**
     * imgUrl : http://tech.gialen.com/cmsstatic/product/13836/1560243954264268.jpg
     * att : {"marketPrice":29.8,"salePrice":19.9,"commission":12.8}
     * mdp : null
     * link :
     * linkType : null
     * itemList : null
     * id : 0
     * type : product
     * title : 测试商品
     */

    private String imgUrl;
    private AttBean att;
    private String mdp;
    private String link;
    private String linkType;
    private Object itemList;
    private int id;
    private int totalInventory;
    private String type;
    private String title;
    private boolean soldOut;
    private String takeMsg;
    public String getTakeMsg() {
        return takeMsg;
    }
    public void setTakeMsg(String takeMsg) {
        this.takeMsg = takeMsg;
    }
    private String flagUrl;
    private String crossBorderTag;
    private String countryTag;
    public String getFlagUrl() {
        return flagUrl;
    }
    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }
    public String getCrossBorderTag() {
        return crossBorderTag;
    }
    public void setCrossBorderTag(String crossBorderTag) {
        this.crossBorderTag = crossBorderTag;
    }
    public String getCountryTag() {
        return countryTag;
    }
    public void setCountryTag(String countryTag) {
        this.countryTag = countryTag;
    }

    public boolean getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public int getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(int totalInventory) {
        this.totalInventory = totalInventory;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public AttBean getAtt() {
        return att;
    }

    public void setAtt(AttBean att) {
        this.att = att;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public Object getItemList() {
        return itemList;
    }

    public void setItemList(Object itemList) {
        this.itemList = itemList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class AttBean {
        /**
         * marketPrice : 29.8
         * salePrice : 19.9
         * commission : 12.8
         */

        private double marketPrice;
        private double salePrice;
        private double commission;
        private String color;
        private String statusDesc;
        private Boolean checked;
        private Boolean status;
        private List<TagBean> tags;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<TagBean> getTags() {
            return tags;
        }

        public void setTags(List<TagBean> tags) {
            this.tags = tags;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }
        public static class TagBean {
            private Integer tagType;//0 新品 1 商品标签 2 活动标签
            private String tagValue;

            public Integer getTagType() {
                return tagType;
            }

            public void setTagType(Integer tagType) {
                this.tagType = tagType;
            }

            public String getTagValue() {
                return tagValue;
            }

            public void setTagValue(String tagValue) {
                this.tagValue = tagValue;
            }
        }
    }
}

