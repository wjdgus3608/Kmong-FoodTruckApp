package com.jung.foodapp.datautil

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun fromList(list :MutableList<Int>?) : String{
        if(list == null) return ""
        var str=""
        for (num in list){
            str+="$num,"
        }
        return str
    }

    @TypeConverter
    fun toList(str :String) : MutableList<Int>? {
        if(str.isNullOrEmpty()) return null
        val tmpList = str.split(",")
        val retList = ArrayList<Int>()
        for (i in 0 until tmpList.size-1){
            retList.add(tmpList[i].toInt())
        }
        return retList.toMutableList()
    }

}