package com.hill.devlibs.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hill.devlibs.tools.Log;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * Created by Hill on 2017/3/7.
 */
//
public  class   ValueObjContainer <VO extends Serializable>  implements Parcelable {
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final int UnKnow=-1,
                            ItemData=0
                            ;

    private VO obj;

    public int dataType=UnKnow;
    public static final Creator<ValueObjContainer> CREATOR=new Creator(){

        @Override
        public ValueObjContainer createFromParcel(Parcel source) {

            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
//            T gateway=new T();
            int dataType=source.readInt();
            Serializable data= source.readSerializable();
            Log.d("createFromParcel->"+data+" dataType->"+dataType);
            ValueObjContainer valueObjContainer=new ValueObjContainer();
            valueObjContainer.setValueObjData(data,dataType);
//            switch (dataType){
//                case ItemData:
//                    valueObjContainer.setValueObjData((Car1ItemData)initData,dataType);
//                    break;
//                case Product:
//                    ProductVO.Product itemProduct=(ProductVO.Product)source.readSerializable()
//                    break;
//            }
            return valueObjContainer;
        }

        @Override
        public ValueObjContainer[] newArray(int size) {
//            Log.d("newArray->"+size);
            return new ValueObjContainer[size];
        }
    };

    public ValueObjContainer setValueObjData(VO obj,int t){
        this.obj=obj;
        dataType=t;
        return this;
    }
    public ValueObjContainer setValueObjData(VO obj){
        this.obj=obj;
        dataType=ItemData;
        return this;
    }

    public  @NotNull VO getValueObj(){
        return obj;
    }

  
    @Override
    public int describeContents() {
//        Log.d("describeContents");
        return dataType;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        Log.e("writeToParcel initData type->"+dataType);
        dest.writeInt(dataType);
        dest.writeSerializable(obj);
    }
}
