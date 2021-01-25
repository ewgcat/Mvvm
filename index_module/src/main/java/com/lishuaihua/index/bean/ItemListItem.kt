package com.lishuaihua.index.bean

import com.google.gson.annotations.SerializedName
import com.lishuaihua.paging3.DifferData

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
    val itemList: Any? = null,
    @SerializedName("linkType")
    val linkType: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("crossBorderTag")
    val crossBorderTag: String? = null,
    @SerializedName("countryTag")
    val countryTag: String? = null
) : DifferData {
    override fun areItemsTheSame(d: DifferData): Boolean {
        return (d as? ItemListItem)?.id == id
    }

    override fun areContentsTheSame(d: DifferData): Boolean {
        return (d as? ItemListItem)?.title == title
    }
}