package com.example.customerapp.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


class OrderDetails(): Serializable{
     var userUid: String? = null
     var userName: String? = null
     var productNames: MutableList<String> ?=null
     var productPrices: MutableList<String> ?=null
     var productDis: MutableList<String> ?=null
     var productImages: MutableList<String> ?=null
     var discounts: MutableList<String> ?=null
     var productsLefts: MutableList<String> ?=null
     var deliveryCharges: MutableList<String> ?=null
     var productHighlights: MutableList<String> ?=null
     var productQuantity: MutableList<Int> ?=null
    var address:String?=null
    var totalPrice:String?=null
    var phoneNumber:String?=null
    var orderAccepted:Boolean = false
    var paymentReceived:Boolean = false
    var itemPushKey:String?=null
    var currentTime:Long = 0

    constructor(parcel: Parcel):this(){
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() !=0.toByte()
        paymentReceived = parcel.readByte() !=0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }
    constructor(
        userId:String,
        name: String,
        productName: ArrayList<String>,
        productPrice: ArrayList<String>,
        productImage: ArrayList<String>,
        productDis:ArrayList<String>,
        discount: ArrayList<String>,
        productsLefts: ArrayList<String>,
        deliveryCharges:ArrayList<String>,
        productHighlight:ArrayList<String>,
        productQuantity:ArrayList<Int>,
        address:String,
        totalAmount:String,
        phone:String,
        time:Long,
        itemPushKey:String?,
        b: Boolean,
        b1: Boolean
    ):this(){
        this.userUid = userId
        this.userName = name
        this.productNames = productName
        this.productPrices = productPrice
        this.productImages = productImage
        this.productDis = productDis
        this.discounts = discount
        this.productsLefts = productsLefts
        this.deliveryCharges = deliveryCharges
        this.productHighlights = productHighlight
        this.productQuantity = productQuantity
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived




    }

     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if(orderAccepted)1 else 0)
        parcel.writeByte(if(paymentReceived)1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }
     fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<OrderDetails>{
        override fun createFromParcel(parcel: Parcel): OrderDetails {
          return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?>{
            return arrayOfNulls(size)
        }
    }

}