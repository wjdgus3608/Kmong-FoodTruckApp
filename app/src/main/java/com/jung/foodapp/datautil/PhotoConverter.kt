package com.jung.foodapp.datautil

import androidx.room.TypeConverter

class PhotoConverter {
    @TypeConverter
    fun fromList(list :MutableList<String>?) : String{
        if(list == null) return ""
        var str=""
        for (num in list){
            str+="$num,"
        }
        return str
    }

    @TypeConverter
    fun toList(str :String) : MutableList<String>? {
        if(str.isNullOrEmpty()) return null
        val tmpList = str.split(",")
        val retList = ArrayList<String>()
        for (i in 0 until tmpList.size-1){
            retList.add(tmpList[i])
        }
        return retList.toMutableList()
    }

}