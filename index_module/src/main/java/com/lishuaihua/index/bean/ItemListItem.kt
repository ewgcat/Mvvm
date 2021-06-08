package com.lishuaihua.index.bean

import com.google.gson.annotations.SerializedName
import com.lishuaihua.paging3.adapter.DifferData

data class ItemListItem(
    @SerializedName("att")
    val att: Att,
    @SerializedName("imgWidth")
    val imgWidth: Int = 0,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("sort")
    val sort: Int = 0,
    @SerializedName("totalInventory")
    val totalInventory: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("flagUrl")
    val flagUrl: String? = null,
    @SerializedName("soldOut")
    val soldOut: Boolean = false,
    @SerializedName("hotAreaName")
    val hotAreaName: String? = null,
    @SerializedName("imgUrl")
    val imgUrl: String = "",
    @SerializedName("takeMsg")
    val takeMsg: String? = null,
    @SerializedName("mpGhId")
    val mpGhId: String? = null,
    @SerializedName("appId")
    val appId: String? = null,
    @SerializedName("mdp")
    val mdp: String = "",
    @SerializedName("imgHeight")
    val imgHeight: Int = 0,
    @SerializedName("itemList")
    var itemList: List<SecondItemListBean> = emptyList(),
    @SerializedName("linkType")
    val linkType: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("crossBorderTag")
    val crossBorderTag: String? = null,
    @SerializedName("countryTag")
    val countryTag: String? = null,

) : DifferData {
    override fun areItemsTheSame(d: DifferData): Boolean {
        return (d as? ItemListItem)?.id == id
    }

    override fun areContentsTheSame(d: DifferData): Boolean {
        val b1 = (d as? ItemListItem)?.title == title
        val b2 = (d as? ItemListItem)?.att == att
        val b3 = (d as? ItemListItem)?.link == link
        val b4 = (d as? ItemListItem)?.sort == sort
        val b5 = (d as? ItemListItem)?.totalInventory == totalInventory
        val b6 = (d as? ItemListItem)?.type == type
        val b7 = (d as? ItemListItem)?.hotAreaName == hotAreaName
        val b8 = (d as? ItemListItem)?.imgUrl == imgUrl
        val b9 = (d as? ItemListItem)?.takeMsg == takeMsg
        val b10 = (d as? ItemListItem)?.mpGhId == mpGhId
        val b11 = (d as? ItemListItem)?.crossBorderTag == crossBorderTag
        val b12 = (d as? ItemListItem)?.countryTag == countryTag
        val b13 = (d as? ItemListItem)?.mdp == mdp
        val b14 = (d as? ItemListItem)?.linkType == linkType

        return b1&&b2&&b3&&b4&&b5&&b6&&b7&&b8&&b9&&b10&&b11&&b12&&b13&&b14
    }
}